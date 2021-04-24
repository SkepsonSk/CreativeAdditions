package pl.trollcraft.crv.games.controller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.trollcraft.crv.games.model.Attraction;
import pl.trollcraft.crv.games.service.GameService;
import pl.trollcraft.crv.games.model.Playable;

import java.util.*;
import java.util.stream.Collectors;

public class PlayableController {

    private final List<Playable> playableList;
    private final Map<Class<? extends Playable>, GameService> services;

    public PlayableController() {
        this.playableList = new ArrayList<>();
        services = new HashMap<>();
    }

    public void register(Class<? extends Playable> type, GameService service) {
        services.put(type, service);
    }

    public GameService getService(Class<? extends Playable> type) {
        return services.get(type);
    }

    public void register(Playable playable) {
        playableList.add(playable);
    }

    public void register(List<Playable> playableList) {
        this.playableList.addAll(playableList);
    }

    public void unregister(Playable playable) {
        playableList.remove(playable);
    }

    public boolean isPlaying(Player player) {
        return playableList.stream()
                .filter(Playable::opened)
                .filter(playable -> playable instanceof Attraction)
                .map(playable -> (Attraction) playable)
                .anyMatch( attraction -> attraction.getParticipants().contains(player));
    }

    public Optional<Playable> findRandom() {

        List<Playable> available = playableList.stream()
                .filter(Playable::opened)
                .collect(Collectors.toList());

        if (available.isEmpty())
            return Optional.empty();

        Random r = new Random();

        Playable playable = available.get(r.nextInt(available.size()));
        return Optional.of(playable);

    }

    public List<Playable> findAll() {
        return playableList;
    }

    public List<Playable> findAllOpened() {
        return playableList.stream()
                .filter(Playable::opened)
                .collect(Collectors.toList());
    }

    public Optional<Playable> findByName(String name) {
        return playableList.stream()
                .filter(playable -> playable.name().equals(name) )
                .findFirst();
    }

    public Optional<Playable> findByName(String name, Class<? extends Playable> type) {
        return playableList.stream()
                .filter(playable -> playable.name().equals(name) )
                .filter(playable -> playable.type().equals(type))
                .findFirst();
    }

    public Optional<Playable> findByPlaying(Player player){
        return playableList.stream()
                .filter( playable -> playable.participates(player))
                .findFirst();
    }

    @Deprecated
    public Optional<Attraction> findByParticipant(Player player) {
        return playableList.stream()
                .filter( playable -> playable instanceof Attraction)
                .map( playable -> (Attraction) playable)
                .filter( attraction -> attraction.getParticipants().contains(player) )
                .findFirst();
    }

    public List<Playable> findByAuthor(UUID author) {
        return playableList.stream()
                .filter(playable -> playable.author().equals(author))
                .collect(Collectors.toList());
    }

    public Optional<Playable> findByNameAndAuthor(String name, UUID author) {
        return playableList.stream()
                .filter(playable -> playable.author().equals(author))
                .filter(playable -> playable.name().equals(name))
                .findFirst();
    }

    public List<Playable> findByType(Class<? extends Playable> type) {
        return playableList.stream()
                .filter(playable -> playable.type().equals(type))
                .collect(Collectors.toList());
    }

}
