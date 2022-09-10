package de.waischbrot.invlib.basic;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Button {

    private final ItemStack item;
    private Consumer<InventoryClickEvent> clickEventConsumer;
    private Consumer<InventoryDragEvent> dragEventConsumer;

    public Button(final ItemStack item) {
        this.item = item;

        this.dragEventConsumer = event -> {};
        this.clickEventConsumer = event -> {};
    }

    public Button(final Material mat) {
        this(new ItemStack(mat));
    }

    public Consumer<InventoryClickEvent> getClickEventConsumer() {
        return clickEventConsumer;
    }

    public Button setClickEventConsumer(Consumer<InventoryClickEvent> clickEventConsumer) {
        this.clickEventConsumer = clickEventConsumer;
        return this;
    }

    public Consumer<InventoryDragEvent> getDragEventConsumer() {
        return dragEventConsumer;
    }

    public Button setDragEventConsumer(Consumer<InventoryDragEvent> dragEventConsumer) {
        this.dragEventConsumer = dragEventConsumer;
        return this;
    }


    public ItemStack getItem() {
        return item;
    }
}
