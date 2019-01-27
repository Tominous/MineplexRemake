package org.mswsplex.mineplex.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class NewsCommand implements CommandExecutor {
	private Mineplex plugin;

	public NewsCommand(Mineplex plugin) {
		this.plugin = plugin;
		PluginCommand command = plugin.getCommand("news");
		command.setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!plugin.getCPlayer(player).getStats().getRank().isAdminStaff()) {
				MSG.noPerm(sender);
				return true;
			}
		}
		if (args.length == 0) {
			MSG.sendHelp(sender, 0, "news");
			return true;
		}
		List<String> news = plugin.config.getStringList("News");
		String msg;
		int id;

		switch (args[0].toLowerCase()) {
		case "list":
			for (int i = 0; i < news.size(); i++)
				MSG.tell(sender, "&e" + i + "&7: &r" + news.get(i));
			return true;
		case "add":
			if (args.length < 2) {
				MSG.tell(sender, MSG.getString("Missing.Message", "specify a message"));
				return true;
			}
			msg = "";
			for (int i = 1; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg = msg.trim();
			news.add(msg);
			break;
		case "set":
			if (args.length < 2) {
				MSG.tell(sender, MSG.getString("Missing.Number", "specify a number"));
				return true;
			}
			if (args.length < 3) {
				MSG.tell(sender, MSG.getString("Missing.Message", "specify a message"));
				return true;
			}
			id = Integer.parseInt(args[1]);
			msg = "";
			for (int i = 2; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg = msg.trim();
			news.set(id, msg);
			break;
		case "delete":
			if (args.length < 2) {
				MSG.tell(sender, MSG.getString("Missing.Number", "specify a number"));
				return true;
			}
			id = Integer.parseInt(args[1]);
			news.remove(id);
			break;
		default:
			MSG.sendHelp(sender, 0, "news");
			return false;
		}
		plugin.config.set("News", news);
		MSG.tell(sender, "&9News> &7Successfully updated the news.");
		return true;
	}
}
