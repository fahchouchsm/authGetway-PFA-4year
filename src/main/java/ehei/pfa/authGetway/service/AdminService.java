package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.res.UserResDTO;
import ehei.pfa.authGetway.database.repository.UserRepository;
import ehei.pfa.authGetway.enums.UserRole;
import ehei.pfa.authGetway.exception.UserNotFoundException;
import ehei.pfa.authGetway.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

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
}
