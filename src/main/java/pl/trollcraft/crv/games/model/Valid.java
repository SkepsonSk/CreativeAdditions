package pl.trollcraft.crv.games.model;

public interface Valid<T extends Attraction> {

    Validation validate(T attraction);

}
