package neurocode.beebetter.security;

import neurocode.beebetter.dto.AuthResponseDTO;
import neurocode.beebetter.dto.LoginRequestDTO;
import neurocode.beebetter.dto.RegisterRequestDTO;
import neurocode.beebetter.dto.UserResponseDTO;
import neurocode.beebetter.model.Mascot;
import neurocode.beebetter.model.ShopItem;
import neurocode.beebetter.model.User;
import neurocode.beebetter.model.UserItem;
import neurocode.beebetter.repository.MascotRepository;
import neurocode.beebetter.repository.ShopItemRepository;
import neurocode.beebetter.repository.UserItemRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private MascotRepository mascotRepository;
    @Autowired private ShopItemRepository shopItemRepository;
    @Autowired private UserItemRepository userItemRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;

    // ─── REGISTER ─────────────────────────────────────────────────────────────
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .birthDate(dto.birthDate())
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

        // Dá os itens padrão ao novo usuário
        darItensPadrao(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, toDTO(user));
    }

    // ─── LOGIN ─────────────────────────────────────────────────────────────────
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Garante que usuários antigos também recebam os itens padrão
        darItensPadrao(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, toDTO(user));
    }

    // ─── LOGIN GOOGLE ──────────────────────────────────────────────────────────
    @Transactional
    public AuthResponseDTO loginWithGoogle(String idToken) throws Exception {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        RestClient restClient = RestClient.create();
        String body = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(body);

        String email = json.get("email").asText();
        String name = json.get("name").asText();

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User novo = User.builder()
                    .name(name)
                    .email(email)
                    .password("")
                    .coins(0)
                    .build();
            userRepository.save(novo);

            Mascot mascot = Mascot.builder()
                    .name("Bee").level(1).experience(0).user(novo).build();
            mascotRepository.save(mascot);

            return novo;
        });

        // Garante itens padrão para usuários Google também
        darItensPadrao(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, toDTO(user));
    }

    // ─── DAR ITENS PADRÃO ─────────────────────────────────────────────────────
    // Busca todos os shop_items com price=0 e associa ao usuário
    // caso ele ainda não os tenha (idempotente — pode chamar várias vezes)
    private void darItensPadrao(User user) {
        try {
            List<ShopItem> itensPadrao = shopItemRepository.findByPrice(0);
            System.out.println("Itens padrão encontrados: " + itensPadrao.size()); // ← add

            for (ShopItem item : itensPadrao) {
                boolean jaTemItem = userItemRepository
                        .existsByUserIdAndShopItemId(user.getId(), item.getId());
                System.out.println("Item " + item.getId() + " - já tem: " + jaTemItem); // ← add

                if (!jaTemItem) {
                    UserItem userItem = UserItem.builder()
                            .user(user)
                            .shopItem(item)
                            .equipped(false)
                            .build();
                    userItemRepository.save(userItem);
                    System.out.println("Item " + item.getId() + " salvo!"); // ← add
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao dar itens padrão: " + e.getMessage());
        }
    }

    // ─── DTO ──────────────────────────────────────────────────────────────────
    private UserResponseDTO toDTO(User user) {
        Integer level = 1;
        Integer experience = 0;

        try {
            Mascot mascot = mascotRepository.findByUserId(user.getId()).orElse(null);
            if (mascot != null) {
                level = mascot.getLevel();
                experience = mascot.getExperience();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar mascot: " + e.getMessage());
        }

        List<String> otherConditions = user.getOtherConditions() != null
                ? List.copyOf(user.getOtherConditions())
                : List.of();

        List<String> symptoms = user.getSymptoms() != null
                ? List.copyOf(user.getSymptoms())
                : List.of();

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
                otherConditions,
                user.getOccupation(),
                symptoms,
                level,
                experience
        );
    }
}