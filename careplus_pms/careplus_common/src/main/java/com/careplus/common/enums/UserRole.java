package com.careplus.common.enums;

/**
 * Role of a system user, covering patients as well as employees.
 *
 * This is the single source of authorization in the application. Each Person
 * subclass fixes its own role in its constructor, and MainDashboard filters the
 * menu bar through DashboardFeature.visibleFor, so a role determines which
 * features a user can even see.
 *
 * That gating is presentation only. The server does not check the caller's role
 * before acting on a Request, so hiding a menu item is not an access control
 * boundary: a modified client could still send any RequestType. Real enforcement
 * would have to happen server side in ClientHandler or the services.
 *
 * Persisted by name via EnumType.STRING on Person.role, so these constants can be
 * reordered safely but not renamed without migrating existing rows.
 */
public enum UserRole {
    PATIENT,
    DOCTOR,
    NURSE,
    RECEPTIONIST
}