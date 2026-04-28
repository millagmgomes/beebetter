package neurocode.beebetter.service;

import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ProfilePictureService {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String upload(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.endsWith(".jpg")
                && !originalFilename.endsWith(".jpeg")
                && !originalFilename.endsWith(".png"))) {
            throw new RuntimeException("Arquivo deve ser uma imagem JPG ou PNG");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "/uploads/profile-pictures/" + fileName;
        user.setProfilePictureUrl(fileUrl);
        userRepository.save(user);

        return fileUrl;
    }
}