package com.careplus.common.client.view;

import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JInternalFrame;

import com.careplus.common.enums.UserRole;

/**
 * Base Dashboard class
 */
public class DashboardFeature {

    /** Top-level menu this feature sits under, e.g. "Employee" or "Patient". */
    private final String menu;

    /** Menu item text, e.g. "Diagnosis". */
    private final String label;

    /** Roles allowed to see this feature. */
    private final Set<UserRole> roles;

    /** Creates a fresh view (and wires its controller) when the item is opened. */
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

    public boolean visibleFor(UserRole role) {
        return role != null && roles.contains(role);
    }

    public JInternalFrame createView() {
        return viewFactory.get();
    }
}
