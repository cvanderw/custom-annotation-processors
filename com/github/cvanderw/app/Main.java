package com.github.cvanderw.app;

public class Main {
    public static void main(String[] args) {
        Person person = Person.of("Bob", 1967);
        System.out.printf("%s was born in %d%n", person.getName(), person.getBirthYear());
    }
}
