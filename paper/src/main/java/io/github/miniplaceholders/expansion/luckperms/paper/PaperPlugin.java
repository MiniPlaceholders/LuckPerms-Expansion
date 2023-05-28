package io.github.miniplaceholders.expansion.luckperms.paper;

import io.github.miniplaceholders.expansion.luckperms.common.CommonExpansion;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PaperPlugin extends JavaPlugin {
	@Override
	public void onEnable(){
		this.getSLF4JLogger().info("Starting LuckPerms Expansion for Paper");

		final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider == null) {
			this.getSLF4JLogger().error("Cannot found LuckPerms plugin, disabling expansion");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		final LuckPerms luckPerms = provider.getProvider();

		new CommonExpansion(luckPerms).commonBuilder()
				.filter(Player.class)
				.build()
				.register();
	}
}
