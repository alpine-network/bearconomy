package co.crystaldev.bearconomy.economy.currency;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @since 0.1.0
 */
public final class DefaultCurrency implements Currency {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    @Override
    public @NotNull String getId() {
        return "default";
    }

    @Override
    public @NotNull String getSingularName() {
        return "dollar";
    }

    @Override
    public @NotNull String getPluralName() {
        return "dollars";
    }

    @Override
    public @NotNull String getSymbol() {
        return "$";
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount) {
        return this.getSymbol() + this.decimalFormat.format(amount);
    }

    @Override
    public @NotNull String formatName(@NotNull BigDecimal amount) {
        String name = BigDecimal.ONE.equals(amount) ? this.getSingularName() : this.getPluralName();
        return this.decimalFormat.format(amount) + " " + name;
    }
}
