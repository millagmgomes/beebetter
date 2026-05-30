package neurocode.beebetter.controller;

import jakarta.validation.Valid;
import neurocode.beebetter.dto.AuthResponseDTO;
import neurocode.beebetter.dto.LoginRequestDTO;
import neurocode.beebetter.dto.RegisterRequestDTO;
import neurocode.beebetter.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponseDTO> loginWithGoogle(@RequestBody Map<String, String> body) throws Exception {
        return ResponseEntity.ok(authService.loginWithGoogle(body.get("idToken")));
    }
}