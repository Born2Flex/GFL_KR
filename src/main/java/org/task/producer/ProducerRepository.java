package org.task.producer;

import org.task.util.AbstractRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerRepository extends AbstractRepository<Producer> {
    //    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerRepository.class);
    private static final Path PRODUCERS_FILE = Path.of("producers.csv");
    //    private List<Producer> producers = new ArrayList<>();
    private static ProducerRepository instance;

    public static ProducerRepository getInstance() {
        if (instance == null) {
            instance = new ProducerRepository();
        }
        return instance;
    }

    @Override
    protected void edit(int id, Producer producer) {
        Producer oldProducer = findProducerById(id).orElse(null);
        if (oldProducer != null) {
            oldProducer.setName(producer.getName());
            oldProducer.setCountry(producer.getCountry());
            saveData();
        }
    }

    @Override
    protected Path getFilePath() {
        return PRODUCERS_FILE;
    }

    private ProducerRepository() {
        loadData(Producer.class);
        Producer.setNextId(new AtomicInteger(items.getLast().getId() + 1));
    }

    public Optional<Producer> findProducerById(int id) {
        for (Producer x : items) {
            if (x.getId() == id) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public Optional<Producer> findProducerByName(String name) {
        for (Producer x : items) {
            if (x.getName().equalsIgnoreCase(name)) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

//    private void loadData() {
//        if (!Files.exists(PRODUCERS_FILE)) {
//            LOGGER.info("Data file of Producers not found");
//            return;
//        }
//        try (BufferedReader br = Files.newBufferedReader(PRODUCERS_FILE)) {
//            items = new ArrayList<>(br.lines().map(this::mapToProducer).toList());
//            Producer.setNextId(new AtomicInteger(items.getLast().getId() + 1));
//            LOGGER.debug("Data files of Producers loaded successfully");
//        } catch (IOException e) {
//            LOGGER.info("Error occurred while trying to load Producers data", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Producer mapToProducer(String line) {
//        String[] ar = line.split(",");
//        return new Producer(Integer.parseInt(ar[0]), ar[1], ar[2]);
//    }

    public List<String> getCountries() {
        return items.stream().map(Producer::getCountry).distinct().toList();
    }
}