package pl.trollcraft.crv.prefix.datasource;

import pl.trollcraft.crv.datasource.DataSource;
import pl.trollcraft.crv.datasource.DatabaseProvider;
import pl.trollcraft.crv.prefix.model.prefix.Prefix;
import pl.trollcraft.crv.prefix.model.prefix.PrefixType;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class PrefixesDataSource implements DataSource<Prefix, Integer> {

    private static final Logger LOG
            = Logger.getLogger(PrefixesDataSource.class.getSimpleName());

    private final DatabaseProvider databaseProvider;

    public PrefixesDataSource(DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    @Override
    public void prepare() {

        String sql = "CREATE TABLE IF NOT EXISTS prefixes (" +
                "id INT," +
                "name VARCHAR(32)," +
                "type VARCHAR(32)," +
                "price DOUBLE," +
                "PRIMARY KEY (id)" +
                ")";

        try (Connection c = databaseProvider.conn()) {

            PreparedStatement st = c.prepareStatement(sql);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public void insert(Prefix prefix) {

        String sql = "INSERT INTO prefixes VALUES (?,?,?,?)";

        try (Connection c = databaseProvider.conn()) {

            int id = prefix.getId();
            String name = prefix.getName();
            String type = prefix.getType().name();
            double price = prefix.getPrice();

            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, name);
            st.setString(3, type);
            st.setDouble(4, price);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public void update(Prefix prefix) {

        String sql = "UPDATE prefixes SET name=?,type=?,price=? WHERE id=?";

        try (Connection c = databaseProvider.conn()){

            int id = prefix.getId();
            String name = prefix.getName();
            String type = prefix.getType().name();
            double price = prefix.getPrice();

            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, name);
            st.setString(2, type);
            st.setDouble(3, price);
            st.setInt(4, id);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public void delete(Prefix prefix) {

        String sql = "DELETE FROM prefixes WHERE id=?";

        try (Connection c = databaseProvider.conn()){

            int id = prefix.getId();

            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public Optional<Prefix> get(Integer id) {

        String sql = "SELECT name,type,price FROM prefixes WHERE id=?";

        LOG.info("Looking for prefix with id " + id);

        try (Connection c = databaseProvider.conn()){

            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet res = st.executeQuery();

            if (res.next()) {

                String name = res.getString("name");
                String type = res.getString("type");
                double price = res.getDouble("price");

                LOG.info("Found. Name is " + name);

                Prefix prefix = new Prefix(id, name, PrefixType.valueOf(type), price);
                return Optional.of(prefix);

            }

        } catch (SQLException t) {
            t.printStackTrace();
        }

        return Optional.empty();

    }

    @Override
    public Collection<Prefix> getAll() {

        String sql = "SELECT * FROM prefixes";

        List<Prefix> list = new ArrayList<>();

        try (Connection c = databaseProvider.conn()){

            PreparedStatement st = c.prepareStatement(sql);
            ResultSet res = st.executeQuery();

            while (res.next()) {

                int id = res.getInt("id");
                String name = res.getString("name");
                String type = res.getString("type");
                double price = res.getDouble("price");

                Prefix prefix = new Prefix(id, name, PrefixType.valueOf(type), price);
                list.add(prefix);

            }

        } catch (SQLException t) {
            t.printStackTrace();
        }

        return list;
    }

    @Override
    public void updateAll(Collection<Prefix> prefixes) {

        try (Connection c = databaseProvider.conn()){

            Statement statement = c.createStatement();

            for (Prefix prefix : prefixes) {

                statement.addBatch(String.format("UPDATE prefixes SET name='%s',type='%s',price='%f' WHERE id='%d'",
                        prefix.getName(), prefix.getType().name(), prefix.getPrice(), prefix.getId()));

            }

            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
