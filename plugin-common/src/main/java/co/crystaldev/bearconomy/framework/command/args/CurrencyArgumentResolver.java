package co.crystaldev.bearconomy.framework.command.args;

import co.crystaldev.alpinecore.framework.command.AlpineArgumentResolver;
import co.crystaldev.bearconomy.Bearconomy;
import co.crystaldev.bearconomy.economy.Economy;
import co.crystaldev.bearconomy.economy.currency.Currency;
import co.crystaldev.bearconomy.framework.BearconomyPlugin;
import co.crystaldev.bearconomy.framework.config.Config;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @since 1.0.5
 */
public final class CurrencyArgumentResolver extends AlpineArgumentResolver<Currency> {

    private CurrencyArgumentResolver() {
        super(Currency.class, null);
    }

    @Override
    protected ParseResult<Currency> parse(Invocation<CommandSender> invocation, Argument<Currency> context, String argument) {
        Map<Currency, Economy> currencies = Bearconomy.get().getCurrencies();

        for (Currency currency : currencies.keySet()) {
            if (currency.getId().equalsIgnoreCase(argument)) {
                return ParseResult.success(currency);
            }
        }

        BearconomyPlugin plugin = BearconomyPlugin.getInstance();
        Config config = plugin.getConfiguration(Config.class);

        return ParseResult.failure(config.error.build(plugin,
                "response", config.invalidCurrency.build(plugin)));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Currency> argument, SuggestionContext context) {
        String input = context.getCurrent().toString();

        Map<Currency, Economy> currencies = Bearconomy.get().getCurrencies();
        return currencies.keySet().stream()
                .map(Currency::getId)
                .filter(id -> id.toLowerCase().startsWith(input.toLowerCase()))
                .collect(SuggestionResult.collector());
    }
}
