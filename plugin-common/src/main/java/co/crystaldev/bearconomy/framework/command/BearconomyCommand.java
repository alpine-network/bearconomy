package co.crystaldev.bearconomy.framework.command;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.command.AlpineCommand;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public void give(
            @Context CommandSender sender,
            @Arg("player") Player player,
            @Arg("amount") BigDecimal amount
    ) {
        Economy economy = Bearconomy.get().getEconomy();
        Party party = Party.player(player);

        Response response = economy.deposit(party, Transaction.of(amount, "withdraw"));
        if (response.succeeded()) {
            Currency currency = economy.getCurrency();
            sender.sendMessage("Added " + currency.format(amount) + " to " + player.getName()
                    + ". New balance: " + currency.format(response.getNewBalance())
                    + ", Old Balance: " + currency.format(response.getPreviousBalance()));
        }
        else if (response.failed()) {
            sender.sendMessage("Failed (" + response.getResult() + "): " + response.getReason());
        }
    }

    @Execute(name = "withdraw")
    public void take(
            @Context CommandSender sender,
            @Arg("player") Player player,
            @Arg("amount") BigDecimal amount
    ) {
        Economy economy = Bearconomy.get().getEconomy();
        Party party = Party.player(player);

        Response response = economy.withdraw(party, Transaction.of(amount, "withdraw"));
        if (response.succeeded()) {
            Currency currency = economy.getCurrency();
            sender.sendMessage("Removed " + currency.format(amount) + " from " + player.getName()
                    + ". New balance: " + currency.format(response.getNewBalance())
                    + ", Old Balance: " + currency.format(response.getPreviousBalance()));
        }
        else if (response.failed()) {
            sender.sendMessage("Failed (" + response.getResult() + "): " + response.getReason());
        }
    }

    @Execute(name = "set")
    public void set(
            @Context CommandSender sender,
            @Arg("player") Player player,
            @Arg("amount") BigDecimal balance
    ) {
        Economy economy = Bearconomy.get().getEconomy();
        Party party = Party.player(player);

        Response response = economy.set(party, balance);
        if (response.succeeded()) {
            String formattedBalance = economy.getCurrency().format(balance);
            sender.sendMessage("Balance of " + player.getName() + ": " + formattedBalance);
        }
        else if (response.failed()) {
            sender.sendMessage("Failed (" + response.getResult() + "): " + response.getReason());
        }
    }

    @Execute(name = "bal")
    @Shortcut({ "balance", "bal" })
    public void bal(
            @Context CommandSender sender,
            @Arg("player") Player player
    ) {
        Economy economy = Bearconomy.get().getEconomy();
        Party party = Party.player(player);
        BigDecimal balance = economy.getBalance(party);
        String formattedBalance = economy.getCurrency().format(balance);
        sender.sendMessage("Balance of " + player.getName() + ": " + formattedBalance);
    }
}
