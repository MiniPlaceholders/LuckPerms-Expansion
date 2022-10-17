package me.dreamerzero.luckpermsexpansion.velocity;

import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.proxy.Player;

import org.slf4j.Logger;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.utils.LegacyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.util.Tristate;

@Plugin(
    name = "LuckPerms-Expansion",
    id = "luckpermsexpansion",
    version = "1.0.0",
    authors = {"4drian3d"},
    dependencies = {
        @Dependency(
            id = "miniplaceholders",
            optional = false
        ),
        @Dependency(
            id = "luckperms",
            optional = false
        )
    }
        
)
public final class VelocityPlugin {
    private final Logger logger;
    private static final Component FALSE_COMPONENT = Component.text(false, NamedTextColor.RED);
	private static final Component TRUE_COMPONENT = Component.text(true, NamedTextColor.GREEN);
	private static final Component UNDEFINED_COMPONENT = Component.text("undefined", NamedTextColor.GRAY);

    @Inject
    public VelocityPlugin(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting LuckPerms Expansion for Velocity");

        LuckPerms luckPerms = LuckPermsProvider.get();

        PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);
		
        Expansion.builder("luckperms")
            .filter(Player.class)
            .audiencePlaceholder("prefix", (aud, queue, ctx) -> Tag.inserting(LegacyUtils.parsePossibleLegacy(adapter.getMetaData((Player)aud).getPrefix())))
            .audiencePlaceholder("suffix", (aud, queue, ctx) -> 
                Tag.inserting(LegacyUtils.parsePossibleLegacy(adapter.getMetaData((Player)aud).getSuffix())))
            .audiencePlaceholder("has_permission", (aud, queue, ctx) -> {
                User user = adapter.getUser((Player)aud);
                String permission = queue.popOr(() -> "you need to introduce an permission").value();
                Tristate result = user.getCachedData().getPermissionData().checkPermission(permission);
                return Tag.selfClosingInserting(result.asBoolean() 
                    ? TRUE_COMPONENT
                    : FALSE_COMPONENT
                );
            })
            .audiencePlaceholder("check_permission", (aud, queue, ctx) -> {
                User user = adapter.getUser((Player)aud);
                String permission = queue.popOr(() -> "you need to introduce an permission").value();
                Tristate result = user.getCachedData().getPermissionData().checkPermission(permission);
				
                return Tag.selfClosingInserting(switch(result) {
                    case TRUE -> TRUE_COMPONENT;
                    case FALSE -> FALSE_COMPONENT;
                    case UNDEFINED -> UNDEFINED_COMPONENT;
                });
            })
            .audiencePlaceholder("inherited_groups", (aud, queue, ctx) -> {
                User user = adapter.getUser((Player)aud);
                String groups = user.getInheritedGroups(user.getQueryOptions()).stream()
                    .map(Group::getDisplayName)
                    .collect(Collectors.joining(" "));
                return Tag.selfClosingInserting(LegacyUtils.parsePossibleLegacy(groups));	
            })
            .audiencePlaceholder("primary_group_name", (aud, queue, ctx) ->
                Tag.selfClosingInserting(Component.text(adapter.getUser((Player)aud).getCachedData().getMetaData().getPrimaryGroup())))
            .audiencePlaceholder("inherits_group", (aud, queue, ctx) -> {
                User user = adapter.getUser((Player)aud);
                Group group = luckPerms.getGroupManager().getGroup(queue.popOr(() -> "you need to provide a group").value());
                return Tag.selfClosingInserting(group != null && user.getInheritedGroups(user.getQueryOptions()).contains(group)
                    ? TRUE_COMPONENT
                    : FALSE_COMPONENT
                );
            })
            .build()
            .register();
    }
}
