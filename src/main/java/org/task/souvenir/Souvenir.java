package org.task.souvenir;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Souvenir {
    private static AtomicInteger nextId = new AtomicInteger(1);
    private final int id;
    private String name;
    private final int producerId;
    private LocalDate creationDate;
    private double price;

    public Souvenir(String name, int producerId, LocalDate creationDate, double price) {
        this.id = nextId.getAndIncrement();
        this.name = name;
        this.producerId = producerId;
        this.creationDate = creationDate;
        this.price = price;
    }

    Souvenir(int id, String name, int producerId, LocalDate creationDate, double price) {
        this.id = id;
        this.name = name;
        this.producerId = producerId;
        this.creationDate = creationDate;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getProducerId() {
        return producerId;
    }

    public static void setNextId(AtomicInteger nextId) {
        Souvenir.nextId = nextId;
    }

    @Override
    public String toString() {
        return "Name \"" + name + '\"' +
                ", creation date = " + creationDate +
                ", price = " + price;
    }
}
