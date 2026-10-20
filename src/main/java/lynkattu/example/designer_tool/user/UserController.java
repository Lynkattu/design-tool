package lynkattu.example.designer_tool.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    final private UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    private static final Set<String> ALLOWED_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = service.findAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable UUID id) {
        UserDTO user = service.findUserById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username) {
        UserDTO user = service.findUserByUsername(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserEntity userRequest) {
        UserDTO user = service.saveUser(userRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        service.deleteUserById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("User deleted successfully");
    }

    @PostMapping(
            path = "/{id}/icon",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadUserIcon(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
            ) throws IOException {

                if (file.isEmpty()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Please select an image");
                }

                if (!ALLOWED_TYPES.contains(file.getContentType())) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Only JPEG, PNG and WebP are allowed"
                    );
                }

                // Limit upload size to 3 MB
                if (file.getSize() > 3 * 1024 * 1024) {
                    throw new ResponseStatusException(
                            HttpStatus.CONTENT_TOO_LARGE, "Image must be 3 MB or smaller");
                }

                String iconUrl = service.uploadUserIcon(id, file);

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(iconUrl);
    }
}
