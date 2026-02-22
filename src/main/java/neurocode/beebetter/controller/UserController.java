package neurocode.beebetter.controller;

import neurocode.beebetter.model.User;
import neurocode.beebetter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController 
@RequestMapping("/usuarios") 
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping 
    public List<User> listar() {
        return service.listAll();
    }

    @PostMapping 
    public User criar(@RequestBody User usuario) {
        return service.save(usuario);
    }
}