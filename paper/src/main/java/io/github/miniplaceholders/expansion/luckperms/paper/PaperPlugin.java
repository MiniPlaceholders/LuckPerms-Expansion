package io.github.miniplaceholders.expansion.luckperms.paper;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.LegacyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.util.Tristate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PaperPlugin extends JavaPlugin {
	private static final Component FALSE_COMPONENT = Component.text(false, NamedTextColor.RED);
	private static final Component TRUE_COMPONENT = Component.text(true, NamedTextColor.GREEN);
	private static final Component UNDEFINED_COMPONENT = Component.text("undefined", NamedTextColor.GRAY);
	@Override
	public void onEnable(){
		this.getSLF4JLogger().info("Starting LuckPerms Expansion for Paper");

		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider == null) {
			this.getSLF4JLogger().error("Cannot found LuckPerms plugin, disabling expansion");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		LuckPerms luckPerms = provider.getProvider();

		PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);
        
		Expansion.builder("luckperms")
			.filter(Player.class)
			.audiencePlaceholder("prefix", (aud, queue, ctx) ->
				Tag.inserting(LegacyUtils.parsePossibleLegacy(adapter.getMetaData((Player)aud).getPrefix())))
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
				Component groups = user.getInheritedGroups(user.getQueryOptions()).stream()
					.map(group -> LegacyUtils.parsePossibleLegacy(group.getDisplayName()))
					.collect(Component.toComponent(Component.space()));
				return Tag.selfClosingInserting(groups);	
			})
			.audiencePlaceholder("primary_group_name", (aud, queue, ctx) -> {
					final String primaryGroup = adapter.getUser((Player)aud).getCachedData().getMetaData().getPrimaryGroup();
					if (primaryGroup == null) {
						return null;
					}
					return Tag.preProcessParsed(primaryGroup);
			})
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
