package lu.formas.repository;

import lu.formas.repository.model.Vaccin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VaccinRepository extends JpaRepository<Vaccin, Long> {
    List<Vaccin> findAll();
}
