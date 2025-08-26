package com.shop.hazel.domain;

import lombok.Getter;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String address;
    private String zipcode;

    protected Address() {
    }

    public Address(String address, String zipcode) {
        this.address = address;
        this.zipcode = zipcode;
    }
}