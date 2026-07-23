package com.careplus.common.enums;

/**
 * Role of a system user, covering patients as well as employees.
 *
 * This is the single source of authorization in the application. Each Person
 * subclass fixes its own role in its constructor, and MainDashboard filters the
 * menu bar through DashboardFeature.visibleFor, so a role determines which
 * features a user can even see.
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