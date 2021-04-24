package pl.trollcraft.crv.prefix.controller;

import pl.trollcraft.crv.prefix.model.prefix.Prefix;
import pl.trollcraft.crv.prefix.model.prefix.PrefixType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrefixesController {

    private final List<Prefix> prefixes;

    public PrefixesController() {
        prefixes = new ArrayList<>();
    }

    public void register(Prefix prefix) {
        prefixes.add(prefix);
    }

    public void register(Collection<Prefix> prefixes) {
        this.prefixes.addAll(prefixes);
    }

    public List<Prefix> getPrefixes(PrefixType type) {
        return prefixes.stream()
                .filter( prefix -> prefix.getType() == type )
                .collect(Collectors.toList());
    }

    public Optional<Prefix> find(int id) {
        return prefixes.stream()
                .filter(prefix -> prefix.getId() == id)
                .findFirst();
    }

    public Optional<Prefix> find(String name) {
        String fullName = name.replaceAll("_", " ");
        return prefixes.stream()
                .filter(prefix -> prefix.getName().equals(fullName) || prefix.getNameNoColors().equals(fullName))
                .findFirst();
    }

    public void delete(Prefix prefix) {
        prefixes.remove(prefix);
    }

}
