package co.crystaldev.bearconomy.framework.command;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.command.AlpineCommand;
import co.crystaldev.alpinecore.framework.config.object.ConfigMessage;
import co.crystaldev.alpinecore.util.Components;
import co.crystaldev.alpinecore.util.Formatting;
import co.crystaldev.alpinecore.util.Messaging;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.Reasons;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.leaderboard.Leaderboard;
import co.crystaldev.bearconomy.framework.config.Config;
import co.crystaldev.bearconomy.framework.leaderboard.LeaderboardEntry;
import co.crystaldev.bearconomy.party.Party;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @since 0.1.0
 */
@Command(name = "bearconomy", aliases = {"eco", "economy" })
@Description("Main command for Bearconomy.")
public final class BearconomyCommand extends AlpineCommand {

    private final Leaderboard leaderboard = new Leaderboard();

    BearconomyCommand(AlpinePlugin plugin) {
        super(plugin);
    }

    @Execute(name = "deposit", aliases = "give")
    @Permission("bearconomy.admin.deposit")
    @Description("Deposit into a player's balance.")
    public void deposit(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal amount
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party party = Party.player(player);
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        Response response = economy.deposit(party, Transaction.of(amount, "admin deposit"));
        if (response.succeeded()) {
            if (isDifferentPlayer(sender, player)) {
                Messaging.send(sender, config.deposit.build(this.plugin, player, senderPlayer,
                        "player", player.getName(),
                        "amount", currency.format(amount),
                        "balance", currency.format(economy.getBalance(party))));
            }

            Messaging.attemptSend(player, config.deposited.build(this.plugin, player, senderPlayer,
                    "player", sender.getName(),
                    "amount", currency.format(amount),
                    "balance", currency.format(economy.getBalance(party))));
        }
        else if (response.failed()) {
            ConfigMessage errorMessage = isDifferentPlayer(sender, player) ? config.oversizeBalanceOther : config.oversizeBalanceSelf;
            Object reason = Reasons.OVERSIZE_BALANCE.equals(response.getReason()) && economy.getConfig().hasMaxBalance()
                    ? errorMessage.build(this.plugin, player, senderPlayer,
                        "player", player.getName(),
                        "limit", currency.format(economy.getConfig().getMaxBalance()))
                    : response.getReason();
            Messaging.send(sender, config.error.build(this.plugin,  player, senderPlayer, "response", reason));
        }
    }

    @Execute(name = "withdraw", aliases = "take")
    @Permission("bearconomy.admin.withdraw")
    @Description("Withdraw from a player's balance.")
    public void withdraw(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal amount
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party party = Party.player(player);
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        Response response = economy.withdraw(party, Transaction.of(amount, "admin withdraw"));
        if (response.succeeded()) {
            if (isDifferentPlayer(sender, player)) {
                Messaging.send(sender, config.withdraw.build(this.plugin, player, senderPlayer,
                        "player", player.getName(),
                        "amount", currency.format(amount),
                        "balance", currency.format(economy.getBalance(party))));
            }

            Messaging.attemptSend(player, config.withdrew.build(this.plugin, player, senderPlayer,
                    "player", sender.getName(),
                    "amount", currency.format(amount),
                    "balance", currency.format(economy.getBalance(party))));
        }
        else if (response.failed()) {
            ConfigMessage errorMessage = isDifferentPlayer(sender, player) ? config.insufficientBalanceOther : config.insufficientBalanceSelf;
            Object reason = Reasons.INSUFFICIENT_BALANCE.equals(response.getReason())
                    ? errorMessage.build(this.plugin,  player, senderPlayer,
                        "player", player.getName())
                    : response.getReason();
            Messaging.send(sender, config.error.build(this.plugin, player, senderPlayer, "response", reason));
        }
    }

    @Execute(name = "set")
    @Permission("bearconomy.admin.set")
    @Description("Set a player's balance.")
    public void set(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal balance
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party party = Party.player(player);
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        Response response = economy.set(party, balance);
        if (response.succeeded()) {
            String formattedBalance = currency.format(balance);

            if (isDifferentPlayer(sender, player)) {
                Messaging.send(sender, config.balanceUpdatedOther.build(this.plugin, player, senderPlayer,
                        "player", player.getName(),
                        "amount", formattedBalance));
            }

            Messaging.attemptSend(player, config.balanceUpdatedSelf.build(this.plugin, player, senderPlayer,
                    "player", sender.getName(),
                    "amount", formattedBalance));
        }
        else if (response.failed()) {
            Messaging.send(sender, config.error.build(this.plugin, player, senderPlayer,
                    "response", response.getReason()));
        }
    }

    @Execute(name = "reset")
    @Permission("bearconomy.admin.reset")
    @Description("Reset a player's balance to default.")
    public void reset(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player
    ) {
        Config config = Config.getInstance();

        set(sender, player, config.defaultBalance);
    }

    @Execute(name = "bal")
    @Shortcut({ "balance", "bal" })
    @Permission("bearconomy.command.balance")
    @Description("Display a player's balance.")
    public void show(
            @Context CommandSender sender,
            @OptionalArg("player") @Key("offlinePlayer") OfflinePlayer player
    ) {
        if (player == null) {
            player = sender instanceof Player ? (OfflinePlayer) sender : null;
            if (player == null) {
                return;
            }
        }

        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Party party = Party.player(player);
        BigDecimal balance = economy.getBalance(party);
        String formattedBalance = economy.getCurrency().format(balance);
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        ConfigMessage message = isDifferentPlayer(sender, player) ? config.balanceOther : config.balanceSelf;
        Messaging.send(sender, message.build(this.plugin, player, senderPlayer,
                "player", player.getName(),
                "amount", formattedBalance));
    }

