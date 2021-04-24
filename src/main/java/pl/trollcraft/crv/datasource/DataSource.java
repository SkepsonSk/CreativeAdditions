package pl.trollcraft.crv.datasource;

import java.util.Collection;
import java.util.Optional;

public interface DataSource<T, ID> {

    void prepare();

    void insert(T t);
    void update(T t);
    void delete(T t);

    void updateAll(Collection<T> ts);

    Optional<T> get(ID id);
    Collection<T> getAll();



}
