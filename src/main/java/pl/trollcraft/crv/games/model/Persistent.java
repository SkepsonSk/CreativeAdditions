package pl.trollcraft.crv.games.model;


import java.util.List;

public interface Persistent {

    void save(Playable t);
    void saveAll(List<Playable> playableList);

    List<Playable> load();

    void delete(Playable p);

}
