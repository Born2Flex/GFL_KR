package org.task.souvenir;

import org.task.util.BaseModel;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Souvenir implements BaseModel {
    private static AtomicInteger nextId = new AtomicInteger(1);
    private int id;
    private String name;
    private int producerId;
    private LocalDate creationDate;
    private double price;

    public Souvenir() {
    }

    public Souvenir(String name, int producerId, LocalDate creationDate, double price) {
        this.id = nextId.getAndIncrement();
        this.name = name;
        this.producerId = producerId;
        this.creationDate = creationDate;
        this.price = price;
    }

    public Souvenir(int id, String name, int producerId, LocalDate creationDate, double price) {
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

    public static class Builder {
        private boolean idSet;
        private int id;
        private String name;
        private int producerId;
        private LocalDate creationDate;
        private double price;

        public Builder setId(int id) {
            this.id = id;
            idSet = true;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setProducerId(int producerId) {
            this.producerId = producerId;
            return this;
        }

        public Builder setCreationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Souvenir build() {
            if (idSet)
                return new Souvenir(id, name, producerId, creationDate, price);
            else
                return new Souvenir(name, producerId, creationDate, price);
        }
    }
}
