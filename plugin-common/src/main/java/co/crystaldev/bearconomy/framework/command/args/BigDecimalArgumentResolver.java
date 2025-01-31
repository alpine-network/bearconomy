package co.crystaldev.bearconomy.framework.command.args;

import co.crystaldev.alpinecore.framework.command.AlpineArgumentResolver;
import co.crystaldev.alpinecore.util.CollectionUtils;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 1.0.3
 */
public final class BigDecimalArgumentResolver extends AlpineArgumentResolver<BigDecimal> {
    private BigDecimalArgumentResolver() {
        super(BigDecimal.class, null);
    }

    private static final List<String> SUFFIXES = CollectionUtils.list("", "k", "m", "b");

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^(\\d+\\.?\\d*)([kKmMbB]?)$");

    @Override
    protected ParseResult<BigDecimal> parse(Invocation<CommandSender> invocation, Argument<BigDecimal> context, String argument) {
        try {
            Matcher matcher = NUMBER_PATTERN.matcher(argument);

            if (!matcher.matches()) {
                return ParseResult.failure("Invalid number format: " + argument);
            }
            String numberPart = matcher.group(1);
            String suffix = matcher.group(2);

            BigDecimal number = new BigDecimal(numberPart);

            switch (suffix) {
                case "k":
                case "K":
                    number = number.multiply(new BigDecimal("1000"));
                    break;
                case "m":
                case "M":
                    number = number.multiply(new BigDecimal("1000000"));
                    break;
                case "b":
                case "B":
                    number = number.multiply(new BigDecimal("1000000000"));
                    break;
            }

            return ParseResult.success(number);
        } catch (NumberFormatException e) {
            return ParseResult.failure(e.getMessage());
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<BigDecimal> argument, SuggestionContext context) {
        String input = context.getCurrent().toString();

        if (input.isEmpty()) {
            return SuggestionResult.of("1", "10", "100", "1k", "10k", "100k", "1m", "10m");
        }
        else if (Character.isLetter(input.charAt(input.length() - 1))) {
            return SuggestionResult.empty();
        }

        return SUFFIXES.stream()
                .map(suffix -> input + (input.endsWith(".") ? "0" : "") + suffix)
                .collect(SuggestionResult.collector());
    }
}
