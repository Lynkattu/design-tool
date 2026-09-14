package lynkattu.example.designer_tool.project;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<ProjectDTO> findAll() {
        Iterable<ProjectEntity> projectsFound = repository.findAll();
        List<ProjectDTO> projects = new java.util.ArrayList<>();
        for(ProjectEntity project : projectsFound) {
            projects.add(ProjectDTO.from(project));
        }
        return projects;
    }

    public ProjectDTO findById(UUID id) {
        ProjectEntity project = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Project not found"
        ));
        return ProjectDTO.from(project);
    }

    @Transactional
    public void deleteProjectById(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found"
                ));
        repository.deleteById(id);
    }

    public ProjectEntity saveProject(ProjectEntity project) {
        return repository.save(project);
    }
}
