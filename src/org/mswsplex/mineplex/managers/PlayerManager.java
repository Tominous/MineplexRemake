package org.mswsplex.mineplex.managers;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class PlayerManager implements Listener {
	private Mineplex plugin;

	private HashMap<Player, CPlayer> players;

	public PlayerManager(Mineplex plugin) {
		this.plugin = plugin;
		players = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public CPlayer getPlayer(Player player) {
		if (!players.containsKey(player))
			players.put(player, new CPlayer(player, plugin));
		return players.get(player);
	}

	@SuppressWarnings("unchecked")
	public List<Player> getLoadedPlayers() {
		return (List<Player>) players.keySet();
	}

	public void removePlayer(Player player) {
		MSG.log("saving and removing player data for " + player.getName() + " data is loaded: "
				+ players.containsKey(player));
		if (players.containsKey(player))
			players.get(player).saveData();
		players.remove(player);
	}

	public void clearPlayers() {
		for (Player player : players.keySet())
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

	public Rank getRank(Player player) {
		int highest = 0;
		for (Rank r : Rank.values()) {
			if (player.hasPermission("mineplex.rank." + r))
				if (r.ordinal() > highest) {
					highest = r.ordinal();
				}
		}
		return Rank.values()[highest];
	}
	
	
}
