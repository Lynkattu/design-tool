package lynkattu.example.designer_tool.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserIconRepository extends CrudRepository<UserIconEntity, UUID> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_icon SET icon_url = ?1 WHERE id = ?2", nativeQuery = true)
    void updateUserIcon(String icon_url, UUID id);
}
