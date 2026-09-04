package lynkattu.example.designer_tool.user;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phone
) {
    public static UserDTO from(UserEntity user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
