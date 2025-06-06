package lu.sormas.services;

import lu.sormas.repository.VaccineRepository;
import lu.sormas.repository.model.Vaccine;
import lu.sormas.services.model.VaccinesByMaturity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VaccineService {

    private final VaccineRepository vaccinRepository;

    public VaccineService(VaccineRepository vaccinRepository) {
        this.vaccinRepository = vaccinRepository;
    }

    public List<Vaccine> vaccines() {
        return vaccinRepository.findAll(sortByMaturity());
    }

    public VaccinesByMaturity groupedByMaturity() {
        return new VaccinesByMaturity(vaccines());
    }

    private Sort sortByMaturity() {
        return Sort.by(Sort.Direction.ASC, "MaturityMonth");
    }
}
