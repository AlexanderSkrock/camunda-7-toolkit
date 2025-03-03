package dev.skrock.camunda.toolkit.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DownloadButton extends Anchor {

    public DownloadButton() {
        Button downloadButton = new Button();
        downloadButton.setIcon(new Icon(VaadinIcon.DOWNLOAD));
        downloadButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        add(downloadButton);

        getElement().setAttribute("download", true);
    }
}
