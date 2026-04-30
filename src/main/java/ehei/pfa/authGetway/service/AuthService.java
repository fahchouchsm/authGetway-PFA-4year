package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.auth.RegisterDTO;
import ehei.pfa.authGetway.DTO.auth.UserLoginDTO;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    private final UserService userService;
    private final StringRedisTemplate redis;
    private final AppProperties appProp;
    private final JwtUtil jwtUtil;

    @Transactional
    public RegisterResDTO register(RegisterDTO dto, HttpServletResponse response) {
        if (userRepository.existsUserByEmail(dto.getEmail()))
            throw new UserAlreadyExistsException("Email already in use");

        UserRole role = (dto.getRole() == null) ? UserRole.USER : dto.getRole();

        User user = userMapper.toEntity(dto);
        user.setRole(role);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setLastName(dto.getLastname());
        User savedUser = userRepository.save(user);
        userService.sendVerificationEmail(savedUser);

        String refreshToken = jwtUtil.genRefreshToken(savedUser.getId(), TIME.ONEDAY);
        setRefreshCookie(response, refreshToken, TIME.ONEDAY);
        redis.opsForValue().set("refresh:" + savedUser.getId(), refreshToken, TIME.ONEDAY, TimeUnit.MILLISECONDS);

        String accessToken = jwtUtil.genToken(savedUser.getId(), savedUser.getRole());
        return userMapper.toRegisterRes(savedUser, accessToken);
    }

    @Transactional
    public String login(UserLoginDTO dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) throw new UserNotFoundException("User with " + dto.getEmail() + " not found.");
        if (!encoder.matches(dto.getPassword(), user.getPassword())) throw new InvalidCredentialsException("Invalid credentials");

        long ttl = dto.isStayLogin() ? TIME.WEEK : TIME.ONEDAY;

        String refreshToken = jwtUtil.genRefreshToken(user.getId(), ttl);
        setRefreshCookie(response, refreshToken, ttl);
        redis.opsForValue().set("refresh:" + user.getId(), refreshToken, ttl, TimeUnit.MILLISECONDS);

        return jwtUtil.genToken(user.getId(), user.getRole());
    }

    public String refreshToken(String refreshToken, HttpServletResponse response) {
        String userId = jwtUtil.validateRefreshToken(refreshToken);
        String stored = redis.opsForValue().get("refresh:" + userId);

        if (!refreshToken.equals(stored)) throw new InvalidRefreshTokenException("Invalid refresh token.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Long remainingTtl = redis.getExpire("refresh:" + userId, TimeUnit.MILLISECONDS);
        long ttl = (remainingTtl != null && remainingTtl > 0) ? remainingTtl : TIME.ONEDAY;

        String newRefresh = jwtUtil.genRefreshToken(userId, ttl);
        setRefreshCookie(response, newRefresh, ttl);
        redis.opsForValue().set("refresh:" + userId, newRefresh, ttl, TimeUnit.MILLISECONDS);

        return jwtUtil.genToken(userId, user.getRole());
    }

    public void logout(String accessToken, String refreshToken, HttpServletResponse response) {
        try {
            var claims = jwtUtil.parseClaims(accessToken);
            long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (ttl > 0) {
                redis.opsForValue().set("blacklist:" + accessToken, "revoked", ttl, TimeUnit.MILLISECONDS);
            }
            redis.delete("refresh:" + claims.getSubject());
        } catch (Exception ignored) {}

        response.addHeader("Set-Cookie", String.format(
                "%s=; Max-Age=0; Path=/auth; HttpOnly; SameSite=Lax",
                COOKIE.REFRESHTOKEN
        ));
    }

    private void setRefreshCookie(HttpServletResponse response, String token, long ttlMillis) {
        String cookie = String.format(
                "%s=%s; Max-Age=%d; Path=/auth; HttpOnly; SameSite=Lax",
                COOKIE.REFRESHTOKEN,
                token,
                (int) (ttlMillis / 1000)
        );
        response.addHeader("Set-Cookie", cookie);
    }
}