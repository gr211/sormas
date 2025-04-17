package lu.formas.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "patients")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Firstname is required")
    @Size(message = "Maximum size is 64", max = 64)
    private String firstName;

    @NotEmpty(message = "Lastname is required")
    @Size(message = "Maximum size is 64", max = 64)
    private String lastName;

    @Email(message = "Email address is not valid")
    @NotEmpty(message = "Email address is required")
    @Size(message = "Maximum size is 128", max = 128)
    private String email;

    @NotEmpty
    @Size(message = "Maximum size is 128", max = 128)
    private String password;
}