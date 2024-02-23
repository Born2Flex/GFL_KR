package org.task.souvenir;

import org.task.util.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SouvenirRepository extends Repository<Souvenir> {
    private static final Path SOUVENIRS_FILE = Path.of("souvenirs.json");
    private static SouvenirRepository instance;

    private SouvenirRepository() {
        loadData(Souvenir.class);
        if (!items.isEmpty()) {
            Souvenir.setNextId(new AtomicInteger(items.getLast().getId() + 1));
        }
    }

    public static SouvenirRepository getInstance() {
        if (instance == null) {
            instance = new SouvenirRepository();
        }
        return instance;
    }

    @Override
    public void edit(int id, Souvenir souvenir) {
        Souvenir oldSouvenir = findSouvenirById(id).orElse(null);
        if (oldSouvenir != null) {
            oldSouvenir.setName(souvenir.getName());
            oldSouvenir.setCreationDate(souvenir.getCreationDate());
            oldSouvenir.setPrice(souvenir.getPrice());
            saveData();
        }
    }

    @Override
    protected Path getFilePath() {
        return SOUVENIRS_FILE;
    }

    public Optional<Souvenir> findSouvenirById(int id) {
        for (Souvenir x : items) {
            if (x.getId() == id) {
                return Optional.of(x);
            }
        }
        return Optional.empty();
    }

    public List<Souvenir> findSouvenirsByProducerId(int id) {
        return items.stream().filter(x -> x.getProducerId() == id).toList();
    }

    public List<String> getSouvenirTypes() {
        return items.stream().map(Souvenir::getName).distinct().toList();
    }
}
