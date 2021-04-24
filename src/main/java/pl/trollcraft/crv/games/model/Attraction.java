package pl.trollcraft.crv.games.model;

import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Attraction implements Playable {

    private final UUID id;

    private String name;
    private final UUID author;

    private boolean open;

    private final List<Player> participants;

    private int playedBy;
    private int finishedBy;

    private double difficulty;

    public Attraction(UUID author, String name) {

        id = UUID.randomUUID();

        this.name = name;
        this.author = author;

        participants = new LinkedList<>();

        playedBy = 0;
        finishedBy = 0;
    }

    public Attraction(UUID id,
                      String name,
                      UUID author,
                      int playedBy,
                      int finishedBy,
                      boolean open) {

        this.id = id;
        this.name = name;
        this.author = author;

        participants = new LinkedList<>();

        this.playedBy = playedBy;
        this.finishedBy = finishedBy;

        estimateDifficulty();

        this.open = open;
    }

    private void estimateDifficulty() {
        if (finishedBy == 0)
            difficulty = 0;
        else
            difficulty = (double) playedBy / (double) finishedBy;
    }

    @Override
    public void join(Player player) {
        participants.add(player);
        playedBy++;
        estimateDifficulty();
    }

    @Override
    public void quit(Player player) {
        participants.remove(player);
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public UUID author() {
        return author;
    }

    @Override
    public boolean opened() {
        return open;
    }

    public void setOpened(boolean open) {
        this.open = open;
    }

    public abstract void finish(Player player);

    public List<Player> getParticipants() {
        return participants;
    }

    public int getPlayedBy() {
        return playedBy;
    }

    public int getFinishedBy() {
        return finishedBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFinished() {
        finishedBy++;
        estimateDifficulty();
    }

    public void addPlayed() {
        playedBy++;
    }

    public double getDifficulty() {
        return difficulty;
    }

    @Override
    public boolean participates(Player player) {
        return participants.contains(player);
    }
}
