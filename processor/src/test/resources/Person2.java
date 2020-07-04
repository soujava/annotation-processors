package org.soujava.example.model;


import org.soujava.example.model.Entity;

@Entity
public class Person2 {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Person2() {}

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
