package lu.formas.views.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import io.vavr.collection.Stream;
import lombok.val;
import lu.formas.repository.model.Patient;
import lu.formas.services.NotificationService;

public class VaccineNotifications extends Div {
    private final NotificationService notificationService;
    private final Patient patient;

    private UnorderedList unorderedList = new UnorderedList();


    public VaccineNotifications(NotificationService notificationService, Patient patient) {
        this.notificationService = notificationService;
        this.patient = patient;

        setClassName("vaccine-notifications-div");

        add(new H3("Overdue vaccinations"), unorderedList);

        refresh();
    }

    public void refresh() {
        val notifications = notificationService.notifications(patient.getEmail());

        setVisible(!notifications.getOverdueVaccines().isEmpty());

        unorderedList.removeAll();

        Stream.ofAll(notifications.getOverdueVaccines())
                .map(NotificationService::showVaccine)
                .map(ListItem::new)
                .forEach(unorderedList::add);
    }
}
