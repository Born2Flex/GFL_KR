package org.task.souvenirs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.Producer;
import org.task.producer.ProducerService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.task.ApplicationCLI.chooseOption;
import static org.task.producer.ProducerCLI.getProducer;

//TODO Implement methods
public class SouvenirCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(SouvenirCLI.class);
    private static SouvenirCLI souvenirCLI;
    private final SouvenirService souvenirService;
    private final ProducerService producerService;

    private SouvenirCLI(SouvenirService souvenirService, ProducerService producerService) {
        this.souvenirService = souvenirService;
        this.producerService = producerService;
    }

    public static SouvenirCLI getInstance() {
        if (souvenirCLI == null) {
            souvenirCLI = new SouvenirCLI(SouvenirService.getInstance(), ProducerService.getInstance());
        }
        return souvenirCLI;
    }

    public void process(Scanner scanner) {
        boolean exit = false;
        do {
            LOGGER.info("Choose option:");
            LOGGER.info("1. Add souvenir");
            LOGGER.info("2. Edit souvenir");
            LOGGER.info("3. Remove souvenir");
            LOGGER.info("4. Exit");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    addSouvenir(scanner);
                    break;
                case 2:
                    editSouvenir(scanner);
                    break;
                case 3:
                    removeSouvenir(scanner);
                    break;
                default:
                    exit = true;
            }
        } while (!exit);
    }

    private void addSouvenir(Scanner scanner) {
        Optional<Producer> producer = getProducer(producerService, scanner);
        if (producer.isEmpty()) {
            return;
        }
        LOGGER.info("Choose option:");
        LOGGER.info("1. Create new souvenir");
        LOGGER.info("2. Choose existing souvenir");
        int option = Integer.parseInt(scanner.nextLine());
        if (option == 1) {
            createNewSouvenir(producer.get().getId(), scanner);
        } else if (option == 2) {
            chooseFromExistingSouvenirs(producer.get().getId(), scanner);
        } else {
            LOGGER.info("Exit");
        }
    }

    private void createNewSouvenir(int producerId, Scanner scanner) {
        LOGGER.info("Enter souvenir name:");
        String name = scanner.nextLine().trim();
        LOGGER.info("Enter date (yyyy-mm-dd):");
        String date = scanner.nextLine().trim();
        LOGGER.info("Enter price:");
        double price = Double.parseDouble(scanner.nextLine());
        try {
            souvenirService.addSouvenir(name, producerId, date, price);
        } catch (RuntimeException e) {
            LOGGER.info(e.getMessage());
        }

    }

    private void chooseFromExistingSouvenirs(int producerId, Scanner scanner) {
        List<String> souvenirs = souvenirService.getSouvenirTypes();
        int option = chooseOption(scanner, souvenirs);
        if (option < 1 || option > souvenirs.size()) {
            LOGGER.info("Exit");
            return;
        }
        LOGGER.info("Enter date (yyyy-mm-dd):");
        String date = scanner.nextLine().trim();
        LOGGER.info("Enter price:");
        double price = Double.parseDouble(scanner.nextLine());
        try {
            souvenirService.addSouvenir(souvenirs.get(option - 1), producerId, date, price);
        } catch (RuntimeException e) {
            LOGGER.info(e.getMessage());
        }

    }

    private void editSouvenir(Scanner scanner) {
        Optional<Producer> producer = getProducer(producerService, scanner);
        if (producer.isEmpty()) {
            return;
        }
        Optional<Souvenir> souvenir = getSouvenir(producer.get().getId(), scanner);
        if (souvenir.isEmpty()) {
            return;
        }
        LOGGER.info("Enter new souvenir name:");
        String name = scanner.nextLine().trim();
        LOGGER.info("Enter new date (yyyy-mm-dd):");
        String date = scanner.nextLine().trim();
        LOGGER.info("Enter new price:");
        double price = Double.parseDouble(scanner.nextLine());
        try {
            souvenirService.editSouvenir(producer.get().getId(), souvenir.get().getId(), name, date, price);
        } catch (RuntimeException e) {
            LOGGER.info(e.getMessage());
        }
    }

    private Optional<Souvenir> getSouvenir(int producerId, Scanner scanner) {
        List<Souvenir> souvenirs = souvenirService.getSouvenirsByProducerId(producerId);
        if (souvenirs.isEmpty()) {
            LOGGER.info("Producer don't have souvenirs");
            return Optional.empty();
        }
        int option = chooseOption(scanner, souvenirs);
        if (option < 1 || option > souvenirs.size()) {
            LOGGER.info("Exit");
            return Optional.empty();
        }
        return Optional.of(souvenirs.get(option - 1));
    }

    private void removeSouvenir(Scanner scanner) {
        Optional<Producer> producer = getProducer(producerService, scanner);
        if (producer.isEmpty()) {
            return;
        }
        Optional<Souvenir> souvenir = getSouvenir(producer.get().getId(), scanner);
        if (souvenir.isEmpty()) {
            return;
        }
        souvenirService.removeSouvenir(souvenir.get().getId());
    }
}