    @Execute(name = "pay")
    @Shortcut("pay")
    @Permission("bearconomy.command.pay")
    @Description("Send money to another player.")
    public void pay(
            @Context Player sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal amount
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party senderParty = Party.player(sender);
        Party targetParty = Party.player(player);

        // do not allow the player to pay themselves
        if (!isDifferentPlayer(sender, player)) {
            Messaging.send(sender, config.paySelf.build(this.plugin, player, sender));
            return;
        }

        if (amount.equals(BigDecimal.ZERO) || amount.compareTo(BigDecimal.ZERO) < 0) {
            Messaging.send(sender, config.error.build(this.plugin, player, sender,
                    "response", config.invalidTransactionAmount.build(this.plugin)));
            return;
        }

        // withdraw the money from the sender's account
        Response response = economy.withdraw(senderParty, Transaction.of(amount, "sending money:pay:" + player.getName()));
        if (response.failed()) {
            Object reason = Reasons.INSUFFICIENT_BALANCE.equals(response.getReason())
                    ? config.insufficientBalanceSelf.build(this.plugin, player, sender, "player", sender.getName())
                    : response.getReason();
            Messaging.send(sender, config.error.build(this.plugin, player, sender, "response", reason));
            return;
        }

        // deposit the money into the recipient's account
        response = economy.deposit(targetParty, Transaction.of(amount, "receiving money:pay:" + sender.getName()));
        if (response.failed()) {
            // deposit the money back into the sender's account
            economy.deposit(senderParty, Transaction.of(amount, "rolling back:pay:" + player.getName()));

            Object reason = Reasons.OVERSIZE_BALANCE.equals(response.getReason()) && economy.getConfig().hasMaxBalance()
                    ? config.oversizeBalanceOther.build(this.plugin,  player, sender,
                        "player", player.getName(),
                        "limit", currency.format(economy.getConfig().getMaxBalance()))
                    : response.getReason();
            Messaging.send(sender, config.error.build(this.plugin, player, sender,
                    "response", reason));
            return;
        }

        // notify players
        Messaging.send(sender, config.paymentSent.build(this.plugin, player, sender,
                "amount", currency.format(amount),
                "player", player.getName()));

        Messaging.attemptSend(player, config.paymentReceived.build(this.plugin, player, sender,
                "amount", currency.format(amount),
                "player", sender.getName()));
    }

    @Execute(name = "balancetop")
    @Shortcut({ "balancetop", "baltop" })
    @Permission("bearconomy.command.balancetop")
    @Description("Display the balance leaderboard.")
    public void balanceTop(
            @Context CommandSender sender,
            @Arg("page") Optional<Integer> pageNumber
    ) {
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        Config config = Config.getInstance();
        int page = pageNumber.orElse(1);
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();

        CompletableFuture<List<LeaderboardEntry>> leaderboard = this.leaderboard.getForEconomy(economy);
        if (leaderboard.isDone()) {
            this.dispatchBalanceTop(sender, currency, page, config, leaderboard.getNow(null));
        }
        else if (leaderboard.isCompletedExceptionally()) {
            Messaging.send(sender, config.error.build(this.plugin, senderPlayer,
                    "response", "An unexpected error has occurred"));
        }
        else {
            leaderboard.whenComplete((board, ex) -> {
                if (ex != null) {
                    Messaging.send(sender, config.error.build(this.plugin, senderPlayer,
                            "response", "An unexpected error has occurred"));
                    ex.printStackTrace();
                }
                else {
                    this.dispatchBalanceTop(sender, currency, page, config, board);
                }
            });

            int length = Bukkit.getOfflinePlayers().length;
            Messaging.send(sender, config.sortingBalances.build(this.plugin, senderPlayer,
                    "amount", length));
        }
    }

    private void dispatchBalanceTop(@NotNull CommandSender sender, @NotNull Currency currency, int page,
                                    @NotNull Config config, @Nullable List<LeaderboardEntry> entries) {
        if (entries == null) {
            return;
        }

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
        entries.forEach(entry -> total.updateAndGet(a -> a.add(entry.getBalance())));

        String command = "/balancetop %page%";
        Component title = config.balanceTopTitle.build(this.plugin, senderPlayer, "type", currency.getSingularName());
        ConfigMessage balanceTopEntry = config.currencyBalanceTopEntries.getOrDefault(currency.getId(), config.balanceTopEntry);
        Component compiledPage = Formatting.page(this.plugin, title, entries, command, page, 10, entry -> {
            return balanceTopEntry.build(this.plugin, Bukkit.getPlayer(entry.getParty().getId()), senderPlayer,
                    "position", entry.getPosition(),
                    "player", entry.getParty().getName(),
                    "amount", currency.format(entry.getBalance()));
        });

        Messaging.send(sender, Components.joinNewLines(
                compiledPage,
                config.serverTotal.build(this.plugin, senderPlayer, "amount", currency.format(total.get()))
        ));
    }

    private static boolean isDifferentPlayer(@NotNull CommandSender sender, @NotNull OfflinePlayer player) {
        if (!(sender instanceof Player)) {
            return true;
        }

        return !((Player) sender).getUniqueId().equals(player.getUniqueId());
    }
}
