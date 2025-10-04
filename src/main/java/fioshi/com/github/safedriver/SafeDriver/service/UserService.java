package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.UserCreateDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.UserUpdateDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SafeDriverMapper safeDriverMapper;

    public UserService(UserRepository userRepository, SafeDriverMapper safeDriverMapper) {
        this.userRepository = userRepository;
        this.safeDriverMapper = safeDriverMapper;
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(safeDriverMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> findById(UUID userId) {
        return userRepository.findById(userId).map(safeDriverMapper::toUserResponseDTO);
    }

    public UserResponseDTO save(User user) {
        User savedUser = userRepository.save(user);
        return safeDriverMapper.toUserResponseDTO(savedUser);
    }

    public Optional<UserResponseDTO> update(UUID userId, UserUpdateDTO dto) {
        return userRepository.findById(userId)
                .map(user -> {
                    safeDriverMapper.updateUserFromDto(dto, user);
                    User updatedUser = userRepository.save(user);
                    return safeDriverMapper.toUserResponseDTO(updatedUser);
                });
    }

    public boolean deleteById(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
