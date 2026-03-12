package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.AdminCreateUserDTO;
import ehei.pfa.authGetway.DTO.UpdateUserByAdminDTO;
import ehei.pfa.authGetway.DTO.res.UserResDTO;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.enums.UserRole;
import ehei.pfa.authGetway.exception.InvalidCredentialsException;
import ehei.pfa.authGetway.exception.UserAlreadyExistsException;
import ehei.pfa.authGetway.exception.UserNotFoundException;
import ehei.pfa.authGetway.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Transactional
    public UserResDTO changeRole(String id, UserRole role) {
        userRepository.updateRole(id, role);
        return userMapper.toUserResDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found.")));
    }

    @Transactional
    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) throw new UserNotFoundException();
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResDTO updateUser(String id, UpdateUserByAdminDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getWebsite() != null) user.setWebsite(dto.getWebsite());
        return userMapper.toUserResDTO(userRepository.save(user));
    }

    @Transactional
    public UserResDTO createUser(AdminCreateUserDTO dto) {
        if (userRepository.existsUserByEmail(dto.getEmail()))
            throw new UserAlreadyExistsException("Email already in use.");

        UserRole role = dto.getRole() == null ? UserRole.USER : dto.getRole();

        if (role == UserRole.COMPANY && (dto.getWebsite() == null || dto.getWebsite().isBlank()))
            throw new InvalidCredentialsException("Website required for company.");

        User user = userMapper.toEntity(dto);
        user.setRole(role);
        user.setEmailVerified(dto.isEmailVerified());
        user.setPassword(encoder.encode(dto.getPassword()));
        if (role != UserRole.COMPANY) user.setWebsite(null);

        User saved = userRepository.save(user);

        if (!dto.isEmailVerified()) {
            userService.sendVerificationEmail(saved);
        }

        return userMapper.toUserResDTO(saved);
    }
}
