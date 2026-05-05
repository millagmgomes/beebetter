package neurocode.beebetter.security;

import neurocode.beebetter.dto.AuthResponseDTO;
import neurocode.beebetter.dto.LoginRequestDTO;
import neurocode.beebetter.dto.RegisterRequestDTO;
import neurocode.beebetter.dto.UserResponseDTO;
import neurocode.beebetter.model.Mascot;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.MascotRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private MascotRepository mascotRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .birthDate(dto.birthDate())   // ← novo
                .coins(0)
                .build();

        userRepository.save(user);

        Mascot mascot = Mascot.builder()
                .name("Bee")
                .level(1)
                .experience(0)
                .user(user)
                .build();

        mascotRepository.save(mascot);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, toDTO(user));
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getCoins(),
                user.getProfilePictureUrl(),
                user.getGender(),
                user.getState(),
                user.getCity(),
                user.getHasTdah(),
                user.getOtherConditions(),
                user.getOccupation(),
                user.getSymptoms()
        );
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, toDTO(user));
    }
}