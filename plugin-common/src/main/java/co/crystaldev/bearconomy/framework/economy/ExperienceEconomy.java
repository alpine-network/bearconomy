package co.crystaldev.bearconomy.framework.economy;

import co.crystaldev.bearconomy.economy.*;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.ExperienceHolder;
import co.crystaldev.bearconomy.party.Party;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @since 0.1.1
 */
public final class ExperienceEconomy implements Economy {

    private final EconomyConfig config = new EconomyConfig(null);

    @Override
    public @NotNull String getId() {
        return Economy.EXPERIENCE_ID;
    }

    @Override
    public @NotNull EconomyConfig getConfig() {
        return this.config;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull Currency getCurrency() {
        return ExperienceCurrency.INSTANCE;
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Party party) {
        if (party instanceof ExperienceHolder) {
            return new BigDecimal(((ExperienceHolder) party).getExperience());
        }

        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull Response deposit(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        if (!(party instanceof ExperienceHolder)) {
            return new Response(party, transaction, BigDecimal.ZERO, BigDecimal.ZERO,
                    Reasons.NOT_IMPLEMENTED, Result.NOT_IMPLEMENTED);
        }

        ExperienceHolder holder = (ExperienceHolder) party;
        BigDecimal balance = this.getBalance(party);
        BigDecimal newBalance = balance.add(transaction.getAmount());

        // Update party's experience
        holder.setExperience(newBalance.intValue());

        return new Response(party, transaction, balance, newBalance, null, Result.SUCCESS);
    }

    @Override
    public @NotNull Response withdraw(@NotNull Party party, @NotNull Transaction transaction, boolean force) {
        if (!(party instanceof ExperienceHolder)) {
            return new Response(party, transaction, BigDecimal.ZERO, BigDecimal.ZERO,
                    Reasons.NOT_IMPLEMENTED, Result.NOT_IMPLEMENTED);
        }

        ExperienceHolder holder = (ExperienceHolder) party;
        BigDecimal balance = this.getBalance(party);
        BigDecimal newBalance = balance.subtract(transaction.getAmount());

        // Ensure party has enough balance
        if (!force && newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            return new Response(party, transaction, balance, balance,
                    Reasons.INSUFFICIENT_BALANCE, Result.FAILURE);
        }

        // Update party's experience
        holder.setExperience(newBalance.intValue());

        return new Response(party, transaction, balance, newBalance, null, Result.SUCCESS);
    }

    /**
     * @since 0.1.1
     */
    private static final class ExperienceCurrency implements Currency {

        private static final ExperienceCurrency INSTANCE = new ExperienceCurrency();

        private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

        @Override
        public @NotNull String getId() {
            return Economy.EXPERIENCE_ID;
        }

        @Override
        public @NotNull String getSingularName() {
            return "levels";
        }

        @Override
        public @NotNull String getPluralName() {
            return "level";
        }

        @Override
        public @NotNull String getSymbol() {
            return "";
        }

        @Override
        public @NotNull String format(@NotNull BigDecimal amount) {
            return this.getSymbol() + DECIMAL_FORMAT.format(amount);
        }

        @Override
        public @NotNull String formatName(@NotNull BigDecimal amount) {
            String name = BigDecimal.ONE.equals(amount) ? this.getSingularName() : this.getPluralName();
            return DECIMAL_FORMAT.format(amount) + " " + name;
        }
    }
}
