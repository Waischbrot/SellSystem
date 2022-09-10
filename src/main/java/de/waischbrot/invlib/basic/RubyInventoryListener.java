package de.waischbrot.invlib.basic;

import de.waischbrot.invlib.events.RubyClickEvent;
import de.waischbrot.invlib.events.RubyCloseEvent;
import de.waischbrot.invlib.events.RubyDragEvent;
import de.waischbrot.invlib.events.RubyOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public record RubyInventoryListener(RubyInventoryAPI inventoryAPI) implements Listener {

        @EventHandler
        public void onClick(final InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof final Player player)) return;

                final RubyInventory openGui = inventoryAPI.getPlayersCurrentInv(player);
                if (openGui == null) return;
                if (event(new RubyClickEvent(event, openGui))) return;

                final boolean shouldProtect = openGui.handleInventoryClickEvent(event);
                final int i = event.getRawSlot();

                if (!shouldProtect) {
                        if (event.getSlot() == i) {
                                event.setCancelled(true);
                        } else {
                                switch (event.getAction()) {
                                        case MOVE_TO_OTHER_INVENTORY, COLLECT_TO_CURSOR, UNKNOWN -> event.setCancelled(true);
                                }
                        }
                } else {
                        event.setCancelled(false);
                }

                final Button button = openGui.getItems().get(i);
                if (button == null) return;

                button.getClickEventConsumer().accept(event);
        }

        @EventHandler
        public void onDrag(final InventoryDragEvent event) {
                if (!(event.getWhoClicked() instanceof final Player player)) return;

                final RubyInventory openGui = inventoryAPI.getPlayersCurrentInv(player);
                if (openGui == null) return;
                if (event(new RubyDragEvent(event, openGui))) return;

                event.setCancelled(!openGui.handleInventoryDragEvent(event));
                for (int i : event.getRawSlots()) {
                        final Button button = openGui.getItems().get(i);
                        if (button == null) return;
                        button.getDragEventConsumer().accept(event);
                }

        }

        @EventHandler
        public void onClose(final InventoryCloseEvent event) {
                if (!(event.getPlayer() instanceof final Player player)) return;

                final RubyInventory openGui = inventoryAPI.getPlayersCurrentInv(player);
                if (openGui == null) return;
                if (!event.getInventory().equals(openGui.getInventory())) return;
                if (event(new RubyCloseEvent(event, openGui))) return;

                openGui.handleInventoryCloseEvent(event);
                openGui.setClosed(true);
                inventoryAPI.getPlayers().remove(player.getUniqueId());

        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onOpen(final InventoryOpenEvent event) {

                if (!(event.getPlayer() instanceof final Player player)) return;

                final RubyInventory openGui = inventoryAPI.getPlayersCurrentInv(player);
                if (openGui == null) return;
                if (event(new RubyOpenEvent(event, openGui))) return;
                if (event.isCancelled()) return;

                openGui.handleInventoryOpenEvent(event);
        }

        private static boolean event(Event event) {
                Bukkit.getPluginManager().callEvent(event);
                return event instanceof Cancellable && ((Cancellable) event).isCancelled();
        }
}
