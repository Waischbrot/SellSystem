package at.mrpuffi.sellsystem.data;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SellableItem {

    public final Material type;
    public final String displayName;
    public final double price;

    private SellableItem(final @Nonnull Material material,
                         final @Nonnull String displayName,
                         final double price) {
        this.type = material;
        this.displayName = displayName;
        this.price = price;

    }

    @Nullable
    public static SellableItem of(final @Nonnull Material material,
                                  final @Nonnull String displayName,
                                  final double price) {
        if (price > 0 && !displayName.isEmpty()) {
            return new SellableItem(material, displayName, price);
        } else {
            return null;
        }
    }
}
