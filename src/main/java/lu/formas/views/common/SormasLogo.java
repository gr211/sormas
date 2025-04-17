package lu.formas.views.common;

import com.vaadin.flow.component.html.Image;

public class SormasLogo extends Image {

    public SormasLogo() {

        this.setSrc("images/sormas.png");
        setAlt("Sormas");
        addClassName("logo");
    }
}
