package lu.formas.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vaccins")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Vaccin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 256)
    private String name;


    @Min(value = 0)
    private Integer maturityMonth;

    @Size(max = 256)
    private String description;

    @Size(max = 256)
    private String goals;
}
