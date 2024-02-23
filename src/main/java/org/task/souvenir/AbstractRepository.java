package org.task.souvenir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.models.BaseModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T extends BaseModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepository.class);
    private List<T> items = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void addItem(T item) {
        items.add(item);
        saveData();
    }

    protected void removeItem(int id) {
        items.removeIf(x -> x.getId() == id);
        saveData();
    }

    protected abstract void edit(int id, T item);

    public List<T> getItems() {
        return items;
    }

    protected abstract String getFileName();

    protected void saveData() {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(getFileName()))) {
            objectMapper.writeValue(writer, items);
            LOGGER.debug("Data successfully saved in file {}", getFileName());
        } catch (IOException e) {
            LOGGER.info("Error occurred while trying to save data in {}", getFileName(), e);
            throw new RuntimeException(e);
        }
    }

    protected void loadData(Class<T> typeClass) {
        if (!Files.exists(Path.of(getFileName()))) {
            LOGGER.info("Data files of {} not found", typeClass);
            return;
        }
        try (BufferedReader souvenirsReader = Files.newBufferedReader(Path.of(getFileName()))) {
            items = objectMapper.readValue(souvenirsReader, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, typeClass));
            LOGGER.debug("Data files of Souvenirs loaded successfully");
        } catch (IOException e) {
            LOGGER.info("Failed to load Souvenirs files", e);
            throw new RuntimeException(e);
        }
    }
}
