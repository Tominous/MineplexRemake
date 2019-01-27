package org.mswsplex.mineplex.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mswsplex.mineplex.managers.CPlayer;
import org.mswsplex.mineplex.msws.Mineplex;

public class MSG {
	public static Mineplex plugin;

	/**
	 * Returns the string with &'s being §
	 * 
	 * @param msg the message to replace
	 * @return returns colored msg
	 */
	public static String color(String msg) {
		if (msg == null || msg.isEmpty())
			return null;
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	/**
	 * Returns string with camel case, and with _'s replaced with spaces
	 * 
	 * @param string hello_how is everyone
	 * @return Hello How Is Everyone
	 */
	public static String camelCase(String string) {
		String prevChar = " ";
		String res = "";
		for (int i = 0; i < string.length(); i++) {
			if (i > 0)
				prevChar = string.charAt(i - 1) + "";
			if (prevChar.matches("[a-zA-Z]")) {
				res = res + ((string.charAt(i) + "").toLowerCase());
			} else {
				res = res + ((string.charAt(i) + "").toUpperCase());
			}
		}
		return res.replace("_", " ");
	}

	/**
	 * Gets a string from lang.yml
	 * 
	 * @param id  key id of the string to get
	 * @param def default string in case lang.yml doesn't have the key
	 * @return
	 */
	public static String getString(String id, String def) {
		return plugin.lang.contains(id) ? plugin.lang.getString(id) : "[" + id + "] " + def;
	}

	/**
	 * Sends the message colored to the sender CommandSender
	 * 
	 * @param sender CommandSender to send message to
	 * @param msg    Message to send
	 */
	public static void tell(CommandSender sender, String msg) {
		if (msg != null && !msg.isEmpty())
			sender.sendMessage(color(msg));
	}

	/**
	 * Sends a message to everyone in a world
	 * 
	 * @param world World to send message to
	 * @param msg   Message to send
	 */
	public static void tell(World world, String msg) {
		if (world != null && msg != null) {
			for (Player target : world.getPlayers()) {
				tell(target, msg);
			}
		}
	}

	/**
	 * Sends a message to all players with a specific permission
	 * 
	 * @param perm Permission to require
	 * @param msg  Message to send
	 */
	public static void tell(String perm, String msg) {
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target.hasPermission(perm))
				tell(target, msg);
		}
	}

	/**
	 * Announces a message to all players
	 * 
	 * @param msg Message to announce
	 */
	public static void announce(String msg) {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			tell(player, msg);
		});
	}

	/**
	 * Sends a no permission message to the target
	 * 
	 * @param sender CommandSender to send message to
	 */
	public static void noPerm(CommandSender sender) {
		tell(sender, getString("NoPermission", "Insufficient Permissions"));
	}

	/**
	 * Logs a message to console
	 * 
	 * @param msg Message to log
	 */
	public static void log(String msg) {
		tell(Bukkit.getConsoleSender(), "[" + plugin.getDescription().getName() + "] " + msg);
	}

	/**
	 * Colored boolean
	 * 
	 * @param bool true/false
	 * @return Green True or Red False
	 */
	public static String TorF(Boolean bool) {
		if (bool) {
			return "&aTrue&r";
		} else {
			return "&cFalse&r";
		}
	}

	/**
	 * Sends a help message with specified pages
	 * 
	 * @param sender  CommandSender to send message to
	 * @param page    Page of help messages
	 * @param command Command to send help to
	 */
	public static void sendHelp(CommandSender sender, int page, String command) {
		if (!plugin.lang.contains("Help." + command.toLowerCase())) {
			tell(sender, getString("UnknownCommand", "There is no help available for this command."));
			return;
		}
		int length = plugin.config.getInt("HelpLength");
		List<String> help = plugin.lang.getStringList("Help." + command.toLowerCase()), list = new ArrayList<String>();
		for (String res : help) {
			if (res.startsWith("perm:")) {
				String perm = "";
				res = res.substring(5, res.length());
				for (char a : res.toCharArray()) {
					if (a == ' ')
						break;
					perm = perm + a;
				}
				if (!sender.hasPermission(perm))
					continue;
				res = res.replace(perm + " ", "");
			}
			list.add(res);
		}
		if (help.size() > length)
			tell(sender, "Page: " + (page + 1) + " of " + (int) Math.ceil((list.size() / length) + 1));
		for (int i = page * length; i < list.size() && i < page * length + length; i++) {
			String res = list.get(i);
			tell(sender, res);
		}
		if (command.equals("default")) // TODO
			tell(sender, "&d&l" + plugin.getDescription().getName() + " &ev" + plugin.getDescription().getVersion()
					+ " &7created by &bMSWS");
	}

	/**
	 * Returns a text progress bar
	 * 
	 * @param prog   0-total double value of progress
	 * @param total  Max amount that progress bar should represent
	 * @param length Length in chars for progress bar
	 * @return
	 */
	public static String progressBar(double prog, double total, int length) {
		return progressBar("&a\u258D", "&c\u258D", prog, total, length);
	}

	/**
	 * Returns a text progress bar with specified chars
	 * 
	 * @param progChar   Progress string to represent progress
	 * @param incomplete Incomplete string to represent amount left
	 * @param prog       0-total double value of progress
	 * @param total      Max amount that progress bar should represent
	 * @param length     Length in chars for progress bar
	 * @return
	 */
	public static String progressBar(String progChar, String incomplete, double prog, double total, int length) {
		String disp = "";
		double progress = Math.abs(prog / total);
		int len = length;
		for (double i = 0; i < len; i++) {
			if (i / len < progress) {
				disp = disp + progChar;
			} else {
				disp = disp + incomplete;
			}
		}
		return color(disp);
	}

	/**
	 * Returns a string for shortened decimal
	 * 
	 * @param decimal Decimal to shorten
	 * @param length  Amount of characters after the ., will add on 0's to meet
	 *                minimum
	 * @return Input: "5978.154123" (Length of 3) Output: "5978.154"
	 */
	public static String parseDecimal(String decimal, int length) {
		if (decimal.contains(".")) {
			if (decimal.split("\\.").length == 1)
				decimal += "0";
			if (decimal.split("\\.")[1].length() > 2) {
				decimal = decimal.split("\\.")[0] + "."
						+ decimal.split("\\.")[1].substring(0, Math.min(decimal.split("\\.")[1].length(), length));
			}
		} else {
			decimal += ".0";
		}
		while (decimal.split("\\.")[1].length() < length)
			decimal += "0";
		return decimal;
	}

	/**
	 * Returns a string for shortened decimal
	 * 
	 * @param decimal Decimal to shorten
	 * @param length  Amount of characters after the .
	 * @return Input: 5978.154123 (Length of 3) Output: "5978.154"
	 */
	public static String parseDecimal(double decimal, int length) {
		return parseDecimal(decimal + "", length);
	}

	public static String parse(Player player, String string) {
		CPlayer cp = plugin.getCPlayer(player);
		return string.replace("%world%", player.getWorld().getName()).replace("%gems%", cp.getSaveInteger("gems") + "")
				.replace("%shards%", cp.getSaveInteger("shards") + "")
				.replace("%rawrank%", MSG.camelCase(cp.getStats().getRank() + ""))
				.replace("%player%", player.getName());
	}

	public static String getLevelColor(int level) {
		if (level >= 80) {
			return "&c";
		} else if (level >= 60) {
			return "&6";
		} else if (level >= 40) {
			return "&2";
		} else if (level >= 20) {
			return "&9";
		} else if (level >= 0) {
			return "&7";
		} else {
			return "&8";
		}
	}

	public static String filter(String msg) {
		String raw = msg;
		for (String word : plugin.getSwears()) {
			char[] letters = raw.toCharArray();
			for (int i = 0; i < raw.length() && i < letters.length; i++) {
				String tmp = "";
				String w = "";
				int p = 0;
				while (p + i < letters.length && tmp.length() < word.length()) {
					tmp += (letters[p + i] + "").replaceAll("[^a-zA-Z]", "").toLowerCase();
					w += letters[p + i] + "";
					p++;
				}

				w = w.trim();
				if (tmp.toLowerCase().contains(word.toLowerCase())) {
					String r = "";
					for (int ii = 0; ii < w.trim().length(); ii++)
						r += "*";
					raw = raw.replace(w.trim(), r);
					break;
				}
			}
		}

		Pattern p = Pattern.compile("(.+\\.(com|net|org|me|edu|info)|[1-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)");
		Matcher m = p.matcher(raw);

		if (m.matches() && !m.group(0).matches("(https:\\/\\/)?(www\\.)?mineplex\\.com")) {
			MSG.announce("group: " + m.group(0));
			String r = "";
			for (int i = 0; i < m.group(0).length(); i++)
				r += "*";
			raw = raw.replace(m.group(0), r);
		}

		String result = "";
		for (char c : raw.toCharArray()) {
			if (c >= 32 && c <= 127)
				result += c;
		}
		return result;
	}
}
