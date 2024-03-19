package co.crystaldev.bearconomy.framework.command;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.command.AlpineCommand;
import co.crystaldev.alpinecore.framework.config.object.ConfigMessage;
import co.crystaldev.alpinecore.util.Messaging;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.Reasons;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.framework.config.Config;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @author BestBearr <crumbygames12@gmail.com>
 * @since 03/18/2024
 */
@Command(name = "bearconomy")
@Description("Main command for Bearconomy.")
public final class BearconomyCommand extends AlpineCommand {
    BearconomyCommand(AlpinePlugin plugin) {
        super(plugin);
    }

    @Execute(name = "deposit")
    @Permission("bearconomy.admin.deposit")
    public void deposit(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal amount
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party party = Party.player(player);

        Response response = economy.deposit(party, Transaction.of(amount, "admin deposit"));
        if (response.succeeded()) {
            if (isDifferentPlayer(sender, player)) {
                Messaging.send(sender, config.deposit.build(this.plugin,
                        "player", player.getName(),
                        "amount", currency.format(amount),
                        "balance", currency.format(economy.getBalance(party))));
            }

            Messaging.attemptSend(player, config.deposited.build(this.plugin,
                    "player", sender.getName(),
                    "amount", currency.format(amount),
                    "balance", currency.format(economy.getBalance(party))));
        }
        else if (response.failed()) {
            ConfigMessage errorMessage = isDifferentPlayer(sender, player) ? config.oversizeBalanceOther : config.oversizeBalanceSelf;
            Object reason = Reasons.OVERSIZE_BALANCE.equals(response.getReason()) && economy.getConfig().hasMaxBalance()
                    ? errorMessage.build(this.plugin, "player", player.getName(), "limit", currency.format(economy.getConfig().getMaxBalance()))
                    : response.getReason();
            Messaging.send(sender, config.error.build(this.plugin, "response", reason));
        }
    }

    @Execute(name = "withdraw")
    @Permission("bearconomy.admin.withdraw")
    public void withdraw(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal amount
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party party = Party.player(player);

        Response response = economy.withdraw(party, Transaction.of(amount, "admin withdraw"));
        if (response.succeeded()) {
            if (isDifferentPlayer(sender, player)) {
                Messaging.send(sender, config.withdraw.build(this.plugin,
                        "player", player.getName(),
                        "amount", currency.format(amount),
                        "balance", currency.format(economy.getBalance(party))));
            }

            Messaging.attemptSend(player, config.withdrew.build(this.plugin,
                    "player", sender.getName(),
                    "amount", currency.format(amount),
                    "balance", currency.format(economy.getBalance(party))));
        }
        else if (response.failed()) {
            ConfigMessage errorMessage = isDifferentPlayer(sender, player) ? config.insufficientBalanceOther : config.insufficientBalanceSelf;
            Object reason = Reasons.INSUFFICIENT_BALANCE.equals(response.getReason())
                    ? errorMessage.build(this.plugin, "player", player.getName())
                    : response.getReason();
            Messaging.send(sender, config.error.build(this.plugin, "response", reason));
        }
    }

    @Execute(name = "set")
    @Permission("bearconomy.admin.set")
    public void setBalance(
            @Context CommandSender sender,
            @Arg("player") @Key("offlinePlayer") OfflinePlayer player,
            @Arg("amount") BigDecimal balance
    ) {
        Config config = Config.getInstance();
        Economy economy = Bearconomy.get().getEconomy();
        Currency currency = economy.getCurrency();
        Party party = Party.player(player);

        Response response = economy.set(party, balance);
        if (response.succeeded()) {
            String formattedBalance = currency.format(balance);

            if (isDifferentPlayer(sender, player)) {
                Messaging.send(sender, config.balanceUpdatedOther.build(this.plugin,
                        "player", player.getName(),
                        "amount", formattedBalance));
            }

            Messaging.attemptSend(player, config.balanceUpdatedSelf.build(this.plugin,
                    "player", sender.getName(),
                    "amount", formattedBalance));
        }
        else if (response.failed()) {
            Messaging.send(sender, config.error.build(this.plugin, "response", response.getReason()));
        }
    }

    @Execute(name = "bal")
    @Shortcut({ "balance", "bal" })
    @Permission("bearconomy.admin.balance")
    public void showBalance(
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

        ConfigMessage message = isDifferentPlayer(sender, player) ? config.balanceOther : config.balanceSelf;
        Messaging.send(sender, message.build(this.plugin,
                "player", player.getName(),
                "amount", formattedBalance));
    }

    private static boolean isDifferentPlayer(@NotNull CommandSender sender, @NotNull OfflinePlayer player) {
        if (!(sender instanceof Player)) {
            return true;
        }

        return !((Player) sender).getUniqueId().equals(player.getUniqueId());
    }
}
