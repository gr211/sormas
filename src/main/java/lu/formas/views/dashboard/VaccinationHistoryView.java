package lu.formas.views.dashboard;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;

public class VaccinationHistoryView extends VerticalLayout {

    public VaccinationHistoryView() {
        // Set up the main layout
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        val title = new H1("Vaccination History");

        add(title);
    }
}