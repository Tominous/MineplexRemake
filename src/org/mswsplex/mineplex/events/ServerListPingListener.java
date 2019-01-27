package org.mswsplex.mineplex.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class ServerListPingListener implements Listener {
	private Mineplex plugin;

	public ServerListPingListener(Mineplex plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		event.setMotd(MSG.color(plugin.config.getString("MOTD").replace("\n", "\n")));
		event.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
	}
}
