package lu.sormas.repository;

import lu.sormas.repository.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    List<Vaccine> findAll();


}
