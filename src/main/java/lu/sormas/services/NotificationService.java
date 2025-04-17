package lu.sormas.services;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.vavr.collection.Stream;
import lombok.val;
import lombok.var;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.Vaccine;
import lu.sormas.services.model.Notifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private final PatientService patientService;
    private final VaccineService vaccineService;

    public NotificationService(PatientService patientService, VaccineService vaccineService) {
        this.patientService = patientService;
        this.vaccineService = vaccineService;
    }

    public long ageInMonths(Patient patient) {
        return ChronoUnit.MONTHS.between(patient.getDob(),
                LocalDate.now().minusDays(1));
    }

    public Notifications notifications(Patient patient) {
        val nextVaccines = nextVaccines(patient);
        val overdueVaccines = overdueVaccines(patient);

        return new Notifications(nextVaccines, overdueVaccines);
    }

    public Notifications notifications(String email) {
        val patient = patientService.byEmail(email);

        if (patient.isPresent()) {
            val nextVaccines = nextVaccines(patient.get());
            val overdueVaccines = overdueVaccines(patient.get());

            return new Notifications(nextVaccines, overdueVaccines);
        }

        return new Notifications(Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Returns a list of vaccines that are coming up for the given patient.
     * <p>
     * Are considered coming up vaccines those that are due at least the next month of the patient.
     * And only the next batch of vaccines are returned.
     * <p>
     * For example, if the patient is 3 months old, and 2 vaccines are due at 4 month, and another 3 are due at 5 months, then
     * only the 2 vaccines due at 4 months are returned.
     * <p>
     * When the patient turns 4 months old, the next batch of vaccines will be 3 vaccines due at 5 months.
     */
    public List<Vaccine> nextVaccines(Patient patient) {
        val months = ageInMonths(patient);

        var alreadyVaccinated = patient.getPatientVaccines();

        val vaccines = Stream.ofAll(vaccineService.vaccines()).filter(vaccine -> Objects.isNull(alreadyVaccinated) ||
                alreadyVaccinated.stream().noneMatch(pv -> pv.getVaccine().equals(vaccine)));

        val futureVaccines = vaccines.filter(vaccine -> vaccine.getMaturityMonth() > months);
        val nextEligibleMaturityMonth = futureVaccines.minBy(Vaccine::getMaturityMonth);

        if (nextEligibleMaturityMonth.isEmpty()) { // no more vaccines coming up
            return Collections.emptyList();
        }

        val eligibleMaturityMonthVaccines = futureVaccines.filter(e -> e.getMaturityMonth().intValue() == nextEligibleMaturityMonth.get().getMaturityMonth().intValue());

        return eligibleMaturityMonthVaccines.collect(Collectors.toList());
    }

    /**
     * Returns a list of overdue vaccines for the given patient.
     * <p>
     * Are considered overdue vaccines those that are due at the current month of the patient.
     * In order words, missed vaccines from previous months are ignored.
     * <p>
     * For example, if the patient is 3 months old and the vaccines are due at 1, 2 and 3 months,
     * <p>
     * -----------------------------------------------------------------------------------------
     * Another aspect to consider is the overdue limit of the vaccine.
     * From the documentation, provided, only the RSV (up to 6 months only) vaccine has an overdue limit of 6 month.
     * <p>
     * The other vaccines do NOT have an overdue limit. And are therefore due for however long the patient is overdue taking them.
     */
    public List<Vaccine> overdueVaccines(Patient patient) {
        val months = ageInMonths(patient);

        val vaccines = vaccineService.vaccines();

        var alreadyVaccinated = patient.getPatientVaccines();

        val pastVaccines = Stream.ofAll(vaccines)
                .filter(vaccine -> vaccine.getMaturityMonth() <= months)
                .filter(vaccine -> !NotificationService.pastOverdueLimit(vaccine, months));

        val overdueVaccines = pastVaccines.filter(vaccine -> Objects.isNull(alreadyVaccinated) ||
                alreadyVaccinated.stream().noneMatch(pv -> pv.getVaccine().equals(vaccine)));

        return overdueVaccines.collect(Collectors.toList());
    }

    public static boolean pastOverdueLimit(Vaccine vaccine, long patientAgeInMonths) {
        return Objects.nonNull(vaccine.getOverdueLimit()) && vaccine.getOverdueLimit() + vaccine.getMaturityMonth() < patientAgeInMonths;
    }

    public static HorizontalLayout showVaccine(Vaccine vaccine) {
        Icon icon = VaadinIcon.DOCTOR.create();
        Span label = new Span(vaccine.getName());

        HorizontalLayout item = new HorizontalLayout(icon, label);
        item.setAlignItems(FlexComponent.Alignment.START);
        item.setSpacing(true);
        return item;
    }
}
