package lynkattu.example.designer_tool.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    final private UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

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

    @PostMapping("")
    public ResponseEntity<UserDTO> postUser(@Valid @RequestBody UserEntity userRequest) {
        UserDTO user = service.saveUser(userRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        service.deleteUserById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("User deleted successfully");
    }

}
