package neurocode.beebetter.service;

import neurocode.beebetter.dto.UserResponseDTO;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired 
    private UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public List<UserResponseDTO> listAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toDTO(user);
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getCoins(),
                user.getProfilePictureUrl()
        );
    }
}