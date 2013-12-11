package com.massivekinetics.ow.domain.model;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/10/13
 * Time: 1:58 AM
 */
public class Prediction {
    private final String description;
    private final String city;
    private final String reference;

    public Prediction(String description, String reference, String city) {
        this.description = description;
        this.city = city;
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public String getCity(){
        return city;
    }

    public String getReference() {
        return reference;
    }
}
