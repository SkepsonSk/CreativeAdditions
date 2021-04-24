package pl.trollcraft.crv.games.command;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.crv.games.controller.EditorsController;
import pl.trollcraft.crv.games.controller.PlayableController;
import pl.trollcraft.crv.games.model.Editor;
import pl.trollcraft.crv.games.model.Persistent;
import pl.trollcraft.crv.games.model.Playable;
import pl.trollcraft.crv.games.model.parkour.Parkour;
import pl.trollcraft.crv.games.service.GameService;
import pl.trollcraft.crv.games.service.ParkourService;
import pl.trollcraft.crv.help.Help;

import java.util.Optional;
import java.util.UUID;

public class ParkourCommand implements CommandExecutor {

    private final PlayableController playableController;
    private final EditorsController editorsController;
    private final ParkourService parkourService;

    public ParkourCommand(PlayableController playableController,
                          EditorsController editorsController,
                          ParkourService parkourService) {

        this.playableController = playableController;
        this.editorsController = editorsController;
        this.parkourService = parkourService;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String s,
                             String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla administracji.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {

            Help.send(player, "&eParkour'y");
            player.sendMessage("");
            Help.send(player, "  &7Jezeli nie wiesz, jak tworzyc parkour'y, to");
            Help.send(player, "  &7zapraszamy pod komende &e/parkour pomoc ;)");
            player.sendMessage("");
            Help.send(player, "&e/" + s + " nowy <nazwa> - nowy parkour,");
            Help.send(player, "&e/" + s + " edytuj <nazwa> - edytuj parkour,");
            player.sendMessage("");

            return true;

        }

        if (args[0].equalsIgnoreCase("pomoc")) {

            Help.send(player, "&ePomoc w tworzeniu parkour'ow:");
            player.sendMessage("");
            Help.send(player, "&eI. Nowy parkour:");
            Help.send(player, "&7Aby utworzyc nowy parkour, uzyj komendy &e/parkour nowy <nazwa>\n" +
                    "&7(jesli chcesz, by w nazwie byl odstep - SPACJA, to uzyj znaku '_', np. 'Moj_parkour' da 'Moj parkour'.\n" +
                    "&7Mozesz takze uzywac kolorow.");
            Help.send(player, "");
            Help.send(player, "&eII. Edytowanie parkour'a:");
            Help.send(player, "&7Gdy utworzysz parkour (patrz: sekcja I), automatycznie rozpoczniesz edycje parkoura.\n" +
                    "&7Jezeli chcesz edytowac istniejacy parkour - uzyj komendy &e/parkour edytuj <nazwa> \n" +
                    "&7(Tutaj takze uzyj znaku '_', by wprowadzic znak SPACJI).");
            Help.send(player, "");
            Help.send(player, "&eIII. Budowa parkour'a:");
            Help.send(player, "&7Budujac parkour, wybierz 3 bloki, ktore beda: blokiem, ktory tworzy checkpoint;\n " +
                    "&7blokiem, ktory 'zabija' gracz (tzn. cofa go do checkpoint'u lub na poczatek parkour'u) oraz blok konczacy gre.\n" +
                    "&7Budujac parkour nie musisz byc w trybie edycji, mozesz najpierw zbudowac parkour sam lub z przyjaciolmi, po czym \n" +
                    "&7utworzyc nowy parkour (patrz: sekcja I) lub edytowac istniejacy parkour (patrz: sekcja II).");
            Help.send(player, "");
            Help.send(player, "&eIV. Ustawianie parkour'a");
            Help.send(player, "&7Bedac w trybie edycji (patrz: sekcja II), aby parkour dzialal, nalezy ustawic nastepujace rzeczy:\n\n" +

                    "&e- poczatek parkour'a (miejsce, gdzie gracz pojawi sie po wybraniu Twojego parkour'a).\n" +
                    "&e  Komenda: &e&l/parkour start &e(miejscem zostanie Twoja obecna pozycja).\n\n" +

                    "&e- blok 'zabijajacy' (blok, po kontakcie z ktorym gracz zostanie cofniety do checkpoint'u lub na poczatek parkoura).\n" +
                    "&e  Komenda: &e&l/parkour kill &e(musisz trzymac blok, ktorym ma byc blokiem 'zabijajacym'.\n\n" +

                    "&e- blok checkpoint'u (blok, po kontakcie z ktorym zostanie na nim ustawiony checkpoint),\n" +
                    "&e  Komenda: &e&l/parkour checkpoint &e(musisz trzymac blok, ktorym ma byc blokiem checkpoint'u.\n\n" +

                    "&e  blok konczacy gre (blok, po kontakcie z ktorym gracz zakonczy parkour jako zwyciezca)\n" +
                    "&e  Komenda: &e&l/parkour finish &e(musisz trzymac blok, ktorym ma byc blokiem konczacym gre.\n\n" +

                    "&7Po zakonczeniu edycji, uzyj komenddy &e/parkour otworz, by Twoj parkour stal sie dostepny i widoczny\n " +
                    "&7dla graczy. Jezeli Twoj parkour jest niekompletny - tzn. brakuje jakichs ustawien, zostaniesz poinformowany \n" +
                    "&7i pokierowany jak to naprawic.");
            Help.send(player, "");
            Help.send(player, "&eIV. Zamykanie i usuwanie parkour'a:");
            Help.send(player, "&7Jezeli uznasz po otwarciu parkour'a, ze cos jest z nim nie tak i nie chcesz, \n" +
                    "by grali na nim gracze - mozesz go zamknac uzywajac komendy &e/parkour zamknij, edytujac wybrany parkour.&7\n " +
                    "&7Spokojnie, mozesz go ponownie otworzyc uzywajac komendy &e/parkour otworz, edytujac wybrany parkour.\n" +
                    "&7Aby parkour usunac - uzyj komendy &e/parkour usun <nazwa> (Tutaj takze uzyj znaku '_', by wprowadzic znak SPACJI).");
            Help.send(player, "");

            return true;
        }

        if (args[0].equalsIgnoreCase("nowy")) {

            if (playableController.isPlaying(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cPodczas gry nie mozesz tworzyc."));
                return true;
            }

            int games = playableController.findByAuthor(player.getUniqueId()).size();
            if (games > 10) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cMozesz miec max. &e10 gier."));
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Uzycie: /" + s + " nowy <nazwa>"));
                return true;
            }

            StringBuilder name = new StringBuilder();
            for (int i = 1 ; i < args.length ; i++)
                name.append(args[i]).append(' ');

            UUID author = player.getUniqueId();

            Parkour parkour = new Parkour(author, name.toString().trim());
            playableController.register(parkour);

            Optional<Editor> oEditor = editorsController.find(player);
            if (oEditor.isPresent()) {
                oEditor.get().setPlayable(parkour);
                oEditor.get().setType(Parkour.class);
            }
            else {
                Editor editor = new Editor(player, parkour, Parkour.class);
                editorsController.register(editor);
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aUtworzono parkour. Mozesz teraz go edytowac.\n" +
                            "&aGdy zakonczysz edycje, uzyj komendy &e/parkour otworz."));

            return true;

        }

