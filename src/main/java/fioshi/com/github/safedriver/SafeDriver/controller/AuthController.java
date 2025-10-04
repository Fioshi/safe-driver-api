package fioshi.com.github.safedriver.SafeDriver.controller;

import fioshi.com.github.safedriver.SafeDriver.dto.UserCreateDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.repository.UserRepository;
import fioshi.com.github.safedriver.SafeDriver.security.dto.JwtAuthenticationResponse;
import fioshi.com.github.safedriver.SafeDriver.security.dto.LoginRequest;
import fioshi.com.github.safedriver.SafeDriver.security.jwt.TokenProvider;
import fioshi.com.github.safedriver.SafeDriver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final SafeDriverMapper safeDriverMapper;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TokenProvider tokenProvider,
                          SafeDriverMapper safeDriverMapper,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.safeDriverMapper = safeDriverMapper;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserCreateDTO userCreateDTO) {
        if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email Address already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = safeDriverMapper.toUser(userCreateDTO);
        user.setHashedPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setCreatedAt(OffsetDateTime.now());

        userService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
