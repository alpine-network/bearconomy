package co.crystaldev.bearconomy.party;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @since 0.1.0
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public final class GenericParty implements Party {

    private final UUID id;

    @Override
    public @NotNull String getName() {
        return this.id.toString();
    }
}
