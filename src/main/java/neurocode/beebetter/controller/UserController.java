package neurocode.beebetter.controller;

import neurocode.beebetter.model.User;
import neurocode.beebetter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController 
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping 
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok().body(service.listAll());
    }

    @PostMapping 
    public User create(@RequestBody User user) {
        return service.save(user);
    }
}