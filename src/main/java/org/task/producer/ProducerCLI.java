package org.task.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.task.ApplicationCLI.chooseOption;

public class ProducerCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerCLI.class);
    private final ProducerService producerService;
    private static ProducerCLI producerCLI;

    private ProducerCLI(ProducerService producerService) {
        this.producerService = producerService;
    }

    public static ProducerCLI getInstance() {
        if (producerCLI == null) {
            producerCLI = new ProducerCLI(ProducerService.getInstance());
        }
        return producerCLI;
    }

    public void process(Scanner scanner) {
        boolean exit = false;
        do {
            LOGGER.info("Choose option:");
            LOGGER.info("1. Add producer");
            LOGGER.info("2. Edit producer");
            LOGGER.info("3. Remove producer");
            LOGGER.info("4. Exit");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    addProducer(scanner);
                    break;
                case 2:
                    editProducer(scanner);
                    break;
                case 3:
                    removeProducer(scanner);
                    break;
                default:
                    exit = true;
            }
        } while (!exit);
    }

    private void addProducer(Scanner scanner) {
        LOGGER.info("Enter producer name:");
        String name = scanner.nextLine().trim();
        LOGGER.info("Enter producer country:");
        String country = scanner.nextLine().trim();
        try {
            producerService.addProducer(name, country);
        } catch (RuntimeException e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void editProducer(Scanner scanner) {
        Optional<Producer> producer = getProducer(producerService, scanner);
        if (producer.isEmpty()) {
            return;
        }
        LOGGER.info("Enter new producer name:");
        String name = scanner.nextLine().trim();
        LOGGER.info("Enter new producer country:");
        String country = scanner.nextLine().trim();
        try {
            producerService.editProducer(producer.get().getId(), name, country);
        } catch (RuntimeException e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void removeProducer(Scanner scanner) {
        Optional<Producer> producer = getProducer(producerService, scanner);
        if (producer.isEmpty()) {
            return;
        }
        producerService.removeProducer(producer.get().getId());
    }

    public static Optional<Producer> getProducer(ProducerService producerService, Scanner scanner) {
        List<Producer> producers = producerService.getProducers();
        if (producers.isEmpty()) {
            LOGGER.info("No producers");
            return Optional.empty();
        }
        int option = chooseOption(scanner, producers);
        if (option < 1 || option > producers.size()) {
            LOGGER.info("Exit");
            return Optional.empty();
        }
        return Optional.of(producers.get(option - 1));
    }
}
