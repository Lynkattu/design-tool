package lynkattu.example.designer_tool.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String username;
    @NotBlank
    @Email
    private String email;
    private String phone;
    @NotBlank
    private String password;

    // default constructor is just for jpa
    protected UserEntity() {}

    public UserEntity(String firstName, String lastName, String email, String phone, String password, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, firstName='%s', lastName='%s', email='%s', phone='%s']",
                id, firstName, lastName, email, phone);
    }

    // getters
    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return  password;
    }
}
