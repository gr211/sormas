package lu.formas.repository.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
public class Login {
    @Email(message = "Email address is not valid")
    @NotEmpty(message = "Email address is required")
    @Size(message = "Maximum size is 128", max = 128)
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(message = "Maximum size is 128", max = 128)
    private String password;
}
