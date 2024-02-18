package org.task.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerRepository.class);
    private static final Path PRODUCERS_FILE = Path.of("producers.csv");
    private List<Producer> producers = new ArrayList<>();
    private static ProducerRepository instance;

    public static ProducerRepository getInstance() {
        if (instance == null) {
            instance = new ProducerRepository();
        }
        return instance;
    }

    private ProducerRepository() {
        loadData();
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public void addProducer(Producer producer) {
        producers.add(producer);
        saveData();
    }

    public void removeProducer(int id) {
        findProducerById(id).ifPresent(producers::remove);
        saveData();
    }

    public void editProducer(int id, Producer producer) {
        Producer oldProducer = findProducerById(id).orElse(null);
        if (oldProducer != null) {
            oldProducer.setName(producer.getName());
            oldProducer.setCountry(producer.getCountry());
            saveData();
        }
    }

    public Optional<Producer> findProducerById(int id) {
        for (Producer x : producers) {
            if (x.getId() == id) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public Optional<Producer> findProducerByName(String name) {
        for (Producer x : producers) {
            if (x.getName().equalsIgnoreCase(name)) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    private void loadData() {
        if (!Files.exists(PRODUCERS_FILE)) {
            LOGGER.info("Data file of Producers not found");
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(PRODUCERS_FILE)) {
            producers = new ArrayList<>(br.lines().map(this::mapToProducer).toList());
            Producer.setNextId(new AtomicInteger(producers.getLast().getId() + 1));
            LOGGER.debug("Data files of Producers loaded successfully");
        } catch (IOException e) {
            LOGGER.info("Error occurred while trying to load Producers data", e);
            throw new RuntimeException(e);
        }
    }

    private Producer mapToProducer(String line) {
        String[] ar = line.split(",");
        return new Producer(Integer.parseInt(ar[0]), ar[1], ar[2]);
    }

    public void saveData() {
        try (BufferedWriter bw = Files.newBufferedWriter(PRODUCERS_FILE)) {
            for (Producer producer : producers) {
                bw.write(convertToCsv(producer) + System.lineSeparator());
            }
            LOGGER.debug("Data of Producers successfully saved");
        } catch (IOException e) {
            LOGGER.info("Error occurred while trying to save Producers data", e);
            throw new RuntimeException(e);
        }
    }

    private String convertToCsv(Producer producer) {
        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add(String.valueOf(producer.getId())).add(producer.getName()).add(producer.getCountry());
        return stringJoiner.toString();
    }

    public List<String> getCountries() {
        return producers.stream().map(Producer::getCountry).distinct().toList();
    }
}