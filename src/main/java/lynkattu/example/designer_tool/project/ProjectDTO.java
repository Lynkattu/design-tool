package lynkattu.example.designer_tool.project;


import lynkattu.example.designer_tool.user.UserDTO;

import java.time.LocalDateTime;

public record ProjectDTO(
        String id,
        String name,
        String owner,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
    public static ProjectDTO from(ProjectEntity project) {
        return new ProjectDTO(
                project.getId().toString(),
                project.getName(),
                project.getOwner().getId().toString(),
                project.getUpdatedAt(),
                project.getCreatedAt()
        );
    }
}
