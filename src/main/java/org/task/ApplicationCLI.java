package org.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.ProducerCLI;
import org.task.query.QueryCLI;
import org.task.souvenir.SouvenirCLI;

import java.util.List;
import java.util.Scanner;

public class ApplicationCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCLI.class);
    private final QueryCLI queryCLI;
    private final ProducerCLI producerCLI;
    private final SouvenirCLI souvenirCLI;
    private static ApplicationCLI applicationCLI;

    private ApplicationCLI(QueryCLI queryCLI, ProducerCLI producerCLI, SouvenirCLI souvenirCLI) {
        this.queryCLI = queryCLI;
        this.producerCLI = producerCLI;
        this.souvenirCLI = souvenirCLI;
    }

    public static ApplicationCLI getInstance() {
        if (applicationCLI == null) {
            applicationCLI = new ApplicationCLI(QueryCLI.getInstance(), ProducerCLI.getInstance(), SouvenirCLI.getInstance());
        }
        return applicationCLI;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("Application started");
        boolean exit = false;
        do {
            try {
                LOGGER.info("Choose option:");
                LOGGER.info("1. Show all producers");
                LOGGER.info("2. Show all souvenirs");
                LOGGER.info("3. Add/edit/remove producer");
                LOGGER.info("4. Add/edit/remove souvenir");
                LOGGER.info("5. Query");
                LOGGER.info("6. Exit");
                int option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1:
                        producerCLI.showAllProducers();
                        break;
                    case 2:
                        souvenirCLI.showAllSouvenirs();
                        break;
                    case 3:
                        producerCLI.process(scanner);
                        break;
                    case 4:
                        souvenirCLI.process(scanner);
                        break;
                    case 5:
                        queryCLI.processQuery(scanner);
                        break;
                    default:
                        exit = true;
                }
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        } while (!exit);
        LOGGER.info("Application stopped");
    }

    public static int chooseOption(Scanner scanner, List<?> options) {
        LOGGER.info("Choose:");
        for (int i = 0; i < options.size(); i++) {
            LOGGER.info("{}. {}", (i + 1), options.get(i));
        }
        int option = Integer.parseInt(scanner.nextLine());
        if (option < 1 || option > options.size()) {
            throw new RuntimeException("Exit");
        }
        return option;
    }
}
