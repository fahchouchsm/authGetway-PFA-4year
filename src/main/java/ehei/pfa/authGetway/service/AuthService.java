package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.RegisterDTO;
import ehei.pfa.authGetway.DTO.UserLoginDTO;
import ehei.pfa.authGetway.DTO.res.RegisterResDTO;
import ehei.pfa.authGetway.config.AppProperties;
import ehei.pfa.authGetway.constant.COOKIE;
import ehei.pfa.authGetway.constant.TIME;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.enums.UserRole;
import ehei.pfa.authGetway.exception.InvalidCredentialsException;
import ehei.pfa.authGetway.exception.UserAlreadyExistsException;
import ehei.pfa.authGetway.exception.UserNotFoundException;
import ehei.pfa.authGetway.mapper.UserMapper;
import ehei.pfa.authGetway.security.InvalidRefreshTokenException;
import ehei.pfa.authGetway.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    private final UserService userService;
    private final StringRedisTemplate redis;
    private final AppProperties appProp;


    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder, UserMapper userMapper, UserService userService, StringRedisTemplate redis, AppProperties appProp) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.userService = userService;
        this.redis = redis;
        this.appProp = appProp;
    }

    @Transactional
    public RegisterResDTO register(RegisterDTO dto) {
        if (userRepository.existsUserByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        UserRole role = (dto.getRole() == null) ? UserRole.USER : dto.getRole();

        if (role == UserRole.COMPANY) {
            if (dto.getWebsite() == null || dto.getWebsite().trim().isEmpty()) {
                throw new InvalidCredentialsException("Website required for company.");
            }
        }

        User user = userMapper.toEntity(dto);
        user.setRole(role);
        user.setPassword(encoder.encode(dto.getPassword()));

        if (role != UserRole.COMPANY) {
            user.setWebsite(null);
        }

        User savedUser = userRepository.save(user);
        userService.sendVerificationEmail(savedUser);
        return userMapper.toRegisterRes(savedUser);
    }

    @Transactional
    public String login(UserLoginDTO dto, HttpServletResponse response) {
        User user = userRepository.findByEmail((dto.getEmail()));
        if(user == null) {
            throw new UserNotFoundException("User with " + dto.getEmail() + " mail not found.");
        }

        if(!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        long maxRefreshAge;
        if(dto.isStayLogin()){
            maxRefreshAge = TIME.WEEK;
        } else {
            maxRefreshAge = TIME.ONEDAY;
        }

        String refreshToken = JwtUtil.genRefreshToken(user.getId(), maxRefreshAge);

        Cookie cookie = new Cookie(COOKIE.REFRESHTOKEN, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(appProp.isUseHttps());
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge((int) (maxRefreshAge / 1000));
        response.addCookie(cookie);

        redis.opsForValue().set("refresh:" + user.getId(), refreshToken, maxRefreshAge, TimeUnit.MILLISECONDS);

        return JwtUtil.genToken(user.getId(), user.getRole());
    }

    public String refreshToken(String refreshToken, HttpServletResponse response) {
        String userId = JwtUtil.validateRefreshToken(refreshToken);
        String storedRedis = redis.opsForValue().get("refresh:" + userId);
        if (!refreshToken.equals(storedRedis)) {
            throw new InvalidRefreshTokenException("Invalid refresh token.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));

        String newRefresh = JwtUtil.genRefreshToken(user.getId(), TIME.THREEDAYS);
        redis.opsForValue().set("refresh:" + userId, newRefresh, TIME.THREEDAYS, TimeUnit.MILLISECONDS);

        Cookie cookie = new Cookie(COOKIE.REFRESHTOKEN, newRefresh);
        cookie.setHttpOnly(true);
        cookie.setSecure(appProp.isUseHttps());
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge((int) (TIME.THREEDAYS / 1000));
        response.addCookie(cookie);

        return JwtUtil.genToken(userId, user.getRole());
    }
}