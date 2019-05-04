package com.github.cvanderw.app;

import com.github.cvanderw.annotation.Immutable;

/**
 * Class representing an address.
 */
@Immutable
public final class Address {
    private final String streetName;
    private final String city;
    private final String zip;
    private final String country;

    private Address(String streetName, String city, String zip, String country) {
        this.streetName = streetName;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    public static Address of(String streetName, String city, String zip, String country) {
        return new Address(streetName, city, zip, country);
    }
}
