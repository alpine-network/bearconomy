package co.crystaldev.example.economy;

import co.crystaldev.bearconomy.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class GemCurrency implements Currency {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");

    @Override
    public @NotNull String getId() {
        return "gem";
    }

    @Override
    public @NotNull String getSingularName() {
        return "Gem";
    }

    @Override
    public @NotNull String getPluralName() {
        return "Gems";
    }

    @Override
    public @NotNull String getSymbol() {
        // 💎
        return "\uD83D\uDC8E";
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount) {
        return DECIMAL_FORMAT.format(amount) + " " + this.getSymbol();
    }

    @Override
    public @NotNull String formatName(@NotNull BigDecimal amount) {
        String name = BigDecimal.ONE.equals(amount) ? this.getSingularName() : this.getPluralName();
        return DECIMAL_FORMAT.format(amount) + " " + name;
    }
}
