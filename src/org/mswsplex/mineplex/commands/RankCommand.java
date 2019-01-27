package org.mswsplex.mineplex.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mswsplex.mineplex.managers.Rank;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class RankCommand implements CommandExecutor, TabCompleter {
	private Mineplex plugin;

	public RankCommand(Mineplex plugin) {
		this.plugin = plugin;
		PluginCommand command = plugin.getCommand("rank");
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!plugin.getCPlayer(player).getStats().getRank().isAdminStaff()) {
				MSG.noPerm(sender);
				return true;
			}
		}
		if (args.length == 0) {
			MSG.sendHelp(sender, 0, "rank");
			return true;
		}

		OfflinePlayer target;
		if (args.length < 2) {
			MSG.tell(sender, MSG.getString("Missing.Player", "specify a player"));
			return true;
		}
		switch (args[0].toLowerCase()) {
		case "set":
			target = Bukkit.getOfflinePlayer(args[1]);
			plugin.getCPlayer(target).getStats().setRank(Rank.valueOf(args[2].toUpperCase()));
			plugin.getCPlayer(target).getStats().saveData();
			break;
		case "get":
			target = Bukkit.getOfflinePlayer(args[1]);
			MSG.tell(sender, target.getName() + " has rank: " + plugin.getCPlayer(target).getStats().getRank() + ".");
			break;
		default:
			MSG.sendHelp(sender, 0, "rank");
			return false;
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!plugin.getCPlayer(player).getStats().getRank().isAdminStaff()) {
				return result;
			}
		}

		if (args.length <= 1) {
			for (String res : new String[] { "get", "set" }) {
				if (res.startsWith(args[0].toLowerCase()))
					result.add(res);
			}
		}
		if (args.length == 2) {
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (target.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(target.getName());
				}
			}
		}

		if (args.length > 2)
			for (Rank rank : Rank.values()) {
				if (rank.toString().toLowerCase().startsWith(args[2].toLowerCase()))
					result.add(MSG.camelCase(rank + ""));
			}

		return result;
	}
}
