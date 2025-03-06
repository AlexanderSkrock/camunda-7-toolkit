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
import dev.skrock.camunda.toolkit.ui.tools.analyze.dependencies.AnalyzeDependenciesToolView;
import dev.skrock.camunda.toolkit.ui.tools.analyze.variables.AnalyzeVariablesToolView;
import dev.skrock.camunda.toolkit.ui.tools.export.ExportToolView;
import dev.skrock.camunda.toolkit.ui.tools.imports.ImportToolView;

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
        restNav.addItem(
                new SideNavItem("Browser", RestApiView.class, VaadinIcon.BROWSER.create())
        );

        SideNav toolsNav = new SideNav("Tools");
        toolsNav.addItem(
                new SideNavItem("Export", ExportToolView.class, VaadinIcon.DOWNLOAD.create()),
                new SideNavItem("Import", ImportToolView.class, VaadinIcon.UPLOAD.create()),
                new SideNavItem("Analyze dependencies", AnalyzeDependenciesToolView.class, VaadinIcon.CHART.create()),
                new SideNavItem("Analyze variables", AnalyzeVariablesToolView.class, VaadinIcon.CHART.create())
        );

        VerticalLayout navWrapper = new VerticalLayout(restNav, toolsNav);
        navWrapper.setSpacing(true);

        Scroller scroller = new Scroller(navWrapper);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        return scroller;
    }
}
