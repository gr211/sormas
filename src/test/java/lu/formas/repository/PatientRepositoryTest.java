package lu.formas.repository;

import jakarta.persistence.EntityManager;
import lombok.val;
import lu.formas.repository.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PatientRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PatientRepository patientRepository;

    @Test
    void creating_then_deleting_patients() {
        val patient = new Patient();
        patient.setEmail("john@example.com");

        patientRepository.save(patient);

        assertEquals("Existing patient was not found", Optional.of(patient), patientRepository.findByEmail("john@example.com"));

        patientRepository.deletePatientByEmail("john@example.com");
        assertEquals("Existing patient should not have been found", Optional.empty(), patientRepository.findByEmail("john@example.com"));
    }

}
