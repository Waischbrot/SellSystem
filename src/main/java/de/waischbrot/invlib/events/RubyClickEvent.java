package de.waischbrot.invlib.events;

import de.waischbrot.invlib.basic.RubyInventory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class RubyClickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final InventoryClickEvent event;
    private final RubyInventory rubyInventory;
    private boolean isCancelled;

    public RubyClickEvent(InventoryClickEvent event, RubyInventory rubyInventory) {
        this.event = event;
        this.rubyInventory = rubyInventory;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    public RubyInventory getRubyInventory() {
        return rubyInventory;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
