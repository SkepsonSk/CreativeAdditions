package pl.trollcraft.crv.prefix.model;

import pl.trollcraft.crv.prefix.model.prefix.Prefix;

import java.util.List;
import java.util.UUID;

public class PrefixUser {

    private final UUID id;
    private final List<Prefix> prefixes;
    private Prefix selectedPrefix;

    public PrefixUser(UUID id, List<Prefix> prefixes, Prefix selectedPrefix) {
        this.id = id;
        this.prefixes = prefixes;
        this.selectedPrefix = selectedPrefix;
    }

    public UUID getId() {
        return id;
    }

    public List<Prefix> getPrefixes() {
        return prefixes;
    }

    public Prefix getSelectedPrefix() {
        return selectedPrefix;
    }

    public void setSelectedPrefix(Prefix selectedPrefix) {
        this.selectedPrefix = selectedPrefix;
    }

    public boolean hasPrefix(Prefix prefix) {
        return prefixes.contains(prefix);
    }

}
