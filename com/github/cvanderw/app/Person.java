package com.github.cvanderw.app;

import com.github.cvanderw.annotation.Immutable;

/**
 * Class that represents a person.
 *
 * <p>This is immutable (in other words, the contents won't change after creation). If new values
 * are needed then a separate {@code Person} instance should be created with those new values.
 */
@Immutable
public class Person {
    private final String name;
    private final int birthYear;

    private Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public static Person of(String name, int birthYear) {
        return new Person(name, birthYear);
    }

    public String getName() {
        return name;
    }

    // TODO: Consider returning "age" based on difference between current year and this birth year.
    public int getBirthYear() {
        return birthYear;
    }
}
