package org.mswsplex.mineplex.managers;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mswsplex.mineplex.msws.Mineplex;

public class PlayerManager implements Listener {
	private Mineplex plugin;

	private HashMap<OfflinePlayer, CPlayer> players;

	public PlayerManager(Mineplex plugin) {
		this.plugin = plugin;
		players = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public CPlayer getPlayer(OfflinePlayer player) {
		if (!players.containsKey(player))
			players.put(player, new CPlayer(player, plugin));
		return players.get(player);
	}

	@SuppressWarnings("unchecked")
	public List<OfflinePlayer> getLoadedPlayers() {
		return (List<OfflinePlayer>) players.keySet();
	}

	public void removePlayer(OfflinePlayer player) {
		if (players.containsKey(player)) {
			players.get(player).getStats().saveData();
			players.get(player).saveData();
		}
		players.remove(player);
	}

	public void clearPlayers() {
		for (OfflinePlayer player : players.keySet())
			removePlayer(player);
	}

	public void loadData(Player player) {
		if (players.containsKey(player))
			throw new IllegalArgumentException("Player data already loaded");
		players.put(player, new CPlayer(player, plugin));
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		loadData(event.getPlayer());
	}
}
