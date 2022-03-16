package me.dreamerzero.example.paper;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.dreamerzero.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class PaperPlugin extends JavaPlugin {

	@Override
	public void onEnable(){
		this.getSLF4JLogger().info("Starting Example Expansion for Paper");
        
		Expansion.builder("example")
			.filter(Player.class)
			.audiencePlaceholder("name", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((Player)aud).getName())))
			.build()
			.register();
	}
}
