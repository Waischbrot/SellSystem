package at.mrpuffi.sellsystem.gui;

import at.mrpuffi.sellsystem.Main;
import de.waischbrot.invlib.basic.Button;
import de.waischbrot.invlib.basic.RubyInventory;
import de.waischbrot.itembuilder.ItemBuilder;
import de.waischbrot.messages.MessageUtil;
import de.waischbrot.roundutil.RoundUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends RubyInventory {

    private final Economy economy;
    private final Main plugin;

    public MainMenu(@NotNull final Player player, @NotNull final Main plugin) {
        super(player, "main", MessageUtil.getMessageColour("&b&lKYPE &8» &a&lSellSystem"), 5);
        this.plugin = plugin;
        this.economy = plugin.getEconomy();
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        ItemStack border = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(MessageUtil.getMessageColour("&8")).build();
        fillRow(border, 0);
        fillRow(border, 4);
        fillColumn(border, 0);
        fillColumn(border, 8);

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtil.getMessageColour("&8"));
        lore.add(MessageUtil.getMessageColour("&7Kontostand: &b" + RoundUtil.round(economy.getBalance(player), 2)));
        addButton(0, 4, new Button(new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(MessageUtil.getMessageColour("&8» &aProfil"))
                .setPlayerHead(player)
                .setLore(lore)
                .build()));

        lore.clear();
        lore.add(MessageUtil.getMessageColour("&8"));
        lore.add(MessageUtil.getMessageColour("&8"));
        String title = MessageUtil.getMessageColour("&8× &9Kategorie: &aHolz");
        String finalTitle1 = title;
        addButton(2, 2, new Button(new ItemBuilder(Material.OAK_LOG)
                    .setDisplayName(title)
                    .setLore(lore)
                    .build())
                .setClickEventConsumer(e -> new SellMenu(player, plugin, plugin.getSellableWoods(), finalTitle1).open()));

        lore.clear();
        lore.add(MessageUtil.getMessageColour("&8"));
        lore.add(MessageUtil.getMessageColour("&8"));
        title = MessageUtil.getMessageColour("&8× &9Kategorie: &aStein & Erze");
        String finalTitle = title;
        addButton(2, 4, new Button(new ItemBuilder(Material.STONE)
                .setDisplayName(title)
                .setLore(lore)
                .build())
                .setClickEventConsumer(e -> new SellMenu(player, plugin, plugin.getSellableStones(), finalTitle).open()));

        lore.clear();
        lore.add(MessageUtil.getMessageColour("&8"));
        lore.add(MessageUtil.getMessageColour("&8"));
        title = MessageUtil.getMessageColour("&8× &9Kategorie: &aSonstiges");
        String finalTitle2 = title;
        addButton(2, 6, new Button(new ItemBuilder(Material.ARMOR_STAND)
                .setDisplayName(title)
                .setLore(lore)
                .build())
                .setClickEventConsumer(e -> new SellMenu(player, plugin, plugin.getSellableOthers(), finalTitle2).open()));
    }
}
