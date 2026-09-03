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
}
