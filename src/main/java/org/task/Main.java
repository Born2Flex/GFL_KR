package org.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.task.souvenir.SouvenirRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        //TODO REFACTOR CODE ADD TRY CATCH
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ApplicationCLI.getInstance().run();
//        List<Souvenir> souvenirs = new ArrayList<>();
//        souvenirs.add(new Souvenir("tea", 1, LocalDate.now(), 1.0));
//        souvenirs.add(new Souvenir("cup", 2, LocalDate.now(), 54.0));
        BufferedWriter writer = Files.newBufferedWriter(Path.of("souvenirs.json"));
        SouvenirRepository souvenirRepository = SouvenirRepository.getInstance();
        objectMapper.writeValue(writer, souvenirRepository.getAll());

//        List<Souvenir> readList = objectMapper.readValue(Files.newBufferedReader(Path.of("test.json")), objectMapper.getTypeFactory().constructCollectionType(List.class, Souvenir.class));
//        System.out.println();
    }
}