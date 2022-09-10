package de.waischbrot.invlib.basic;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class RubyInventoryAPI {

    private final JavaPlugin plugin;
    private static RubyInventoryAPI instance;
    private final HashMap<UUID, RubyInventory> players = new HashMap<>();
    private final Listener listener = new RubyInventoryListener(this);

    private boolean initialised = false;

    public RubyInventoryAPI(JavaPlugin plugin) {
        if (plugin == null) throw new IllegalArgumentException("Plugin ist NULL!");
        this.plugin = plugin;
        instance = this;

    }

    public static RubyInventoryAPI getInstance() {
        return instance;
    }

    public void initialise() {
        this.plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        this.initialised = true;
    }

    @Nonnull
    public HashMap<UUID, RubyInventory> getPlayers() {
        return players;
    }

    @Nullable
    public RubyInventory getPlayersCurrentInv(final Player player) {
        if (player == null) return null;
        if (!initialised) throw new IllegalStateException("Rufe initialise() auf!");
        return players.get(player.getUniqueId());
    }

    @Nullable
    public RubyInventory getInvFromInv(final Inventory inventory) {
        for (RubyInventory rubyInventory : players.values()) {
            if (rubyInventory.getInventory().equals(inventory)) {
                return rubyInventory;
            }
        }
        return null;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Listener getListener() {
        return listener;
    }
}
