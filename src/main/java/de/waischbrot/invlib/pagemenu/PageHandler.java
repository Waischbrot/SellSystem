package de.waischbrot.invlib.pagemenu;

import de.waischbrot.invlib.basic.Button;
import de.waischbrot.invlib.basic.RubyInventory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PageHandler {

    private final RubyInventory rubyInventory;
    private final LinkedList<Integer> slots = new LinkedList<>();
    private final LinkedList<Button> buttons = new LinkedList<>();
    private int page;

    public PageHandler(final RubyInventory inventory) {

        this.rubyInventory = inventory;

    }

    public LinkedList<Integer> getSlots() {
        return slots;
    }

    public void registerPageSlots(Integer... slots) {
        registerPageSlots(Arrays.asList(slots));
    }

    public void registerPageSlots(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    public void registerPageSlotsBetween(int slot1, int slot2) {
        if (slot1 > slot2) return;
        for (; slot1 <= slot2; slot1++) {
            this.slots.add(slot1);
        }
    }

    public void unregisterAllSlots() {
        this.slots.clear();
    }

    public RubyInventory getRubyInventory() {
        return rubyInventory;
    }

    public int getCurrentPage() {
        return page;
    }

    public PageHandler nextPage() {
        if (page >= getLastPage()) return this;
        page += 1;
        return this;
    }

    public PageHandler previousPage() {
        if (page <= 0) return this;
        page -= 1;
        return this;
    }

    public PageHandler firstPage() {
        page = 0;
        return this;
    }

    public PageHandler lastPage() {
        page = getLastPage();
        return this;
    }

    public boolean isLastPage() {
        return page == getLastPage();
    }

    public boolean isFirstPage() {
        return page == 0;
    }

    public int getLastPage() {
        if (slots.size() == 0) return 0;
        return Math.max(buttons.size() / slots.size(), 0);
    }


    public void addButton(Button... buttons) {
        this.buttons.addAll(Arrays.asList(buttons));
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void update() {
        if (this.page < 0) return;

        for (int slotNumber = 0; slotNumber < slots.size(); slotNumber++) {
            int buttonNumber = slotNumber + page * slots.size();

            if (buttons.size() == buttonNumber) return;
            rubyInventory.addButton(slots.get(slotNumber), buttons.get(buttonNumber));
        }
    }
}
