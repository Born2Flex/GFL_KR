package org.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.ProducerCLI;
import org.task.souvenirs.SouvenirCLI;

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
                LOGGER.info("1. Add/edit/remove producer");
                LOGGER.info("2. Add/edit/remove souvenir");
                LOGGER.info("3. Query");
                LOGGER.info("4. Exit");
                int option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1:
                        producerCLI.process(scanner);
                        break;
                    case 2:
                        souvenirCLI.process(scanner);
                        break;
                    case 3:
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
