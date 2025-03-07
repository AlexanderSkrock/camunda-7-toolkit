package dev.skrock.camunda.toolkit.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.skrock.camunda.toolkit.config.ToolkitProperties;
import dev.skrock.camunda.toolkit.engine.ConfigurableCamundaEngineProvider;
import dev.skrock.camunda.toolkit.engine.RemoteCamundaEngine;
import dev.skrock.camunda.toolkit.ui.rest.RestApiView;
import dev.skrock.camunda.toolkit.ui.analyze.dependencies.AnalyzeDependenciesToolView;
import dev.skrock.camunda.toolkit.ui.analyze.variables.AnalyzeVariablesToolView;
import dev.skrock.camunda.toolkit.ui.transfer.export.ExportToolView;
import dev.skrock.camunda.toolkit.ui.transfer.imports.ImportToolView;

import java.util.List;

@Layout
public class RootLayout extends AppLayout {

    public RootLayout(ToolkitProperties toolkitProperties, ConfigurableCamundaEngineProvider engineProvider) {
        addToNavbar(getTopNavigation(engineProvider, toolkitProperties.getEngines()));

        addToDrawer(getSideNavigation());

        setPrimarySection(Section.DRAWER);
    }

    protected HorizontalLayout getTopNavigation(ConfigurableCamundaEngineProvider engineProvider, List<RemoteCamundaEngine> engines) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        ComboBox<RemoteCamundaEngine> engineChooser = new ComboBox<>(event -> {
            RemoteCamundaEngine nextEngine = event.getValue();
            engineProvider.configureEngine(nextEngine);
        });
        engineChooser.setItemLabelGenerator(RemoteCamundaEngine::getName);
        engineChooser.setItems(engines);
        engineChooser.setValue(engineProvider.provide());

        layout.add(new DrawerToggle());
        layout.addAndExpand(new HorizontalLayout());
        layout.add(engineChooser);

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
                new SideNavItem("SAP (coming soon)"),
                new SideNavItem("Databases (coming soon)")
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