        else if (args[0].equalsIgnoreCase("edytuj")) {

            if (args.length < 2) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Uzycie: /" + s + " edytuj <nazwa>"));
                return true;
            }

            if (playableController.isPlaying(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cPodczas gry nie mozesz tworzyc."));
                return true;
            }

            StringBuilder name = new StringBuilder();
            for (int i = 1 ; i < args.length ; i++)
                name.append(args[i]).append(' ');

            UUID author = player.getUniqueId();

            Optional<Playable> oPlayable = playableController.findByNameAndAuthor(name.toString().trim(), author);

            if (oPlayable.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cBrak takiej Twojej gry."));
                return true;
            }

            Playable playable = oPlayable.get();
            Optional<Editor> oEditor = editorsController.find(player);

            if (oEditor.isPresent()) {
                oEditor.get().setPlayable(playable);
                oEditor.get().setType(Parkour.class);
            }
            else {
                Editor editor = new Editor(player, playable, Parkour.class);
                editorsController.register(editor);
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aEdytujesz ten parkour od teraz. Aby zakonczyc, uzyj komendy &e/" + s + " zakoncz"));

            return true;

        }

        else if (args[0].equalsIgnoreCase("zakoncz")) {

            Optional<Editor> oEditor = editorsController.find(player);

            if (oEditor.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cNie edytujesz nic obecnie."));
                return true;
            }

            Editor editor = oEditor.get();
            editorsController.unregister(editor);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aZakonczono edycje."));

            return true;

        }

        else if (args[0].equalsIgnoreCase("start")) {

            Parkour parkour = getEditedParkour(player);
            if (parkour == null)
                return true;

            parkour.setStart(player.getLocation());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aWyznaczono pozycje startowa."));

        }

        else if (args[0].equalsIgnoreCase("checkpoint")){

            Parkour parkour = getEditedParkour(player);
            if (parkour == null)
                return true;

            ItemStack held = player.getInventory().getItemInMainHand();
            Material material = held.getType();
            if (material == Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +
                        "&cNie trzymasz nic w reku."));
                return true;
            }


