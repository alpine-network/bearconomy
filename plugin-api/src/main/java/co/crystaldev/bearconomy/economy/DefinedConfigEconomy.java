package co.crystaldev.bearconomy.economy;

import co.crystaldev.alpinecore.util.Formatting;
import co.crystaldev.bearconomy.economy.currency.Currency;
import de.exlll.configlib.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @since 1.1.0
 */
@Getter @AllArgsConstructor @NoArgsConstructor
@Configuration
public final class DefinedConfigEconomy {

    private static final String DECIMAL_FORMAT = "#,###.##";

    /**
     * The singular name representation of a currency (e.g., dollar).
     */
    private String singularName;

    /**
     * The plural name representation of a currency (e.g., dollars).
     */
    private String pluralName;

    /**
     * The symbol representation of a currency (e.g., "$" for dollars).
     */
    private String symbol;

    /**
     * The format specifier used to define the structured representation of a currency's value.
     */
    private String format;

    /**
     * Specifies the format name used to describe how the currency's value should be represented
     * when combined with its name (e.g., "10 dollars").
     */
    private String formatName;

    /**
     * Represents the format used for displaying decimal values within the economy system.
     */
    private String decimalFormat;

    /**
     * Holds the economy configuration details, which define the behavior and constraints
     * of the currency system, such as default balances, maximum balances, and transferability.
     */
    private EconomyConfig config;

    public DefinedConfigEconomy(
            @NotNull String singularName,
            @NotNull String pluralName,
            @NotNull String symbol,
            @NotNull EconomyConfig config
    ) {
        this(
                singularName,
                pluralName,
                symbol,
                "%symbol%%amount%",
                "%amount% %name%",
                DECIMAL_FORMAT,
                config
        );
    }

    public @NotNull Currency buildCurrency(@NotNull String id) {
        return new DefinedCurrency(id, this);
    }

    public static final class DefinedCurrency implements Currency {
        @Getter
        private final String id;
        private final DefinedConfigEconomy economy;
        private final DecimalFormat decimalFormat;

        private DefinedCurrency(@NotNull String id, @NotNull DefinedConfigEconomy economy) {
            this.id = id;
            this.economy = economy;
            this.decimalFormat = new DecimalFormat(
                    economy.decimalFormat == null ? DECIMAL_FORMAT : economy.decimalFormat);
        }

        @Override
        public @NotNull String getSingularName() {
            return this.economy.singularName;
        }

        @Override
        public @NotNull String getPluralName() {
            return this.economy.pluralName;
        }

        @Override
        public @NotNull String getSymbol() {
            return this.economy.symbol;
        }

        @Override
        public @NotNull String format(@NotNull BigDecimal amount) {
            return this.format(amount, this.economy.format);
        }

        @Override
        public @NotNull String formatName(@NotNull BigDecimal amount) {
            return this.format(amount, this.economy.formatName);
        }

        private @NotNull String format(@NotNull BigDecimal amount, @NotNull String formatString) {
            String name = amount.compareTo(BigDecimal.ONE) == 0 ? this.getSingularName() : this.getPluralName();
            return Formatting.placeholders(formatString,
                    "name", name,
                    "singular_name", this.economy.getSingularName(),
                    "plural_name", this.economy.getPluralName(),
                    "symbol", this.economy.getSymbol(),
                    "amount", this.decimalFormat.format(amount));
        }
    }
}
