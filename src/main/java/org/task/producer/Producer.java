package org.task.producer;

import org.task.util.BaseModel;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements BaseModel {
    private static AtomicInteger nextId = new AtomicInteger(1);
    private final int id;
    private String name;
    private String country;

    public Producer(String name, String country) {
        this.id = nextId.getAndIncrement();
        this.name = name;
        this.country = country;
    }

    Producer(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static void setNextId(AtomicInteger nextId) {
        Producer.nextId = nextId;
    }

    @Override
    public String toString() {
        return "\"" + name + "\", " + country;
    }
}
