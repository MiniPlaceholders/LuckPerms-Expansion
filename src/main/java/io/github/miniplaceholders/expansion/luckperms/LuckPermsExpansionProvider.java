package io.github.miniplaceholders.expansion.luckperms;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;

public final class LuckPermsExpansionProvider implements ExpansionProvider {

    @Override
    public Expansion provideExpansion() {
        return LuckPermsExpansion.luckPerms();
    }

    @Override
    public LoadRequirement loadRequirement() {
        return LoadRequirement.requiredComplement("luckperms", "LuckPerms");
    }

}
