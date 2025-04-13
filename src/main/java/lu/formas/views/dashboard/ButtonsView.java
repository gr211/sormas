package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;

public class ButtonsView extends VerticalLayout {

    public ButtonsView() {
        // Set up the main layout
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        // Create buttons layout
        val buttonsLayout = new HorizontalLayout();
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(true);
        buttonsLayout.setWidth("100%");

        // Create first button
        val primaryButton = new Button("Primary Action");
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        primaryButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));
        primaryButton.getStyle().set("padding", "15px 30px");
        primaryButton.getStyle().set("font-size", "18px");
        primaryButton.addClickListener(e ->
                Notification.show("Primary action clicked")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS)
        );

        // Create second button
        val secondaryButton = new Button("Secondary Action");
        secondaryButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        secondaryButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        secondaryButton.getStyle().set("padding", "15px 30px");
        secondaryButton.getStyle().set("font-size", "18px");
        secondaryButton.addClickListener(e ->
                Notification.show("Secondary action clicked")
                        .addThemeVariants(NotificationVariant.LUMO_CONTRAST)
        );

        // Add buttons to layout
        buttonsLayout.add(primaryButton, secondaryButton);

        // Add buttons layout to the main layout
        add(buttonsLayout);
    }
}