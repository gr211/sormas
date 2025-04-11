package lu.formas;

//import com.example.vaadindemo.model.Person;
//import com.example.vaadindemo.service.PersonService;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@RolesAllowed({"USER", "ADMIN"})
//@AnonymousAllowed
public class MainView extends AppLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientService personService;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private Button save = new Button("Save");
    private Button clear = new Button("Clear");
//    private Binder<Person> binder = new Binder<>(Person.class);
//    private Person person = new Person();

    @Autowired
    public MainView(PatientService personService, AuthenticationContext authContext, SecurityService securityService) {
        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = personService;

        val logo = new H1("Vaadin CRM");
        logo.addClassName("logo");

        // Add welcome message with username
        String username = authContext.getPrincipalName().get();
        H2 h2 = new H2("Welcome, " + username + "!");

        HorizontalLayout header;
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click -> securityService.logout());
            header = new HorizontalLayout(logo, logout);
            header.setAlignItems(FlexComponent.Alignment.BASELINE);
        } else {
            header = new HorizontalLayout(logo);
        }

        addToNavbar(header);

        configureGrid();
        configureForm();

        HorizontalLayout formLayout = new HorizontalLayout(firstName, lastName, email);
        HorizontalLayout buttonsLayout = new HorizontalLayout(save, clear);

        VerticalLayout formSection = new VerticalLayout(h2, formLayout, buttonsLayout);

        formSection.addClassName("login-view");
        formSection.setSizeFull();
        formSection.setAlignItems(FlexComponent.Alignment.CENTER);
        formSection.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        setContent(formSection);

        updateList();
        clearForm();

        save.addClickListener(event -> {
            System.out.println("save clicked");
            securityService.getAuthenticatedUser();
//            if (binder.writeBeanIfValid(person)) {
//                personService.save(person);
//                updateList();
//                clearForm();
//            }
        });

        clear.addClickListener(event -> clearForm());
    }

    private void configureGrid() {
//        grid.setColumns("firstName", "lastName", "email");
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));
//        grid.asSingleSelect().addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                binder.readBean(event.getValue());
//                person = event.getValue();
//            } else {
//                clearForm();
//            }
//        });
    }

    private void configureForm() {
//        binder.bindInstanceFields(this);
    }

    private void updateList() {
//        grid.setItems(personService.findAll());
    }

    private void clearForm() {
//        person = new Person();
//        binder.readBean(person);
    }
}