package neurocode.beebetter.controller;

import java.io.IOException;

import neurocode.beebetter.dto.UpdateUserDTO;
import neurocode.beebetter.dto.UserResponseDTO;
import neurocode.beebetter.service.CoinService;
import neurocode.beebetter.service.DailyProgressService;
import neurocode.beebetter.service.ProfilePictureService;
import neurocode.beebetter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private CoinService coinService;
    @Autowired private ProfilePictureService profilePictureService;
    @Autowired private DailyProgressService dailyProgressService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @PostMapping("/{id}/daily-login")
    public ResponseEntity<Map<String, Object>> dailyLogin(@PathVariable Long id) {
        boolean firstLogin = dailyProgressService.registerDailyLogin(id);
        Integer coins = coinService.getCoins(id);

        return ResponseEntity.ok(Map.of(
                "firstLoginToday", firstLogin,
                "coins", coins
        ));
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        System.out.println("=== UPLOAD FOTO ===");
        System.out.println("userId: " + id);
        System.out.println("file: " + file.getOriginalFilename());

        String url = profilePictureService.upload(id, file);
        return ResponseEntity.ok(Map.of("profilePictureUrl", url));
    }
}