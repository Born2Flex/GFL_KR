package org.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.Producer;
import org.task.producer.ProducerService;
import org.task.souvenirs.Souvenir;
import org.task.souvenirs.SouvenirService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.task.ApplicationCLI.chooseOption;

public class QueryCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCLI.class);
    private final ProducerService producerService;
    private final SouvenirService souvenirService;
    private static QueryCLI queryCLI;

    private QueryCLI(ProducerService producerService, SouvenirService souvenirService) {
        this.producerService = producerService;
        this.souvenirService = souvenirService;
    }

    public static QueryCLI getInstance() {
        if (queryCLI == null) {
            queryCLI = new QueryCLI(ProducerService.getInstance(), SouvenirService.getInstance());
        }
        return queryCLI;
    }

    public void processQuery(Scanner scanner) {
        boolean exit = false;
        do {
            LOGGER.info("Choose option:");
            LOGGER.info("1. Find souvenirs by producer");
            LOGGER.info("2. Find souvenirs by country");
            LOGGER.info("3. Find producers which prices less then");
            LOGGER.info("4. Show all data about souvenirs and producers");
            LOGGER.info("5. Find producers of specified souvenir by year");
            LOGGER.info("6. Find souvenirs by years");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    findSouvenirsByProducer(scanner);
                    break;
                case 2:
                    findSouvenirsByCountry(scanner);
                    break;
                case 3:
                    findProducersWhichPricesLessThen(scanner);
                    break;
                case 4:
                    findAllDataAboutSouvenirs();
                    break;
                case 5:
                    findProducersOfSouvenirByYear(scanner);
                    break;
                case 6:
                    findSouvenirsByYears();
                    break;
                default:
                    exit = true;
            }
        } while (!exit);
    }

    private void findSouvenirsByProducer(Scanner scanner) {
        List<Producer> producers = producerService.getProducers();
        int option = chooseOption(scanner, producers);
        if (option < 1 || option > producers.size()) {
            LOGGER.info("Invalid option");
        }
        List<Souvenir> souvenirs = souvenirService.getSouvenirsByProducerId(producers.get(option - 1).getId());
        souvenirs.forEach(souvenir -> LOGGER.info(souvenir.toString() + System.lineSeparator()));
    }

    private void findSouvenirsByCountry(Scanner scanner) {
        List<String> producers = producerService.getCountries();
        int option = chooseOption(scanner, producers);
        if (option < 1 || option > producers.size()) {
            LOGGER.info("Invalid option");
        }
        List<Souvenir> souvenirs = souvenirService.getSouvenirsByCountry(producers.get(option - 1));
        souvenirs.forEach(souvenir -> LOGGER.info(souvenir.toString()));
    }

    private void findProducersWhichPricesLessThen(Scanner scanner) {
        LOGGER.info("Enter price:");
        double price = Double.parseDouble(scanner.nextLine());
        List<Producer> producers = souvenirService.getProducersWhichPricesLessThen(price);
        producers.forEach(producer -> LOGGER.info(producer.toString()));
    }

    private void findAllDataAboutSouvenirs() {
        List<Producer> producers = producerService.getProducers();
        producers.forEach(producer -> {
            LOGGER.info("Producer: {}", producer);
            List<Souvenir> souvenirs = souvenirService.getProducersOfSouvenir(producer.getId());
            souvenirs.forEach(souvenir -> LOGGER.info("\t" + souvenir.toString()));
        });
    }

    private void findProducersOfSouvenirByYear(Scanner scanner) {
        LOGGER.info("Enter year:");
        int year = Integer.parseInt(scanner.nextLine());
        List<String> souvenirs = souvenirService.getSouvenirTypes();
        int option = chooseOption(scanner, souvenirs);
        if (option < 1 || option > souvenirs.size()) {
            LOGGER.info("Invalid option");
        }
        List<Producer> producers = souvenirService.getProducersOfSouvenirByYear(souvenirs.get(option - 1), year);
        producers.forEach(producer -> LOGGER.info(producer.toString() + System.lineSeparator()));
    }

    private void findSouvenirsByYears() {
        Map<Integer, List<Souvenir>> souvenirs = souvenirService.getSouvenirsByYears();
        souvenirs.forEach((year, list) -> {
            LOGGER.info("Year: {}", year);
            list.forEach(souvenir -> LOGGER.info(souvenir.toString()));
        });
    }
}
