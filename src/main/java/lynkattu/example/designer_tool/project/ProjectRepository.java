package lynkattu.example.designer_tool.project;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProjectRepository extends CrudRepository<ProjectEntity, UUID> {
}
