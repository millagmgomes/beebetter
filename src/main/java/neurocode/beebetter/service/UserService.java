package neurocode.beebetter.service;

import neurocode.beebetter.dto.UpdateUserDTO;
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

    public UserResponseDTO update(Long id, UpdateUserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());
        if (dto.gender() != null) user.setGender(dto.gender());
        if (dto.state() != null) user.setState(dto.state());
        if (dto.city() != null) user.setCity(dto.city());
        if (dto.hasTdah() != null) user.setHasTdah(dto.hasTdah());
        if (dto.otherConditions() != null) user.setOtherConditions(dto.otherConditions());
        if (dto.occupation() != null) user.setOccupation(dto.occupation());
        if (dto.symptoms() != null) user.setSymptoms(dto.symptoms());

        return toDTO(repository.save(user));
    }
}