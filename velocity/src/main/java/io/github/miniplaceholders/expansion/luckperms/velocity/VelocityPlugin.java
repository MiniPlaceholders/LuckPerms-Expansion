package io.github.miniplaceholders.expansion.luckperms.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import io.github.miniplaceholders.expansion.luckperms.common.CommonExpansion;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.slf4j.Logger;

@Plugin(
        name = "LuckPerms-Expansion",
        id = "luckperms-expansion",
        version = "1.0.0",
        authors = {"4drian3d"},
        dependencies = {
                @Dependency(id = "miniplaceholders"),
                @Dependency(id = "luckperms")
        }

)
public final class VelocityPlugin {
    private final Logger logger;

    @Inject
    public VelocityPlugin(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(final ProxyInitializeEvent event) {
        logger.info("Starting LuckPerms Expansion for Velocity");

        final LuckPerms luckPerms = LuckPermsProvider.get();

        new CommonExpansion(luckPerms).commonBuilder()
                .filter(Player.class)
                .build()
                .register();
    }
}
