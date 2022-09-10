package at.mrpuffi.sellsystem;

import at.mrpuffi.sellsystem.commands.SellCommand;
import at.mrpuffi.sellsystem.data.SellableItem;
import de.waischbrot.invlib.basic.RubyInventoryAPI;
import de.waischbrot.messages.MessageUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

public final class Main extends JavaPlugin {

    private Economy economy;
    public final String prefix = "&b&lKYPE &8» &7";

    private final Collection<SellableItem> sellableWoods = new ArrayList<>();
    private final Collection<SellableItem> sellableStones = new ArrayList<>();
    private final Collection<SellableItem> sellableOthers = new ArrayList<>();


    @Override
    public void onEnable() {

        PluginCommand sellCommand = getCommand("sell");
        if (sellCommand == null) {
            Bukkit.getConsoleSender().sendMessage("Commands konnten nicht richtig registriert werden!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        sellCommand.setExecutor(new SellCommand(this));

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getConsoleSender().sendMessage("Es ist kein Economy Plugin installiert!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        economy = rsp.getProvider();

        new RubyInventoryAPI(this).initialise();

        initCategories();
    }

    private void initCategories() {

        //Hier items in Collections adden
        sellableWoods.add(SellableItem.of(Material.OAK_PLANKS,
                MessageUtil.getMessageColour("&8» &7Eichenholz"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.SPRUCE_PLANKS,
                MessageUtil.getMessageColour("&8» &7Fichtenholz"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.DARK_OAK_PLANKS,
                MessageUtil.getMessageColour("&8» &7Schwarzeichenholz"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.BIRCH_PLANKS,
                MessageUtil.getMessageColour("&8» &7Birkenholz"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.JUNGLE_PLANKS,
                MessageUtil.getMessageColour("&8» &7Dschungelholz"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.ACACIA_PLANKS,
                MessageUtil.getMessageColour("&8» &7Akazienholz"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.CRIMSON_PLANKS,
                MessageUtil.getMessageColour("&8» &7Karmesenstiel"),
                0.1));
        sellableWoods.add(SellableItem.of(Material.WARPED_PLANKS,
                MessageUtil.getMessageColour("&8» &7Wirrstiel"),
                0.1));

        sellableStones.add(SellableItem.of(Material.STONE,
                MessageUtil.getMessageColour("&8» &7Stein"),
                0.1));

        sellableStones.add(SellableItem.of(Material.COBBLESTONE,
                MessageUtil.getMessageColour("&8» &7Bruchstein"),
                0.1));

        sellableStones.add(SellableItem.of(Material.GRANITE,
                MessageUtil.getMessageColour("&8» &7Granite"),
                0.1));

        sellableStones.add(SellableItem.of(Material.DIORITE,
                MessageUtil.getMessageColour("&8» &7Diorite"),
                0.1));

        sellableStones.add(SellableItem.of(Material.ANDESITE,
                MessageUtil.getMessageColour("&8» &7Andesite"),
                0.1));

        sellableStones.add(SellableItem.of(Material.NETHER_BRICKS,
                MessageUtil.getMessageColour("&8» &7Nethersteinziegel"),
                0.1));

        sellableStones.add(SellableItem.of(Material.END_STONE,
                MessageUtil.getMessageColour("&8» &7Endstein"),
                0.1));

        sellableStones.add(SellableItem.of(Material.SAND,
                MessageUtil.getMessageColour("&8» &7Sand"),
                0.1));

        sellableStones.add(SellableItem.of(Material.SANDSTONE,
                MessageUtil.getMessageColour("&8» &7Sandstein"),
                0.1));
        sellableStones.add(SellableItem.of(Material.DIRT,
                MessageUtil.getMessageColour("&8» &7Stein"),
                0.1));

        sellableOthers.add(SellableItem.of(Material.ROTTEN_FLESH,
                MessageUtil.getMessageColour("&8» &7Verottetes"),
                0.1));
        // FF

        sellableOthers.add(SellableItem.of(Material.BONE,
                MessageUtil.getMessageColour("&8» &7Knochen"),
                0.1));

        sellableOthers.add(SellableItem.of(Material.STRING,
                MessageUtil.getMessageColour("&8» &7Faden"),
                0.1));

        sellableOthers.add(SellableItem.of(Material.SPIDER_EYE,
                MessageUtil.getMessageColour("&8» &7Spinnenauge"),
                0.1));

        sellableOthers.add(SellableItem.of(Material.SLIME_BALL,
                MessageUtil.getMessageColour("&8» &7Schleimball"),
                0.1));

        sellableOthers.add(SellableItem.of(Material.FEATHER,
                MessageUtil.getMessageColour("&8» &7Feder"),
                0.1));


    }

    public Economy getEconomy() {
        return economy;
    }

    public Collection<SellableItem> getSellableWoods() {
        return sellableWoods;
    }

    public Collection<SellableItem> getSellableStones() {
        return sellableStones;
    }

    public Collection<SellableItem> getSellableOthers() {
        return sellableOthers;
    }
}
