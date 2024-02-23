package org.task.souvenir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task.producer.Producer;
import org.task.producer.ProducerRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public void addSouvenir(String name, int producerId, String creationDate, double price) {
        validateName(name, producerId);
        LocalDate date = getDate(creationDate);
        souvenirRepository.add(new Souvenir.SouvenirBuilder().setName(name)
                .setProducerId(producerId)
                .setCreationDate(date)
                .setPrice(price)
                .build());
        LOGGER.debug("Souvenir {} added", name);
    }

    public void editSouvenir(int producerId, int souvenirId, String name, String creationDate, double price) {
        validateName(name, producerId, souvenirId);
        LocalDate date = getDate(creationDate);
        souvenirRepository.edit(souvenirId, new Souvenir.SouvenirBuilder().setName(name)
                .setProducerId(producerId)
                .setCreationDate(date)
                .setPrice(price)
                .build());
        LOGGER.debug("Souvenir {} edited", name);
    }

    private LocalDate getDate(String creationDate) {
        LocalDate date;
        try {
            date = LocalDate.parse(creationDate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }
        return date;
    }

    private void validateName(String name, int producerId, int... souvenirId) {
        if (producerHaveSouvenir(name, producerId, souvenirId)) {
            throw new RuntimeException("Producer already have a souvenir with name " + name);
        }
    }

    private boolean producerHaveSouvenir(String name, int producerId, int... souvenirId) {
        List<Souvenir> souvenirs = getSouvenirsByProducerId(producerId);
        return souvenirs.stream().anyMatch(souvenir -> souvenir.getName().equalsIgnoreCase(name)
                        && (souvenirId.length == 0 || souvenir.getId() != souvenirId[0]));
    }

    public void removeSouvenir(int id) {
        souvenirRepository.remove(id);
        LOGGER.debug("Souvenir with id {} removed", id);
    }

    public List<Souvenir> getSouvenirsByProducerId(int id) {
        return souvenirRepository.findSouvenirsByProducerId(id);
    }

    public List<Souvenir> getSouvenirsByCountry(String country) {
        return producerRepository.getAll().stream()
                .filter(producer -> producer.getCountry().equals(country))
                .flatMap(producer -> getSouvenirsByProducerId(producer.getId()).stream())
                .toList();
    }

    public List<Producer> getProducersWhichPricesLessThen(double priceLimit) {
        // Map<ProducerId, List<Prices>>
        Map<Integer, List<Double>> map = souvenirRepository.getAll().stream()
                .collect(Collectors.groupingBy(Souvenir::getProducerId,
                        Collectors.mapping(Souvenir::getPrice, Collectors.toList())));
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().stream().allMatch(p -> p < priceLimit))
                .map(entry -> producerRepository.findProducerById(entry.getKey()).get())
                .toList();
    }

    public List<Producer> getProducersOfSouvenirByYear(String souvenirName, int year) {
        return souvenirRepository.getAll().stream()
                .filter(souvenir -> souvenir.getName().equalsIgnoreCase(souvenirName)
                        && souvenir.getCreationDate().getYear() == year)
                .map(souvenir -> producerRepository.findProducerById(souvenir.getProducerId()).get())
                .toList();
    }

    public Map<Integer, List<Souvenir>> getSouvenirsByYears() {
        return souvenirRepository.getAll().stream()
                .collect(Collectors.groupingBy(souvenir -> souvenir.getCreationDate().getYear(),
                        TreeMap::new, Collectors.toList()));
    }

    public List<Souvenir> getAllSouvenirs() {
        return souvenirRepository.getAll();
    }

    public List<String> getSouvenirTypes() {
        return souvenirRepository.getSouvenirTypes();
    }

    public List<Souvenir> getProducersOfSouvenir(int producerId) {
        return souvenirRepository.findSouvenirsByProducerId(producerId);
    }
}
