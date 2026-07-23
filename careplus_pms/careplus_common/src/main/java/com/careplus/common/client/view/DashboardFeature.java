package com.careplus.common.client.view;

import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JInternalFrame;

import com.careplus.common.enums.UserRole;

/**
 * Base Dashboard class
 *
 * Describes one dashboard menu item without knowing anything about the feature
 * behind it. This is what lets careplus_common host the MDI shell while every
 * concrete view lives in careplus_client: the shell is handed a list of these and
 * never imports a single patient or employee screen.
 *
 * Registering a feature is therefore the only step needed to add one, and
 * ClientApp is the single place that does it.
 */
public class DashboardFeature {

    /** Top-level menu this feature sits under, e.g. "Employee" or "Patient". */
    private final String menu;

    /** Menu item text, e.g. "Diagnosis". */
    private final String label;

    /** Roles allowed to see this feature. */
    private final Set<UserRole> roles;

    /**
     * Creates a fresh view (and wires its controller) when the item is opened.
     *
     * A Supplier rather than a prebuilt frame, so the twelve feature screens are
     * not all constructed at login. Each one performs a blocking server call in its
     * constructor, so eager construction would stall startup on twelve round trips.
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
     * The single authorization check in the client. A null role fails closed,
     * hiding the feature rather than exposing it, which is the safe default if a
     * Person ever arrives without one.
     *
     * This governs visibility only. The server does not verify the caller's role
     * before acting on a Request, so this hides a menu item rather than preventing
     * the underlying action.
     */
    public boolean visibleFor(UserRole role) {
        return role != null && roles.contains(role);
    }

    public JInternalFrame createView() {
        return viewFactory.get();
    }
}
