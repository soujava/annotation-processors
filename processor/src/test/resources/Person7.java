package org.soujava.example.model;


import org.soujava.medatadata.api.Entity;

@Entity("table")
public class Person7 {

    private String name;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
