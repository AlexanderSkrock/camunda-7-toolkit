package dev.skrock.camunda.toolkit.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.skrock.camunda.toolkit.config.ToolkitProperties;
import dev.skrock.camunda.toolkit.engine.ConfigurableCamundaEngineProvider;
import dev.skrock.camunda.toolkit.engine.RemoteCamundaEngine;
import dev.skrock.camunda.toolkit.ui.rest.RestApiView;
import dev.skrock.camunda.toolkit.ui.tools.ToolsView;

import java.util.List;

@Layout
public class RootLayout extends AppLayout {

    public RootLayout(ToolkitProperties toolkitProperties, ConfigurableCamundaEngineProvider engineProvider) {
        addToNavbar(getTopNavigation(engineProvider, toolkitProperties.getEngines()));

        Scroller scroller = new Scroller(getSideNavigation());
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(scroller);

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

    protected SideNav getSideNavigation() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(new SideNavItem("REST", RestApiView.class));
        sideNav.addItem(new SideNavItem("Tools", ToolsView.class));
        return sideNav;
    }
}
