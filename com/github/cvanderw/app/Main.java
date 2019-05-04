package com.github.cvanderw.app;

public class Main {
    public static void main(String[] args) {
        Address address = Address.of("123 Street", "Seoul", "34980", "KR");
        Person person = Person.of("Bob", 1967, address, Person.EmploymentType.FULL_TIME);
        System.out.printf("%s was born in %d%n", person.getName(), person.getBirthYear());
    }
}
