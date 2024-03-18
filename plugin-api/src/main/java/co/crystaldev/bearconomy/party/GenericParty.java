package co.crystaldev.bearconomy.party;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author BestBearr <crumbygames12@gmail.com>
 * @since 03/18/2024
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public final class GenericParty implements Party {

    private final UUID id;

    @Override
    public @NotNull String getName() {
        return this.id.toString();
    }
}
