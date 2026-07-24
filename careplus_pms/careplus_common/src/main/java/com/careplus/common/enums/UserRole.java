package com.careplus.common.enums;

/**
 * What kind of user someone is, patients as well as staff.
 *
 * This is the only thing controlling permissions in the whole app. Each Person
 * subclass sets its own role in its constructor, and MainDashboard filters the
 * menus through DashboardFeature.visibleFor, so the role decides what a user can
 * even see.
 *
 */
public enum UserRole {
    PATIENT,
    DOCTOR,
    NURSE,
    RECEPTIONIST
}