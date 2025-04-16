package lu.formas.repository;

import jakarta.persistence.EntityManager;
import lombok.val;
import lu.formas.repository.model.Vaccine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class VaccineRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private VaccineRepository vaccineRepository;

    @Test
    void creating_a_vaccine() {
        val vaccine = new Vaccine();
        vaccine.setId(1L);
        vaccine.setName("Vaccine");
        vaccine.setGoals("Goals");
        vaccine.setDescription("Description");

        vaccineRepository.save(vaccine);

        // vaccines are created via data.sql
        assertEquals("15 vaccines should have been found", 1, vaccineRepository.findAll().size());
    }
}
