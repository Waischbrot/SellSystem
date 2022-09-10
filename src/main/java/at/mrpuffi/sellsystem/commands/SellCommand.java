package at.mrpuffi.sellsystem.commands;

import at.mrpuffi.sellsystem.Main;
import at.mrpuffi.sellsystem.gui.MainMenu;
import at.mrpuffi.sellsystem.gui.SellMenu;
import de.waischbrot.messages.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public record SellCommand(Main plugin) implements CommandExecutor {

    public SellCommand(@Nonnull final Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof final Player player) {
            if (args.length == 0) {
                new MainMenu(player, plugin).open();
            } else if (args.length == 1) {
                switch (args[0]) {
                    case "stone" -> new SellMenu(player, plugin, plugin.getSellableStones(), MessageUtil.getMessageColour("&8× &9Kategorie: &aStein & Erze"));
                    case "wood" -> new SellMenu(player, plugin, plugin.getSellableWoods(), MessageUtil.getMessageColour("&8× &9Kategorie: &aHolz"));
                    case "others" -> new SellMenu(player, plugin, plugin.getSellableOthers(), MessageUtil.getMessageColour("&8× &9Kategorie: &aSonstiges"));
                    default -> new MainMenu(player, plugin).open();
                }
            } else {
                player.sendMessage(MessageUtil.getMessageColour("&cBenutzung: &7/sell <submenü>"));
            }
        }
        return false;
    }
}
