package neurocode.beebetter.service;

import neurocode.beebetter.model.Mascot;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.MascotRepository;
import neurocode.beebetter.repository.UserRepository;
import neurocode.beebetter.security.OAuthUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private MascotRepository mascotRepository;

    @Transactional
    public User processOAuthLogin(OAuthUserInfo userInfo) {

        if (userInfo.getEmail() == null) {
            throw new RuntimeException("Não foi possível obter o email do provedor " + userInfo.getProvider());
        }

        return userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createNewUser(userInfo));
    }

    private User createNewUser(OAuthUserInfo userInfo) {
        User user = User.builder()
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .password("")
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

        return user;
    }
}