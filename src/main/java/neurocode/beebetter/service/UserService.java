package neurocode.beebetter.service;

import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired 
    private UserRepository repository;

    public List<User> listAll() {
        return repository.findAll();
    }

    public User save(User usuario) {
        return repository.save(usuario);
    }
}