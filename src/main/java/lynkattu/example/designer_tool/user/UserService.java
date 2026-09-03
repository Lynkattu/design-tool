package lynkattu.example.designer_tool.user;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    final private UserRepository repository;
    public UserService (UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDTO>  findAllUsers() {
        Iterable<UserEntity> foundUsers = repository.findAll();
        List<UserDTO> users = new java.util.ArrayList<>();
        for(UserEntity user : foundUsers) {
            users.add(
                    new UserDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPhone()
                    )
            );
        }
        return users;
    }

    public UserDTO findUserById(UUID id) {
        UserEntity foundUser = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return new UserDTO(
                foundUser.getId(),
                foundUser.getFirstName(),
                foundUser.getLastName(),
                foundUser.getUsername(),
                foundUser.getEmail(),
                foundUser.getPhone()
        );
    }

    public UserDTO saveUser(UserEntity userRequest) {
        UserEntity userEntity = repository.save(userRequest);
        return new UserDTO(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userRequest.getPhone()
        );
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
}
