package lu.sormas.views.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.vavr.collection.Stream;
import lombok.val;
import lu.sormas.repository.model.Patient;
import lu.sormas.services.NotificationService;

public class VaccineNotifications extends VerticalLayout {
    private final NotificationService notificationService;
    private final Patient patient;

    private Div dueVaccinations = new Div();
    private Div upcomingVaccinations = new Div();
    private UnorderedList dueVaccinationsList = new UnorderedList();
    private UnorderedList upcomingVaccinationsList = new UnorderedList();


    public VaccineNotifications(NotificationService notificationService, Patient patient) {
        this.notificationService = notificationService;
        this.patient = patient;

        dueVaccinations.setClassName("due-vaccine-notifications-div");
        dueVaccinations.add(new H3("Due vaccinations"), dueVaccinationsList);

        upcomingVaccinations.setClassName("upcoming-vaccine-notifications-div");
        upcomingVaccinations.add(new H3("Next vaccinations"), upcomingVaccinationsList);

        add(dueVaccinations, upcomingVaccinations);

        refresh();
    }

    public void refresh() {
        val notifications = notificationService.notifications(patient.getEmail());

        dueVaccinations.setVisible(!notifications.getOverdueVaccines().isEmpty());
        upcomingVaccinations.setVisible(!notifications.getNextVaccines().isEmpty());

        dueVaccinationsList.removeAll();
        upcomingVaccinationsList.removeAll();

        Stream.ofAll(notifications.getOverdueVaccines())
                .map(NotificationService::showVaccine)
                .map(ListItem::new)
                .forEach(dueVaccinationsList::add);

        Stream.ofAll(notifications.getNextVaccines())
                .map(NotificationService::showVaccine)
                .map(ListItem::new)
                .forEach(upcomingVaccinationsList::add);
    }
}
