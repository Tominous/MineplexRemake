package org.mswsplex.mineplex.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mswsplex.mineplex.managers.CPlayer;
import org.mswsplex.mineplex.managers.Stats;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class ChatListener implements Listener {
	private Mineplex plugin;

	public ChatListener(Mineplex plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		CPlayer cp = plugin.getCPlayer(player);

		String msg = MSG.filter(event.getMessage());
		Stats stats = cp.getStats();

		for (Player target : Bukkit.getOnlinePlayers()) {
			target.sendMessage(MSG.color(
					MSG.getLevelColor(stats.getLevel()) + stats.getLevel() + " " + stats.getRank().getPrefix() + "&e")
					+ player.getName() + MSG.color(" &r") + msg);
		}

	}
}
