package org.task.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerService.class);
    private final ProducerRepository producerRepository;
    private static ProducerService instance;

    public static ProducerService getInstance() {
        if (instance == null) {
            instance = new ProducerService(ProducerRepository.getInstance());
        }
        return instance;
    }

    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    public void addProducer(String name, String country) {
        producerRepository.findProducerByName(name).ifPresent(producer -> {
            throw new RuntimeException("Producer with name " + name + " already exists");
        });
        producerRepository.addProducer(new Producer(name, country));
        LOGGER.debug("Producer {} added", name);
    }

    public void editProducer(int id, String name, String country) {
        if (producerRepository.findProducerById(id).isEmpty()) {
            throw new RuntimeException("Producer with id " + id + " not found");
        }
        producerRepository.editProducer(id, new Producer(name, country));
        LOGGER.debug("Producer {} edited", name);
    }

    public void removeProducer(int id) {
        producerRepository.removeProducer(id);
        LOGGER.debug("Producer with id {} removed", id);
    }

}
