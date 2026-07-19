package com.careplus.common.enums;

/*
 * Patient gender, recorded on Patient and stored by name via EnumType.STRING.
 *
 * Mapped as nullable = false, so a value is required at registration. OTHER is
 * what keeps that constraint satisfiable for patients the first two do not
 * describe, and so must not be removed while the column stays non nullable.
 */
public enum Gender {
    MALE,
    FEMALE,
    OTHER
}
