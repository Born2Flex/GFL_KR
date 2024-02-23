package org.task.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T extends BaseModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    protected List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
        saveData();
    }

    public void remove(int id) {
        items.removeIf(x -> x.getId() == id);
        saveData();
    }

    protected abstract void edit(int id, T item);

    public List<T> getAll() {
        return items;
    }

    protected abstract Path getFilePath();

    protected void saveData() {
        try (BufferedWriter writer = Files.newBufferedWriter(getFilePath())) {
            objectMapper.writeValue(writer, items);
            LOGGER.debug("Data successfully saved in file {}", getFilePath());
        } catch (IOException e) {
            LOGGER.info("Error occurred while trying to save data in {}", getFilePath(), e);
            throw new RuntimeException(e);
        }
    }

    protected void loadData(Class<T> typeClass) {
        if (!Files.exists(getFilePath())) {
            LOGGER.info("Data files of {}s not found", typeClass.getSimpleName());
            return;
        }
        try (BufferedReader souvenirsReader = Files.newBufferedReader(getFilePath())) {
            items = objectMapper.readValue(souvenirsReader, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, typeClass));
            LOGGER.debug("Data files of Souvenirs loaded successfully");
        } catch (IOException e) {
            LOGGER.info("Failed to load Souvenirs files", e);
            throw new RuntimeException(e);
        }
    }
}
