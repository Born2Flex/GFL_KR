package org.task.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.Producer;
import org.task.producer.ProducerService;
import org.task.souvenir.Souvenir;
import org.task.souvenir.SouvenirService;

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
            LOGGER.info("3. Find producers whose prices less then specified value");
            LOGGER.info("4. Show all data about souvenirs and producers");
            LOGGER.info("5. Find producers of specified souvenir and year");
            LOGGER.info("6. Show souvenirs by years");
            LOGGER.info("7. Exit");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    getSouvenirsByProducer(scanner);
                    break;
                case 2:
                    getSouvenirsByCountry(scanner);
                    break;
                case 3:
                    getProducersWhichPricesLessThen(scanner);
                    break;
                case 4:
                    getAllDataAboutSouvenirs();
                    break;
                case 5:
                    getProducersOfSouvenirByYear(scanner);
                    break;
                case 6:
                    getSouvenirsByYears();
                    break;
                default:
                    exit = true;
            }
        } while (!exit);
    }

    private void getSouvenirsByProducer(Scanner scanner) {
        List<Producer> producers = producerService.getAllProducers();
        if (producers.isEmpty()) {
            LOGGER.info("No producers");
            return;
        }
        int option = chooseOption(scanner, producers);
        List<Souvenir> souvenirs = souvenirService.getSouvenirsByProducerId(producers.get(option - 1).getId());
        souvenirs.forEach(souvenir -> LOGGER.info(souvenir.toString()));
    }

    private void getSouvenirsByCountry(Scanner scanner) {
        List<String> countries = producerService.getCountries();
        if (countries.isEmpty()) {
            LOGGER.info("No countries to choose, because no producers");
            return;
        }
        int option = chooseOption(scanner, countries);
        List<Souvenir> souvenirs = souvenirService.getSouvenirsByCountry(countries.get(option - 1));
        souvenirs.forEach(souvenir -> LOGGER.info(souvenir.toString()));
    }

    private void getProducersWhichPricesLessThen(Scanner scanner) {
        LOGGER.info("Enter price:");
        double price = Double.parseDouble(scanner.nextLine());
        List<Producer> producers = souvenirService.getProducersWhichPricesLessThen(price);
        producers.forEach(producer -> LOGGER.info(producer.toString()));
    }

    private void getAllDataAboutSouvenirs() {
        List<Producer> producers = producerService.getAllProducers();
        producers.forEach(producer -> {
            LOGGER.info("Producer: {}", producer);
            LOGGER.info("\tSouvenirs:");
            List<Souvenir> souvenirs = souvenirService.getProducersOfSouvenir(producer.getId());
            souvenirs.forEach(souvenir -> LOGGER.info("\t\t{}", souvenir));
        });
    }

    private void getProducersOfSouvenirByYear(Scanner scanner) {
        LOGGER.info("Enter year:");
        int year = Integer.parseInt(scanner.nextLine());
        List<String> souvenirs = souvenirService.getSouvenirTypes();
        int option = chooseOption(scanner, souvenirs);
        List<Producer> producers = souvenirService.getProducersOfSouvenirByYear(souvenirs.get(option - 1), year);
        producers.forEach(producer -> LOGGER.info(producer.toString()));
    }

    private void getSouvenirsByYears() {
        Map<Integer, List<Souvenir>> souvenirs = souvenirService.getSouvenirsByYears();
        souvenirs.forEach((year, list) -> {
            LOGGER.info("Year: {}", year);
            list.forEach(souvenir -> LOGGER.info("\t{}", souvenir));
        });
    }
}
