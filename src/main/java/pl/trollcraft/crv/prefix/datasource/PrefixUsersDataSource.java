package pl.trollcraft.crv.prefix.datasource;

import pl.trollcraft.crv.datasource.DataSource;
import pl.trollcraft.crv.datasource.DatabaseProvider;
import pl.trollcraft.crv.prefix.controller.PrefixesController;
import pl.trollcraft.crv.prefix.model.PrefixUser;
import pl.trollcraft.crv.prefix.model.prefix.Prefix;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class PrefixUsersDataSource implements DataSource<PrefixUser, UUID> {

    private static final Logger LOG
            = Logger.getLogger(PrefixesDataSource.class.getSimpleName());

    private final DatabaseProvider databaseProvider;
    private final PrefixesController prefixesController;

    public PrefixUsersDataSource(DatabaseProvider databaseProvider,
                                 PrefixesController prefixesController) {

        this.databaseProvider = databaseProvider;
        this.prefixesController = prefixesController;
    }

    @Override
    public void prepare() {

        String sqlUsers = "CREATE TABLE IF NOT EXISTS prefix_users (" +
                "id varchar(36)," +
                "selectedPrefix INT," +
                "PRIMARY KEY (id)" +
                ")";

        String sqlPrefixes = "CREATE TABLE IF NOT EXISTS prefix_users_prefixes (" +
                "user VARCHAR(36)," +
                "prefix INT," +
                "FOREIGN KEY (user) REFERENCES prefix_users (id) ON DELETE RESTRICT ON UPDATE CASCADE," +
                "FOREIGN KEY (prefix) REFERENCES prefixes (id) ON DELETE RESTRICT ON UPDATE CASCADE," +
                "PRIMARY KEY (user, prefix)" +
                ")";

        try (Connection c = databaseProvider.conn()) {

            Statement statement = c.createStatement();
            statement.addBatch(sqlUsers);
            statement.addBatch(sqlPrefixes);
            statement.executeBatch();

        } catch (SQLException t) {
            t.printStackTrace();
        }


    }

    @Override
    public void insert(PrefixUser prefixUser) {

        String sql = "INSERT INTO prefix_users (id, selectedPrefix) VALUES (?,?)";

        try (Connection c = databaseProvider.conn()) {

            UUID id = prefixUser.getId();
            int selectedPrefix = prefixUser.getSelectedPrefix() == null ? -1 : prefixUser.getSelectedPrefix().getId();

            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, id.toString());
            st.setInt(2, selectedPrefix);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public void update(PrefixUser prefixUser) {

        UUID id = prefixUser.getId();
        int selectedPrefix = prefixUser.getSelectedPrefix() == null ? -1 : prefixUser.getSelectedPrefix().getId();

        String sql = String.format("UPDATE prefix_users SET selectedPrefix=%d WHERE id='%s'",
                selectedPrefix, id);

        try (Connection c = databaseProvider.conn()){

            PreparedStatement statement = c.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }


    }

    public void addPrefix(PrefixUser user, Prefix prefix) {

        String sql = "INSERT INTO prefix_users_prefixes VALUES (?,?)";

        try (Connection c = databaseProvider.conn()) {

            UUID userId = user.getId();
            int prefixId = prefix.getId();

            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, userId.toString());
            st.setInt(2, prefixId);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    public void delete(Prefix prefix) {

        int id = prefix.getId();
        String sql = "DELETE FROM prefix_users_prefixes WHERE prefix=?";

        try (Connection c = databaseProvider.conn()) {

            PreparedStatement st = c.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public void delete(PrefixUser prefixUser) {
        //TODO implement.
    }

    @Override
    public void updateAll(Collection<PrefixUser> prefixUsers) {

        try (Connection c = databaseProvider.conn()) {

            Statement st = c.createStatement();

            for (PrefixUser user : prefixUsers) {

                LOG.info(user.getId() + " saving...");
                LOG.info( (user.getSelectedPrefix() == null ? -1 : user.getSelectedPrefix().getId() )+ "");

                st.addBatch(String.format("UPDATE prefix_users SET selectedPrefix='%d' WHERE id='%s'",
                        user.getSelectedPrefix() == null ? -1 : user.getSelectedPrefix().getId(), user.getId()));
            }

            st.executeBatch();

        } catch (SQLException t) {
            t.printStackTrace();
        }

    }

    @Override
    public Optional<PrefixUser> get(UUID uuid) {

        String sql = "SELECT selectedPrefix,prefix FROM prefix_users_prefixes " +
                "INNER JOIN prefix_users ON user=prefix_users.id " +
                "INNER JOIN prefixes ON prefix=prefixes.id " +
                "WHERE prefix_users.id=?";

        try (Connection c = databaseProvider.conn()) {

            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, uuid.toString());
            ResultSet res = st.executeQuery();

            PrefixUser user = new PrefixUser(uuid, new ArrayList<>(), null);

            if (!res.next()) {

                LOG.info("No prefix...");

                sql = "SELECT selectedPrefix FROM prefix_users WHERE id=?";
                st = c.prepareStatement(sql);
                st.setString(1, uuid.toString());

                res = st.executeQuery();

                if (res.next()) {

                    int sel = res.getInt("selectedPrefix");

                    Prefix selected;
                    if (sel == -1)
                       selected = null;
                    else
                        selected = prefixesController.find(sel).orElseThrow( () -> new IllegalStateException("Unknown prefix ID") );

                    user.setSelectedPrefix(selected);

                    return Optional.of(user);

                }
                else
                    return Optional.empty();

            }

            int selectedId = res.getInt("selectedPrefix");
            int prefixId = res.getInt("prefix");

            LOG.info("Loading first prefix...");

            user.getPrefixes().add(prefixesController.find(prefixId)
                    .orElseThrow( () -> new IllegalStateException("Unknown prefix ID.") ));

            if (selectedId != -1)
                user.setSelectedPrefix(prefixesController.find(prefixId)
                        .orElseThrow( () -> new IllegalStateException("Unknown prefix ID.") ));

            LOG.info("Selected: " + selectedId);

            while (res.next()) {

                LOG.info("Loading prefix...");

                user.getPrefixes().add(prefixesController.find(res.getInt("prefix"))
                        .orElseThrow( () -> new IllegalStateException("Unknown prefix ID.") ));

            }

            return Optional.of(user);

        } catch (SQLException t) {
            t.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Collection<PrefixUser> getAll() {
        return null;
    }
}
