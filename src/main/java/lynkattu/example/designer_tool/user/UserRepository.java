package lynkattu.example.designer_tool.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    Optional<UserEntity> findByUsername(String username);

}
