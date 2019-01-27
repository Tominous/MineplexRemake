package org.mswsplex.mineplex.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mswsplex.mineplex.managers.CPlayer;
import org.mswsplex.mineplex.managers.Rank;
import org.mswsplex.mineplex.managers.Stats;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class OnJoinListener implements Listener {
	private Mineplex plugin;

	public OnJoinListener(Mineplex plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		CPlayer cp = plugin.getCPlayer(player);
		Stats stats = cp.getStats();

		if (stats.getRank() == null) {
			MSG.tell(player, "Seems to be your first time here...");
			stats.setRank(Rank.DEFAULT);
		}

		List<String> news = plugin.config.getStringList("News");
		news.forEach((line) -> {
			MSG.tell(player, line);
		});

		cp.refreshTabName();
	}
}
