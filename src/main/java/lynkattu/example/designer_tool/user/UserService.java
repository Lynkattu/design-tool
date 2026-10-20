package lynkattu.example.designer_tool.user;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    final private UserRepository repository;
    final private PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final Path uploadDir =
            Paths.get("uploads/user-icons").toAbsolutePath().normalize();





    public UserService (UserRepository repository) {
        this.repository = repository;
    }

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean checkPassword(String password, String storedHash) {
        return encoder.matches(password, storedHash);
    }

    public List<UserDTO>  findAllUsers() {
        Iterable<UserEntity> foundUsers = repository.findAll();
        List<UserDTO> users = new java.util.ArrayList<>();
        for(UserEntity user : foundUsers) {
            users.add(UserDTO.from(user));
        }
        return users;
    }

    public UserDTO findUserById(UUID id) {
        UserEntity foundUser = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        return UserDTO.from(foundUser);
    }

    public UserDTO findUserByUsername(String username) {
        UserEntity foundUser = repository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        return UserDTO.from(foundUser);
    }

    public UserDTO saveUser(UserEntity user) {
        user.setPassword(hashPassword(user.getPassword()));

        repository.save(user);

        return UserDTO.from(user);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        repository.deleteById(id);
    }

    @Transactional
    public String uploadUserIcon(UUID id, MultipartFile file) throws IOException {
        Files.createDirectories(uploadDir);

        String originalName = file.getOriginalFilename();
        String extension = originalName != null
                && originalName.toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)$")
                ? originalName.substring(originalName.lastIndexOf('.'))
                .toLowerCase()
                : switch (file.getContentType()) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case null -> null;
            default -> ".jpg";
        };

        String filename = UUID.randomUUID() + extension;
        Path destination = uploadDir.resolve(filename).normalize();
        Files.copy(file.getInputStream(), destination);
        return filename;
    }

}
