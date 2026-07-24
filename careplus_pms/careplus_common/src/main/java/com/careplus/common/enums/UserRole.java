package com.careplus.common.enums;

/**
 * What kind of user someone is, patients as well as staff.
 *
 * This is the only thing controlling permissions in the whole app. Each Person
 * subclass sets its own role in its constructor, and MainDashboard filters the
 * menus through DashboardFeature.visibleFor, so the role decides what a user can
 * even see. Note that's all it does though: the server doesn't check roles, so
 * it hides menu items rather than blocking anything.
 *
 * Saved by name with EnumType.STRING on Person.role, so reordering these is fine
 * but renaming one breaks every row already in the database.
 */
public enum UserRole {
    PATIENT,
    DOCTOR,
    NURSE,
    RECEPTIONIST
}