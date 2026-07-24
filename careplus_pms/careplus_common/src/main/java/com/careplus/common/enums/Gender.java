package com.careplus.common.enums;

/*
 * Patient gender, kept on Patient and saved by name with EnumType.STRING.
 *
 * The column is NOT NULL so a value is always required at registration. That's
 * why OTHER has to stay: without it there'd be no valid option for a patient the
 * first two don't describe, and they couldn't be registered at all.
 */
public enum Gender {
    MALE,
    FEMALE,
    OTHER
}
