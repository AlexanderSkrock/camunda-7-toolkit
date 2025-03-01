package dev.skrock.camunda.toolkit.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.skrock.camunda.toolkit.ui.rest.RestApiView;
import dev.skrock.camunda.toolkit.ui.tools.ToolsView;

@Layout
public class RootLayout extends AppLayout {

    public RootLayout() {
        addToNavbar(new DrawerToggle());

        Scroller scroller = new Scroller(getSideNavigation());
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(scroller);

        setPrimarySection(Section.DRAWER);
    }

    protected SideNav getSideNavigation() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(new SideNavItem("REST", RestApiView.class));
        sideNav.addItem(new SideNavItem("Tools", ToolsView.class));
        return sideNav;
    }
}
