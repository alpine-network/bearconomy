package co.crystaldev.bearconomy.framework.integration;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.integration.AlpineIntegrationEngine;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * @since 1.0.4
 */
final class VaultIntegrationEngine extends AlpineIntegrationEngine implements Economy {

    private static final EconomyResponse BANK_RESPONSE = new EconomyResponse(0, 0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported with Bearconomy");

    VaultIntegrationEngine(AlpinePlugin plugin) {
        super(plugin);

        RegisteredServiceProvider<Economy> serviceProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (serviceProvider != null) {
            plugin.log(Level.WARNING, String.format("Replacing economy provider \"%s\" with Bearconomy", serviceProvider.getPlugin().getName()));
        }

        plugin.getServer().getServicesManager().register(Economy.class, this, plugin, ServicePriority.Highest);
    }

    @Override
    public boolean isEnabled() {
        return get().isEnabled();
    }

    @Override
    public String getName() {
        return "Bearconomy";
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return get().getCurrency().format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return get().getCurrency().getPluralName();
    }

    @Override
    public String currencyNameSingular() {
        return get().getCurrency().getSingularName();
    }

    @Override
    public double getBalance(String playerName) {
        return get().getBalance(getParty(playerName)).doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return get().getBalance(Party.player(player)).doubleValue();
    }

    @Override
    public boolean has(String playerName, double amount) {
        return get().hasBalance(getParty(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return get().hasBalance(Party.player(player), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return fromResponse(get().withdraw(getParty(playerName), Transaction.of(amount, "vault api withdraw")));
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return fromResponse(get().withdraw(Party.player(player), Transaction.of(amount, "vault api withdraw")));
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return fromResponse(get().deposit(getParty(playerName), Transaction.of(amount, "vault api deposit")));
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return fromResponse(get().deposit(Party.player(player), Transaction.of(amount, "vault api deposit")));
    }

    @Override
    public double getBalance(String playerName, String world) {
        // Bearconomy does not store per-world
        return this.getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        // Bearconomy does not store per-world
        return this.getBalance(player);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        // Bearconomy does not store per-world
        return this.has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        // Bearconomy does not store per-world
        return this.has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        // Bearconomy does not store per-world
        return this.withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        // Bearconomy does not store per-world
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        // Bearconomy does not store per-world
        return this.depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        // Bearconomy does not store per-world
        return this.depositPlayer(player, amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean hasAccount(String playerName) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        // Bearconomy does not require managing accounts; everyone has an account
        return true;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return BANK_RESPONSE;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return BANK_RESPONSE;
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @NotNull
    private static co.crystaldev.bearconomy.economy.Economy get() {
        return Bearconomy.get().getEconomy();
    }

    @NotNull
    private static Party getParty(@NotNull String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return player == null
                ? Party.id(UUID.nameUUIDFromBytes(("unknown_player:" + name).getBytes()))
                : Party.player(player);
    }

    private static EconomyResponse fromResponse(@NotNull Response response) {
        EconomyResponse.ResponseType type;
        switch (response.getResult()) {
            case FAILURE:
                type = EconomyResponse.ResponseType.FAILURE;
                break;
            case NOT_IMPLEMENTED:
                type = EconomyResponse.ResponseType.NOT_IMPLEMENTED;
                break;
            default:
                type = EconomyResponse.ResponseType.SUCCESS;
        }
        return new EconomyResponse(response.getTransaction().getAmount().doubleValue(), response.getNewBalanceAsDouble(), type, response.getReason());
    }
}
