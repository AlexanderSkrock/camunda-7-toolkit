package dev.skrock.camunda.toolkit.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.skrock.camunda.toolkit.config.ToolkitProperties;
import dev.skrock.camunda.toolkit.engine.ConfigurableCamundaEngineProvider;
import dev.skrock.camunda.toolkit.engine.RemoteCamundaEngine;
import dev.skrock.camunda.toolkit.ui.analyze.db.AnalyzeDbToolView;
import dev.skrock.camunda.toolkit.ui.analyze.sap.AnalyzeSapToolView;
import dev.skrock.camunda.toolkit.ui.rest.RestApiView;
import dev.skrock.camunda.toolkit.ui.analyze.dependencies.AnalyzeDependenciesToolView;
import dev.skrock.camunda.toolkit.ui.analyze.variables.AnalyzeVariablesToolView;
import dev.skrock.camunda.toolkit.ui.transfer.export.ExportToolView;
import dev.skrock.camunda.toolkit.ui.transfer.imports.ImportToolView;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

@Layout
@PermitAll
public class RootLayout extends AppLayout {

    public RootLayout(AuthenticationContext authenticationContext, ToolkitProperties toolkitProperties, ConfigurableCamundaEngineProvider engineProvider) {
        addToNavbar(getTopNavigation(authenticationContext, engineProvider, toolkitProperties.getEngines()));

        addToDrawer(getSideNavigation());

        setPrimarySection(Section.DRAWER);
    }

    protected HorizontalLayout getTopNavigation(AuthenticationContext authenticationContext, ConfigurableCamundaEngineProvider engineProvider, List<RemoteCamundaEngine> engines) {
        HorizontalLayout rightSide = new HorizontalLayout();

        ComboBox<RemoteCamundaEngine> engineChooser = new ComboBox<>(event -> {
            RemoteCamundaEngine nextEngine = event.getValue();
            engineProvider.configureEngine(nextEngine);
        });
        engineChooser.setItemLabelGenerator(RemoteCamundaEngine::getName);
        engineChooser.setItems(engines);
        engineChooser.setValue(engineProvider.provide());
        rightSide.add(engineChooser);

        if (authenticationContext.isAuthenticated()) {
            Optional<SwitchUserGrantedAuthority> impersonationAuthority = authenticationContext.getGrantedAuthorities()
                    .stream()
                    .filter(SwitchUserGrantedAuthority.class::isInstance)
                    .map(SwitchUserGrantedAuthority.class::cast)
                    .findFirst();

            if (impersonationAuthority.isPresent()) {
                Button impersonationButton = new Button(VaadinIcon.GLASSES.create(), e -> {
                    e.getSource().getUI().ifPresent(ui -> ui.getPage().setLocation("/impersonate/exit"));
                });
                rightSide.add(impersonationButton);
            } else if (authenticationContext.hasRole(Roles.ADMIN_ROLE)) {
                Dialog impersonationDialog = new Dialog();
                TextField userField = new TextField("Username");
                Button doImpersonateButton = new Button("Impersonate", submitEvent -> {
                    if (StringUtils.isNotBlank(userField.getValue())) {
                        impersonationDialog.close();
                        submitEvent.getSource().getUI().ifPresent(ui -> ui.getPage().setLocation("/impersonate?username=" + userField.getValue()));
                    }
                });
                impersonationDialog.add(userField, doImpersonateButton);
                Button exitImpersonationButton = new Button(VaadinIcon.GLASSES.create(), e -> impersonationDialog.open());
                rightSide.add(exitImpersonationButton, impersonationDialog);
            }

            Button logoutButton = new Button(VaadinIcon.EXIT.create());
            logoutButton.addClickListener(event -> authenticationContext.logout());
            rightSide.add(logoutButton);
        }

        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);
        layout.add(new DrawerToggle());
        layout.addAndExpand(new HorizontalLayout());
        layout.add(rightSide);
        return layout;
    }

    protected Component getSideNavigation() {
        SideNav restNav = new SideNav("REST");
        restNav.setCollapsible(true);
        restNav.addItem(
                new SideNavItem("Browser", RestApiView.class, VaadinIcon.BROWSER.create()),
                new SideNavItem("Batch (coming soon)")
        );

        SideNav analyzeModelsNav = new SideNav("Analyze models");
        analyzeModelsNav.setCollapsible(true);
        analyzeModelsNav.addItem(
                new SideNavItem("Dependencies", AnalyzeDependenciesToolView.class, VaadinIcon.CHART.create()),
                // TODO protect this route with a data experts role
                new SideNavItem("SAP", AnalyzeSapToolView.class, VaadinIcon.CHART.create()),
                // TODO protect this route with a data experts role
                new SideNavItem("Databases", AnalyzeDbToolView.class, VaadinIcon.CHART.create())
        );

        SideNav analyzeProcessesNav = new SideNav("Analyze processes");
        analyzeProcessesNav.setCollapsible(true);
        analyzeProcessesNav.addItem(
                new SideNavItem("Variables", AnalyzeVariablesToolView.class, VaadinIcon.CHART.create())
        );

        SideNav dataTransferNav = new SideNav("Data transfer");
        dataTransferNav.setCollapsible(true);
        dataTransferNav.addItem(
                new SideNavItem("Export", ExportToolView.class, VaadinIcon.DOWNLOAD.create()),
                new SideNavItem("Import", ImportToolView.class, VaadinIcon.UPLOAD.create())
        );

        VerticalLayout navWrapper = new VerticalLayout(restNav, analyzeModelsNav, analyzeProcessesNav, dataTransferNav);
        navWrapper.setSpacing(true);

        Scroller scroller = new Scroller(navWrapper);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        return scroller;
    }
}
