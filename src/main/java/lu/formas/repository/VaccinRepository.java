package lu.formas.repository;

import lu.formas.repository.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VaccinRepository extends JpaRepository<Vaccine, Long> {
    List<Vaccine> findAll();


}
