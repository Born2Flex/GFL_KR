package org.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            ApplicationCLI.getInstance().run();
        } catch (Exception e) {
            LOGGER.error("Error occurred: {}", e.getMessage());
        }
    }
}