package org.task.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.souvenir.SouvenirService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.task.ApplicationCLI.chooseOption;

public class ProducerCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerCLI.class);
    private final ProducerService producerService;
    private final SouvenirService souvenirService;
    private static ProducerCLI producerCLI;

    private ProducerCLI(ProducerService producerService, SouvenirService souvenirService) {
        this.producerService = producerService;
        this.souvenirService = souvenirService;
    }

    public static ProducerCLI getInstance() {
        if (producerCLI == null) {
            producerCLI = new ProducerCLI(ProducerService.getInstance(), SouvenirService.getInstance());
        }
        return producerCLI;
    }

    public void process(Scanner scanner) {
        boolean exit = false;
        do {
            LOGGER.info("Choose option:");
            LOGGER.info("1. Add producer");
            LOGGER.info("2. Edit producer");
            LOGGER.info("3. Remove producer (All souvenirs of this producer will be removed)");
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
        souvenirService.getSouvenirsByProducerId(producer.get().getId())
                .forEach(souvenir -> souvenirService.removeSouvenir(souvenir.getId()));
        producerService.removeProducer(producer.get().getId());
    }

    public static Optional<Producer> getProducer(ProducerService producerService, Scanner scanner) {
        List<Producer> producers = producerService.getAllProducers();
        if (producers.isEmpty()) {
            LOGGER.info("No producers");
            return Optional.empty();
        }
        int option = chooseOption(scanner, producers);
        return Optional.of(producers.get(option - 1));
    }

    public void showAllProducers() {
        List<Producer> producers = producerService.getAllProducers();
        producers.stream()
                .sorted(Comparator.comparing(Producer::getName))
                .forEach(producer -> LOGGER.info(producer.toString()));
    }
}
