package co.crystaldev.example.command;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.command.AlpineCommand;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.Response;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.economy.transaction.Transaction;
import co.crystaldev.bearconomy.party.Party;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Command(name = "gem")
@Description("Manage the Gem economy")
@Permission("example.command.gem")
public final class GemCommand extends AlpineCommand {
    GemCommand(@NotNull AlpinePlugin plugin) {
        super(plugin);
    }

    @Execute(name = "give")
    @Permission("example.command.gem.admin")
    public void give(
            @Context CommandSender sender,
            @Arg("target") @Async OfflinePlayer recipient,
            @Arg("amount") BigDecimal amount
    ) {
        Economy gemEconomy = Bearconomy.get().fetchEconomy("gem");
        Currency gemCurrency = gemEconomy.getCurrency();

        // Create a new party for the recipient player
        Party playerParty = Party.player(recipient);

        // Format the actor executing this transaction for the description
        String actorId = sender instanceof Player ? ((Player) sender).getUniqueId().toString() : sender.getName();

        // Create a description string to attach to the transaction
        String description = String.join(":", "admin_command", actorId, amount.toPlainString());

        // Create the transaction to be executed
        Transaction transaction = Transaction.of(amount, description);

        // Execute the transaction
        Response response = gemEconomy.deposit(playerParty, transaction);

        // Notify the executing player
        String formattedAmount = gemCurrency.format(amount);
        if (response.failed()) {
            sender.sendMessage(ChatColor.RED + "Failed to deposit " + formattedAmount + ": " + response.getReason());
        }
        else {
            String newBalance = gemCurrency.format(response.getNewBalance());

            sender.sendMessage(ChatColor.GREEN + "Deposited " + formattedAmount + ": " + newBalance);
        }
    }

    @Execute(name = "balance", aliases = "bal")
    public void balance(
            @Context CommandSender sender,
            @OptionalArg("target") @Async OfflinePlayer target
    ) {
        if (target == null && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can have a balance!");
            return;
        }

        Economy gemEconomy = Bearconomy.get().fetchEconomy("gem");
        Currency gemCurrency = gemEconomy.getCurrency();

        OfflinePlayer targetPlayer = target != null ? target : (OfflinePlayer) sender;

        // Create a new party to query the balance
        Party playerParty = Party.player(targetPlayer);

        // Query the player's balance
        BigDecimal balance = gemEconomy.getBalance(playerParty);

        // Notify the sender
        String formattedBalance = gemCurrency.format(balance);
        sender.sendMessage(ChatColor.GREEN + "Balance of " + targetPlayer.getName() + ": " + formattedBalance);
    }
}
