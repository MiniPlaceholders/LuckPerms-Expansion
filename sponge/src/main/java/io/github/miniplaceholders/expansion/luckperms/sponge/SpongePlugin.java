package io.github.miniplaceholders.expansion.luckperms.sponge;

import com.google.inject.Inject;
import io.github.miniplaceholders.expansion.luckperms.common.CommonExpansion;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("miniplaceholders-luckperms-expansion")
public class SpongePlugin {
    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(final StartingEngineEvent<Server> event) {
        this.logger.info("Starting LuckPerms Expansion for Sponge");

        final LuckPerms luckPerms = LuckPermsProvider.get();
        new CommonExpansion(luckPerms).commonBuilder()
                .filter(ServerPlayer.class)
                .build()
                .register();
    }
}
