package co.crystaldev.bearconomy.framework.command;

import co.crystaldev.alpinecore.config.AlpineCoreConfig;
import co.crystaldev.alpinecore.framework.command.AlpineArgumentResolver;
import co.crystaldev.bearconomy.framework.BearconomyPlugin;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

/**
 * @since 0.1.0
 */
public final class OfflinePlayerArgument extends AlpineArgumentResolver<OfflinePlayer> {
    public OfflinePlayerArgument() {
        super(OfflinePlayer.class, "offlinePlayer");
    }

    @Override
    protected ParseResult<OfflinePlayer> parse(Invocation<CommandSender> invocation, Argument<OfflinePlayer> context, String argument) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(argument);
        if (player != null && player.hasPlayedBefore()) {
            return ParseResult.success(player);
        }

        BearconomyPlugin plugin = BearconomyPlugin.getInstance();
        AlpineCoreConfig config = plugin.getConfiguration(AlpineCoreConfig.class);
        return ParseResult.failure(config.playerNotFound.buildString(plugin, "player", argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<OfflinePlayer> argument, SuggestionContext context) {
        String current = context.getCurrent().lastLevel().toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .filter(v -> v.toLowerCase().startsWith(current))
                .collect(SuggestionResult.collector());
    }
}
