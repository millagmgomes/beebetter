package neurocode.beebetter.service;

import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoinService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Integer addTaskReward(Long userId) {
        return addCoins(userId, 10);
    }

    @Transactional
    public Integer addDailyLoginReward(Long userId) {
        return addCoins(userId, 5);
    }

    public Integer getCoins(Long userId) {
        User user = findUser(userId);
        return user.getCoins();
    }

    private Integer addCoins(Long userId, int amount) {
        User user = findUser(userId);
        user.setCoins(user.getCoins() + amount);
        userRepository.save(user);
        return user.getCoins();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}