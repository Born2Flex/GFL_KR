package org.task.souvenirs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SouvenirRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SouvenirRepository.class);
    private static final Path SOUVENIRS_FILE = Path.of("souvenirs.csv");
    private List<Souvenir> souvenirs = new ArrayList<>();
    private static SouvenirRepository instance;

    private SouvenirRepository() {
        loadData();
    }

    public static SouvenirRepository getInstance() {
        if (instance == null) {
            instance = new SouvenirRepository();
        }
        return instance;
    }

    public List<Souvenir> getSouvenirs() {
        return souvenirs;
    }

    public void addSouvenir(Souvenir souvenir) {
        souvenirs.add(souvenir);
        saveData();
    }

    public void removeSouvenir(int id) {
        List<Souvenir> souvenirs1 = souvenirs;
        for (Souvenir x : souvenirs) {
            if (x.getId() == id) {
                souvenirs1.remove(x);
                break;
            }
        }
        saveData();
    }

    public void editSouvenir(int id, Souvenir souvenir) {
        Souvenir oldSouvenir = findSouvenirById(id).orElse(null);
        if (oldSouvenir != null) {
            oldSouvenir.setName(souvenir.getName());
            oldSouvenir.setCreationDate(souvenir.getCreationDate());
            oldSouvenir.setPrice(souvenir.getPrice());
            saveData();
        }
    }

    public Optional<Souvenir> findSouvenirById(int id) {
        for (Souvenir x : souvenirs) {
            if (x.getId() == id) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public List<Souvenir> findByProducerId(int id) {
        return souvenirs.stream().filter(x -> x.getProducerId() == id).toList();
    }

    public List<String> getSouvenirTypes() {
        return souvenirs.stream().map(Souvenir::getName).distinct().toList();
    }

    private void saveData() {
        try (BufferedWriter souvenirsWriter = Files.newBufferedWriter(SOUVENIRS_FILE)) {
            for (Souvenir souvenir : souvenirs) {
                souvenirsWriter.write(souvenirToCSV(souvenir) + System.lineSeparator());
            }
            LOGGER.debug("Data of Souvenirs successfully saved");
        } catch (IOException e) {
            LOGGER.info("Error occurred while trying to save Souvenirs data", e);
            throw new RuntimeException(e);
        }
    }

    private String souvenirToCSV(Souvenir souvenir) {
        return souvenir.getId() + "," +
                souvenir.getName() + "," +
                souvenir.getProducerId() + "," +
                souvenir.getCreationDate() + "," +
                souvenir.getPrice();
    }

    private void loadData() {
        if (!Files.exists(SOUVENIRS_FILE)) {
            LOGGER.info("Data files of Souvenirs not found");
            return;
        }
        try (BufferedReader souvenirsReader = Files.newBufferedReader(SOUVENIRS_FILE)) {
            souvenirs = new ArrayList<>(souvenirsReader.lines().map(this::mapToSouvenir).toList());
            if (!souvenirs.isEmpty()) {
                Souvenir.setNextId(new AtomicInteger(souvenirs.getLast().getId() + 1));
                LOGGER.debug("Data files of Souvenirs loaded successfully");
            }
        } catch (IOException e) {
            LOGGER.info("Failed to load Souvenirs files", e);
            throw new RuntimeException(e);
        }
    }

    private Souvenir mapToSouvenir(String line) {
        String[] ar = line.split(",");

        int id = Integer.parseInt(ar[0]);
        String name = ar[1];
        int producerId = Integer.parseInt(ar[2]);
        LocalDate date = LocalDate.parse(ar[3]);
        double price = Double.parseDouble(ar[4]);

        return new Souvenir(id, name, producerId, date, price);
    }
}
