package io.github.miniplaceholders.expansion.luckperms.fabric;

import io.github.miniplaceholders.expansion.luckperms.common.CommonExpansion;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FabricMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("miniplaceholders-luckperms-expansion");

    @Override
    public void onInitialize() {
        LOGGER.info("Starting LuckPerms Expansion for Fabric");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            final LuckPerms luckPerms = LuckPermsProvider.get();
            new CommonExpansion(luckPerms).commonBuilder()
                    .filter(ServerPlayer.class)
                    .build()
                    .register();
        });
    }

}