            parkour.setCheckpoint(material);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aUstawiono trzymany blok jako &eblok checkpoint'u."));

            return true;

        }

        else if (args[0].equalsIgnoreCase("kill")){

            Parkour parkour = getEditedParkour(player);
            if (parkour == null)
                return true;

            ItemStack held = player.getInventory().getItemInMainHand();
            Material material = held.getType();
            if (material == Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +
                        "&cNie trzymasz nic w reku."));
                return true;
            }


            parkour.setKill(material);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aUstawiono trzymany blok jako &eblok zabijajacy."));

            return true;

        }
        else if (args[0].equalsIgnoreCase("finish")){

            Parkour parkour = getEditedParkour(player);
            if (parkour == null)
                return true;

            ItemStack held = player.getInventory().getItemInMainHand();
            Material material = held.getType();
            if (material == Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +
                        "&cNie trzymasz nic w reku."));
                return true;
            }

            parkour.setFinish(material);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aUstawiono trzymany blok jako &eblok konczacy."));

            return true;

        }

        else if (args[0].equalsIgnoreCase("otworz")) {

            Parkour parkour = getEditedParkour(player);
            if (parkour == null)
                return true;

            if (parkour.opened()) {
                Help.send(player, "&7Parkour jest juz otwarty.");
                return true;
            }

            //TODO checking if parkour is valid.

            parkour.setOpened(true);
            parkourService.save(parkour);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aParkour zostal &a&lotwarty."));

            player.performCommand("parkour zakoncz");

        }

        else if (args[0].equalsIgnoreCase("zamknij")) {

            Parkour parkour = getEditedParkour(player);
            if (parkour == null)
                return true;

            if (!parkour.opened()) {
                Help.send(player, "&7Parkour jest juz zamkniety.");
                return true;
            }

            parkour.setOpened(false);
            parkourService.save(parkour);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aParkour zostal &c&lzamkniety."));

        }

        else if (args[0].equalsIgnoreCase("usun")) {

            if (args.length < 2) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7Uzycie: /" + s + " usun <nazwa>"));
                return true;
            }

            StringBuilder name = new StringBuilder();
            for (int i = 1 ; i < args.length ; i++)
                name.append(args[i]).append(' ');

            Optional<Playable> oPlayable;
            if (player.hasPermission("creative.parkour.delete"))
                oPlayable = playableController.findByName(name.toString());
            else {
                UUID author = player.getUniqueId();
                oPlayable = playableController.findByNameAndAuthor(name.toString(), author);
            }

            if (oPlayable.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cBrak parkour'a."));
                return true;
            }

            Playable playable = oPlayable.get();
            playableController.unregister(playable);
            GameService service = playableController.getService(playable.type());

            if (service instanceof Persistent)
                ((Persistent) service).delete(playable);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aParkour zostal usuniety."));

            return true;

        }

        else
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7Nieznane polecenie."));


        return true;
    }

    private Parkour getEditedParkour(Player player) {
        Optional<Editor> oEditor = editorsController.find(player);
        if (oEditor.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +
                    "&cNie edytujesz zadnego parkour'u.\n&7By edytowac, wpisz &e/parkour edytuj <nazwa>"));
            return null;
        }

        Editor editor = oEditor.get();

        if (!editor.getType().equals(Parkour.class)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +
                    "&cNie edytujesz parkour'u!"));
            return null;
        }

        return (Parkour) editor.getPlayable();
    }

}
