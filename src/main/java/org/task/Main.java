package org.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.task.souvenir.Souvenir;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //TODO REFACTOR CODE
//        ApplicationCLI.getInstance().run();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Souvenir> souvenirs = new ArrayList<>();
        souvenirs.add(new Souvenir("tea", 1, LocalDate.now(), 1.0));
        souvenirs.add(new Souvenir("cup", 2, LocalDate.now(), 54.0));
        BufferedWriter writer = Files.newBufferedWriter(Path.of("test.json"));
        objectMapper.writeValue(writer, souvenirs);

        List<Souvenir> readList = objectMapper.readValue(Files.newBufferedReader(Path.of("test.json")), objectMapper.getTypeFactory().constructCollectionType(List.class, Souvenir.class));
        System.out.println();
    }
}