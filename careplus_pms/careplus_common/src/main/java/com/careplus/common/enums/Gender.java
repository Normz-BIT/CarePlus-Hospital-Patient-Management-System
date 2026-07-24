package com.careplus.common.enums;

/*
 * Patient gender, kept on Patient and saved by name with EnumType.STRING.
 *
 * The column is NOT NULL so a value is always required at registration. That's
 * why OTHER has to stay.
 */
public enum Gender {
    MALE,
    FEMALE,
    OTHER
}
