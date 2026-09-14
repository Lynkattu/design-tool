package lynkattu.example.designer_tool.project;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(name = "api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController (ProjectService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectDTO>> findAll() {
        List<ProjectDTO> projects = service.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> findById(@PathVariable UUID id) {
        ProjectDTO project = service.findById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(project);
    }

    @PostMapping("")
    public ResponseEntity<ProjectEntity> saveProject(@Valid @RequestBody ProjectEntity request) {
        ProjectEntity project = service.saveProject(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        service.deleteProjectById(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Project deleted");
    }
}
