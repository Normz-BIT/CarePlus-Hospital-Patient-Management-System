package com.careplus.common.client.view;

import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JInternalFrame;

import com.careplus.common.enums.UserRole;

/**
 * Base Dashboard class
 *
 * Describes one menu item on the dashboard without knowing anything about the
 * screen behind it.  MDI shell live in careplus_common while
 * all the actual screens live in careplus_client
 * The shell just gets handed a list of these and never imports a single patient or employee view.
 *
 * So adding a new feature is just registering one of these, and ClientApp is the
 * only place that does it.
 */
public class DashboardFeature {

    /** Top-level menu this feature sits under, e.g. "Employee" or "Patient". */
    private final String menu;

    /** Menu item text, e.g. "Diagnosis". */
    private final String label;

    /** Roles allowed to see this feature. */
    private final Set<UserRole> roles;

    /**
     * Builds the view (and hooks up its controller) when someone actually opens
     * the menu item.
     *
     * A Supplier rather than a ready-made frame, so we're not building all twelve
     * screens the moment someone logs in. Each one makes a blocking server call in
     * its constructor, so building them all up front would mean sitting through
     * twelve round trips before the dashboard even appears.
     */
    private final Supplier<JInternalFrame> viewFactory;

    public DashboardFeature(String menu, String label, Set<UserRole> roles,
            Supplier<JInternalFrame> viewFactory) {
        this.menu = menu;
        this.label = label;
        this.roles = roles;
        this.viewFactory = viewFactory;
    }

    public String getMenu() {
        return menu;
    }

    public String getLabel() {
        return label;
    }

    /*
     * A null role hides the feature rather than showing it
     *
     * this only controls what's visible. The server doesn't check
     * anyone's role before acting on a Request as this only hides a menu item
     */
    public boolean visibleFor(UserRole role) {
        return role != null && roles.contains(role);
    }

    public JInternalFrame createView() {
        return viewFactory.get();
    }
}
