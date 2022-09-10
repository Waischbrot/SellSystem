package de.waischbrot.invlib.basic;

import com.google.common.base.Preconditions;
import de.waischbrot.invlib.events.customevent.FakeBukkitCloseInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public abstract class RubyInventory implements InventoryHolder {

    private final String id;
    private final InventoryType inventoryType;
    public final Player player;
    private Inventory inventory;
    private String title;
    private int size;

    private final Map<Integer, Button> buttons = new HashMap<>();
    private final List<BukkitTask> taskList = new ArrayList<>();

    private boolean isClosed = false;

    public RubyInventory(@Nonnull Player player, @Nonnull String id, String title, @Nonnegative int rows) {
        this.player = player;
        this.size = 9 * rows;
        this.title = title;
        this.id = id;
        this.inventoryType = InventoryType.CHEST;
    }

    public RubyInventory(@Nonnull Player player, @Nonnull String id, String title, InventoryType inventoryType) {
        this.player = player;
        this.size = Integer.MAX_VALUE;
        this.title = title;
        this.id = id;
        this.inventoryType = inventoryType;
    }

    public boolean handleInventoryClickEvent(InventoryClickEvent event) {
        return false;
    }

    public boolean handleInventoryDragEvent(InventoryDragEvent event) {
        return false;
    }

    public void handleInventoryOpenEvent(InventoryOpenEvent event) {

    }

    public void handleInventoryCloseEvent(InventoryCloseEvent event) {
        if (event instanceof FakeBukkitCloseInv) return;
        final RubyInventory gui = RubyInventoryAPI
                .getInstance()
                .getInvFromInv(event.getPlayer()
                        .getOpenInventory()
                        .getTopInventory());

        if (gui == null) return;
        if (!gui.equals(this)) return;
        taskList.forEach(BukkitTask::cancel);
    }


    public void open() {
        final RubyInventory gui = RubyInventoryAPI.getInstance().getPlayersCurrentInv(player);
        if (gui != null) {
            Bukkit.getPluginManager().callEvent(new FakeBukkitCloseInv(player.getOpenInventory()));
        }

        RubyInventoryAPI.getInstance().getPlayers().put(player.getUniqueId(), this);

        if (this.inventoryType.equals(InventoryType.CHEST)) {
            inventory = Bukkit.createInventory(null, size, title);
        } else {
            inventory = Bukkit.createInventory(null, inventoryType, title);
        }

        player.openInventory(inventory);
    }


    //Tasks die mit dem schließen gecancelt werden!
    public void runUpdateScheduler(@Nonnegative long runDelayInTicks,
                                   @Nonnegative long periodInTicks,
                                   @Nonnull final Consumer<BukkitTask> update) {
        Preconditions.checkNotNull(RubyInventoryAPI.getInstance(), "Rufe zuerst RubyInventoryAPI#init() auf!");
        final BukkitTask[] bukkitTask = new BukkitTask[]{null};
        bukkitTask[0] = Bukkit.getScheduler().runTaskTimer(getPlugin(),
                () -> update.accept(bukkitTask[0]),
                runDelayInTicks,
                periodInTicks);
        taskList.add(bukkitTask[0]);
    }

    public void runTaskLater(@Nonnegative long runDelayInTicks,
                             @Nonnull final Consumer<BukkitTask> update) {
        Preconditions.checkNotNull(RubyInventoryAPI.getInstance(), "Rufe zuerst RubyInventoryAPI#init() auf!");
        final BukkitTask[] bukkitTask = new BukkitTask[]{null};
        bukkitTask[0] = Bukkit.getScheduler().runTaskLater(getPlugin(),
                () -> update.accept(bukkitTask[0]),
                runDelayInTicks);
        taskList.add(bukkitTask[0]);
    }



    //Ganzes GUI füllen
    public void fillGui(Button button) {
        for (int slot = 0; slot < size; slot++) {
            addButton(slot, button);
        }
    }

    public void fillGui(ItemStack item) {
        fillGui(new Button(item));
    }

    public void fillGui(Material material) {
        fillGui(new Button(material));
    }

    public void fillGui(Button button, Iterable<Integer> notPlaceSlots) {
        for (int slot = 0; slot < size; slot++) {
            if (!contains(slot, notPlaceSlots)) {
                addButton(slot, button);
            }
        }
    }

    public void fillRow(Button button, int row) {
        for (int i = 0; i < 9; i++) {
            addButton((row * 9 + i), button);
        }
    }

    public void fillRow(ItemStack item, int row) {
        fillRow(new Button(item), row);
    }

    public void fillRow(Material material, int row) {
        fillRow(new Button(material), row);
    }

    public void fillColumn(Button button, int column) {
        for (int i = 0; i < (size / 9); i++) {
            addButton((i * 9 + column), button);
        }
    }

    public void fillColumn(ItemStack item, int column) {
        fillColumn(new Button(item), column);
    }

    public void fillColumn(Material material, int column) {
        fillColumn(new Button(material), column);
    }


    public void addButton(@Nonnegative int slot, Button button) {
        if (inventory.getSize() <= slot) {
            throw new IndexOutOfBoundsException("Slot kann nicht höher als die Inventargröße sein! Inventargröße: " + inventory.getSize());
        }
        if (button == null) {
            throw new NullPointerException("Button kann nicht NULL sein!");
        }

        buttons.remove(slot);
        buttons.put(slot, button);
        inventory.setItem(slot, button.getItem());
    }

    public void addButton(Button button, Integer... slots) {
        for (int slot : slots) {
            addButton(slot, button);
        }
    }

    public void addButton(@Nonnegative int slot, ItemStack item) {
        addButton(slot, new Button(item));
    }

    public void addButton(ItemStack item) {
        addButton(inventory.firstEmpty(), new Button(item));
    }

    public void addButton(Material mat) {
        addButton(inventory.firstEmpty(), new Button(mat));
    }

    public void addButton(@Nonnegative int slot, Material mat) {
        addButton(slot, new Button(mat));
    }

    public void addButton(@Nonnegative int row, @Nonnegative int column, Button button) {
        addButton(((row * 9) + column), button);
    }

    public void addButton(SlotPosition slotPosition, Button button) {
        addButton(slotPosition.row, slotPosition.column, button);
    }






    @Override
    @Nonnull
    public Inventory getInventory() {
        return inventory;
    }

    @Nonnull
    public Plugin getPlugin() {
        return RubyInventoryAPI.getInstance().getPlugin();
    }

    @Nonnull
    public Map<Integer, Button> getItems() {
        return buttons;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void sendTitleUpdate(@Nonnull String title) {

        setTitle(Objects.requireNonNull(title, "Titel kann nicht Null sein!"));
        open();

    }

    public int getSize() {
        return this.size;
    }

    public void setSize(@Nonnegative int size) {
        this.size = size;
    }

    public void sendSizeUpdate(@Nonnegative int size) {
        setSize(size);
        open();
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public void setClosed(boolean closed) {
        this.isClosed = closed;
    }

    private boolean contains(int i, Iterable<Integer> ints) {
        for (int number : ints) {
            if (number == i) {
                return true;
            }
        }
        return false;
    }
}
