package de.hawai.bicycle_tracking.server.utility.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

    MALE("male"),
    FEMALE("female"),
    NONE("");

    private final String value;

    private Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static Gender byValue(String s) {
        for (Gender val : values()) {
            if (val.getValue().equals(s)) {
                return val;
            }
        }
        return Gender.NONE;
    }

    @JsonCreator
    public static Gender forValue(String value) {
        return byValue(value);
    }

    @JsonValue
    public String toValue() {
        return getValue();
    }
}
