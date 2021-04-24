package pl.trollcraft.crv.prefix.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.trollcraft.crv.prefix.controller.PrefixesController;
import pl.trollcraft.crv.prefix.datasource.PrefixUsersDataSource;
import pl.trollcraft.crv.prefix.datasource.PrefixesDataSource;
import pl.trollcraft.crv.prefix.model.prefix.Prefix;
import pl.trollcraft.crv.prefix.model.prefix.PrefixType;

import java.util.Optional;
import java.util.Random;

public class AdminPrefixesCommand implements CommandExecutor {

    private final PrefixesController prefixesController;

    private final PrefixesDataSource prefixesDataSource;
    private final PrefixUsersDataSource prefixUsersDataSource;

    public AdminPrefixesCommand(PrefixesController prefixesController,
                                PrefixesDataSource prefixesDataSource,
                                PrefixUsersDataSource prefixUsersDataSource) {

        this.prefixesController = prefixesController;
        this.prefixesDataSource = prefixesDataSource;
        this.prefixUsersDataSource = prefixUsersDataSource;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String s,
                             String[] args) {

        if (!sender.hasPermission("prefixes.admin")) {
            sender.sendMessage("Brak uprawnien.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Uzycie: /" + s + " create/edit/delete");
            return true;
        }

        else if (args[0].equalsIgnoreCase("create")) {

            if (args.length < 3) {
                sender.sendMessage("Uzycie: /" + s + " create <nazwa> <typ> (cena)");
                return true;
            }

            String name = args[1].replaceAll("_", " ");
            PrefixType type = PrefixType.valueOf(args[2].toUpperCase());

            double price;
            if (type == PrefixType.DEFAULT)
                price = Double.parseDouble(args[3]);
            else
                price = 0;

            Prefix prefix = new Prefix(new Random().nextInt(10000), name, type, price);
            prefixesController.register(prefix);

            prefixesDataSource.insert(prefix);

            sender.sendMessage("Utworzono nowy prefix.");

        }

        else if (args[0].equalsIgnoreCase("edit")) {

            if (args.length < 5) {
                sender.sendMessage("Uzycie: /" + s + " edit <nazwa> <nowa_nazwa> <nowy-typ> <nowa-cena>");
                return true;
            }

            Optional<Prefix> oPrefix = prefixesController.find(args[1]);

            if (oPrefix.isEmpty()) {
                sender.sendMessage("Nieznany prefix.");
                return true;
            }

            Prefix prefix = oPrefix.get();

            if (!args[2].equals("-"))
                prefix.setName(args[2]);

            if (!args[3].equals("-"))
                prefix.setType(PrefixType.valueOf(args[3].toUpperCase()));

            if (!args[4].equals("-"))
                prefix.setPrice(Double.parseDouble(args[4]));

            prefixesDataSource.update(prefix);
            sender.sendMessage("Prefix zostal zmieniony.");

        }

        else if (args[0].equalsIgnoreCase("delete")) {

            if (args.length != 2 ) {
                sender.sendMessage("Uzycie: /" + s + " delete <nazwa_prefix>");
                return true;
            }

            Optional<Prefix> oPrefix = prefixesController.find(args[1]);

            if (oPrefix.isEmpty()) {
                sender.sendMessage("Nieznany prefix.");
                return true;
            }

            Prefix prefix = oPrefix.get();

            prefixUsersDataSource.delete(prefix);
            prefixesDataSource.delete(prefix);
            prefixesController.delete(prefix);

            sender.sendMessage("Usunieto prefix.");

        }

        return true;
    }
}
