package org.task.souvenirs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.Producer;
import org.task.producer.ProducerRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SouvenirService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SouvenirService.class);
    private final SouvenirRepository souvenirRepository;
    private final ProducerRepository producerRepository;
    private static SouvenirService instance;

    public static SouvenirService getInstance() {
        if (instance == null) {
            instance = new SouvenirService(SouvenirRepository.getInstance(), ProducerRepository.getInstance());
        }
        return instance;
    }

    private SouvenirService(SouvenirRepository souvenirRepository, ProducerRepository producerRepository) {
        this.souvenirRepository = souvenirRepository;
        this.producerRepository = producerRepository;
    }

    //TODO CHECK IF SOUVENIR ALREADY EXISTS IN PRODUCER
    public void addSouvenir(String name, int producerId, String creationDate, double price) {
        try {
            LocalDate date = LocalDate.parse(creationDate);
            souvenirRepository.addSouvenir(new Souvenir(name, producerId, date, price));
            LOGGER.debug("Souvenir {} added", name);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }

    public void editSouvenir(int id, String name, String creationDate, double price) {
        try {
            LocalDate date = LocalDate.parse(creationDate);
            souvenirRepository.editSouvenir(id, new Souvenir(name, 0, date, price));
            LOGGER.debug("Souvenir {} edited", name);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }

    public void removeSouvenir(int id) {
        souvenirRepository.removeSouvenir(id);
        LOGGER.debug("Souvenir with id {} removed", id);
    }

    public List<Souvenir> findSouvenirByProducerId(int id) {
        return souvenirRepository.findByProducerId(id);
    }

    public List<Souvenir> findSouvenirByCountry(String country) {
        return souvenirRepository.getSouvenirs().stream()
                .filter(souvenir -> producerRepository.findProducerById(souvenir.getProducerId()).get().getCountry().equals(country))
                .toList();
    }

    public List<Producer> findProducersWhichPricesLessThen(double priceLimit) {
        Map<Integer, List<Double>> map = souvenirRepository.getSouvenirs().stream()
                .collect(Collectors.groupingBy(Souvenir::getProducerId, Collectors.mapping(Souvenir::getPrice, Collectors.toList())));
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().stream().allMatch(p -> p < priceLimit))
                .map(entry -> producerRepository.findProducerById(entry.getKey()).get())
                .toList();
    }

    public List<Producer> getSouvenirProducersByYear(int souvenirId, int year) {
        return souvenirRepository.getSouvenirs().stream()
                .filter(souvenir -> souvenir.getId() == souvenirId && souvenir.getCreationDate().getYear() == year)
                .map(souvenir -> producerRepository.findProducerById(souvenir.getProducerId()).get())
                .toList();
    }

    public Map<Integer, List<Souvenir>> getSouvenirsByYears() {
        return souvenirRepository.getSouvenirs().stream()
                .collect(Collectors.groupingBy(souvenir -> souvenir.getCreationDate().getYear(), Collectors.toList()));
    }
}
