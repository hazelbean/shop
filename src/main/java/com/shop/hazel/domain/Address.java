package com.shop.hazel.domain;

import lombok.Getter;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String address;
    private String detailAddr;
    private String extraAddr;
    private String zipcode;

    protected Address() {
    }

    public Address(String address, String detailAddr, String extraAddr, String zipcode) {
        this.address = address;
        this.detailAddr = detailAddr;
        this.extraAddr = extraAddr;
        this.zipcode = zipcode;
    }
}