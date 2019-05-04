package com.github.cvanderw.app;

/**
 * Very simple driver program to hook together and make use of the immutable value types.
 *
 * <p>The true value in the program is having the types compiled correctly with the {@code
 * Immutable} annotation.
 */
public class Main {
    public static void main(String[] args) {
        Address address = Address.of("123 Street", "Seoul", "34980", "KR");
        Person person = Person.of("Bob", 1967, address, Person.EmploymentType.FULL_TIME);
        System.out.printf("%s was born in %d%n", person.getName(), person.getBirthYear());
    }
}
