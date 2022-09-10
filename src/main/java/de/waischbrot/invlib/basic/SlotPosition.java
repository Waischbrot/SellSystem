package de.waischbrot.invlib.basic;

import javax.annotation.Nonnegative;

public class SlotPosition {

    public int row;
    public int column;

    public SlotPosition(@Nonnegative int row, @Nonnegative int column) {
        this.row = row;
        this.column = column;
    }

    public static SlotPosition of(int row, int column) {
        return new SlotPosition(row, column);
    }
}
