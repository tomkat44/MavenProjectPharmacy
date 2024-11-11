package com.example.app.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

    @Column(name="street", length=50)
    private String street;

    @Column(name="number", length = 10)
    private String number;

    @Column(name="city", length = 50)
    private String city;

    @Column(name="country", length=50)
    private String country = "Ελλάδα";

    @Column(name="zipcode", length = 50)
    private String zipcode;

    public Address(){

    }

    public Address(String street, String number, String city, String country, String zipcode) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
    }

    public Address(Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.city = address.getCity();
        this.zipcode = address.getZipCode();
        this.country = address.getCountry();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipcode;
    }

    public void setZipCode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object address) {
        if (this == address) return true;
        if (address == null || getClass() != address.getClass()) return false;
        Address addresses = (Address) address;
        //return street.equals(address.street) && number.equals(address.number) && city.equals(address.city) && country.equals(address.country) && zipcode.equals(address.zipcode);
        return Objects.equals(this.city, addresses.city) &&
                Objects.equals(this.country, addresses.country) &&
                Objects.equals(this.number, addresses.number) &&
                Objects.equals(this.street, addresses.street) &&
                Objects.equals(this.zipcode, addresses.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, city, country, zipcode);
    }
}
