package lu.formas.views.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.NotificationService;
import lu.formas.services.PatientService;
import lu.formas.services.UserService;
import lu.formas.services.VaccineService;
import lu.formas.views.MainView;
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

            val vaccinationHistoryGrid = new VaccinationHistoryGrid(patientService, securityService);
            val vaccinationHistoryView = new VaccinationHistoryView(patient.get(), user.get(), vaccinationHistoryGrid, patientService, vaccineService, notificationService, securityService);
            add(
                    vaccinationHistoryView,
                    vaccinationHistoryGrid
            );
        }
    }
}