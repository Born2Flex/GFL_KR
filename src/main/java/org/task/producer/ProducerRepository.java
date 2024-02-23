package org.task.producer;

import org.task.util.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerRepository extends Repository<Producer> {
    private static final Path PRODUCERS_FILE = Path.of("producers.json");
    private static ProducerRepository instance;

    private ProducerRepository() {
        loadData(Producer.class);
        if (!items.isEmpty()) {
            Producer.setNextId(new AtomicInteger(items.getLast().getId() + 1));
        }
    }

    public static ProducerRepository getInstance() {
        if (instance == null) {
            instance = new ProducerRepository();
        }
        return instance;
    }

    @Override
    protected void edit(int id, Producer producer) {
        Producer oldProducer = findProducerById(id).orElse(null);
        if (oldProducer != null) {
            oldProducer.setName(producer.getName());
            oldProducer.setCountry(producer.getCountry());
            saveData();
        }
    }

    @Override
    protected Path getFilePath() {
        return PRODUCERS_FILE;
    }

    public Optional<Producer> findProducerById(int id) {
        for (Producer x : items) {
            if (x.getId() == id) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public Optional<Producer> findProducerByName(String name) {
        for (Producer x : items) {
            if (x.getName().equalsIgnoreCase(name)) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public List<String> getCountries() {
        return items.stream().map(Producer::getCountry).distinct().toList();
    }
}