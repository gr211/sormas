package lu.sormas.views.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.sormas.security.SecurityService;
import lu.sormas.services.NotificationService;
import lu.sormas.services.PatientService;
import lu.sormas.services.UserService;
import lu.sormas.services.VaccineService;
import lu.sormas.views.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed({"USER"})
@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Vaccination card")
public class DashboardView extends VerticalLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    @Autowired
    public DashboardView(PatientService patientService, VaccineService vaccineService, AuthenticationContext authContext, NotificationService notificationService, UserService userService, SecurityService securityService) {

        this.authContext = authContext;
        this.securityService = securityService;

        val userDetails = securityService.getAuthenticatedUser();
        val patient = patientService.byEmail(userDetails.getUsername());
        val user = userService.get(userDetails);

        if (!patient.isPresent() || !user.isPresent()) {
            add(new Div("Patient not found"));
        } else {
            setJustifyContentMode(JustifyContentMode.CENTER);
            setAlignItems(Alignment.CENTER);

            val vaccineNotifications = new VaccineNotifications(notificationService, patient.get());
            val vaccinationHistoryGrid = new VaccinationHistoryGrid(patientService, securityService, vaccineNotifications);
            val vaccinationHistoryView = new VaccinationHistoryView(patient.get(), user.get(), vaccinationHistoryGrid, patientService, vaccineService, vaccineNotifications, securityService);
            add(
                    vaccinationHistoryView,
                    vaccinationHistoryGrid
            );
        }
    }
}