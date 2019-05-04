package com.github.cvanderw.app;

import com.github.cvanderw.annotation.Immutable;

/**
 * Class that represents a person.
 *
 * <p>This is immutable (in other words, the contents won't change after creation). If new values
 * are needed then a separate {@code Person} instance should be created with those new values.
 */
@Immutable
public final class Person {
    public enum EmploymentType {
        UNEMPLOYED,
        PART_TIME,
        FULL_TIME;
    }

    private final String name;
    private final int birthYear;
    private final Address address;
    private final EmploymentType employmentType;

    private Person(String name, int birthYear, Address address, EmploymentType employmentType) {
        this.name = name;
        this.birthYear = birthYear;
        this.address = address;
        this.employmentType = employmentType;
    }

    public static Person of(String name, int birthYear, Address address,
            EmploymentType employmentType) {
        return new Person(name, birthYear, address, employmentType);
    }

    public String getName() {
        return name;
    }

    // TODO: Consider returning "age" based on difference between current year and this birth year.
    public int getBirthYear() {
        return birthYear;
    }
}
