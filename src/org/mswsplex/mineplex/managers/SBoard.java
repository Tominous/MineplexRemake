package org.mswsplex.mineplex.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class SBoard {
	Scoreboard board;

	double tick = 0, spd = 1, changed = 0;
	int length = 25;
	List<String> sLines = new ArrayList<String>();
	String name = "", prefix = "";

	Runtime runtime = Runtime.getRuntime();

	private Mineplex plugin;

	ConfigurationSection scoreboard;

	public SBoard(Mineplex plugin) {
		this.plugin = plugin;
		scoreboard = plugin.config.getConfigurationSection("Scoreboard");
	}

	public void refresh() {
		sLines = flip(plugin.config.getStringList("Scoreboard.Lines"));
		length = plugin.config.getInt("Scoreboard.Length");
		prefix = plugin.config.getString("Scoreboard.Prefix");
		spd = plugin.config.getDouble("Scoreboard.Speed");
		System.gc();
	}

	public void register() {
		refresh();
		PlayerManager pManager = plugin.getPlayerManager();
		new BukkitRunnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					CPlayer cp = pManager.getPlayer(player);
					if (cp.hasTempData("scoreboard") && !cp.getTempBoolean("scoreboard"))
						continue;
					name = plugin.config.getString("Scoreboard.Title");
					List<String> lines = new ArrayList<String>();
					board = player.getScoreboard();
					// Anti Lag/Flash Scoreboard functions
					if (board != null && player.getScoreboard().getObjective("mineplex") != null
							&& cp.hasTempData("oldLines")) {
						if (board.getObjectives().size() > sLines.size())
							continue;
						Objective obj = board.getObjective("mineplex");
						List<String> oldLines = (List<String>) cp.getTempData("oldLines");
						for (int i = 0; i < 15 && i < sLines.size() && i < oldLines.size(); i++) {
							String sLine = MSG.parse(player, sLines.get(i)), nLine = "";
							if (sLine.startsWith("scroll")) {
								sLine = sLine.substring("scroll".length());
								nLine = sLine.substring((int) (tick % sLine.length()))
										+ sLine.substring(0, (int) (tick % sLine.length()));
								nLine = nLine.substring(0, Math.min(18, nLine.length()));
							} else {
								nLine = sLine.substring(0, Math.min(40, sLine.length()));
							}
							lines.add(nLine);
							if (board.getEntries().contains(nLine))
								continue;
							board.resetScores(MSG.parse(player, oldLines.get(i)));
							obj.getScore(MSG.color(nLine)).setScore(i + 1);
						}
						name = MSG.parse(player, name);
						String disp = "";
						cp.setTempData("oldLines", lines);
						if (name.length() > length) {
							disp = name.substring((int) (tick % name.length()))
									+ name.substring(0, (int) (tick % name.length()));
							disp = disp.substring(0, Math.min(length, disp.length()));
						} else {
							disp = name;
						}
						// obj.setDisplayName(MSG.color(prefix+disp));
						obj.setDisplayName(MSG.color(prefix+animate(disp, tick)));
					} else {
						board = Bukkit.getScoreboardManager().getNewScoreboard();
						Objective obj = board.registerNewObjective("mineplex", "dummy");
						player.setScoreboard(board);
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						int pos = 1;
						for (String res : sLines) {
							String line = MSG.parse(player, res);
							line = line.substring(0, Math.min(40, line.length()));
							obj.getScore(MSG.color(line)).setScore(pos);
							lines.add(line);
							if (pos >= 15 || pos >= sLines.size())
								break;
							pos++;
						}
						cp.setTempData("oldLines", lines);
					}
					if (board.getEntries().size() > sLines.size())
						refresh(player);
				}
				tick += spd;
			}
		}.runTaskTimer(plugin, 0, 1);

	}

	public String animate(String line, double time) {
		int pos = (int) Math.floor((time * 1) % line.length());
		String name = line;
		name = line.substring(0, pos) + line.substring(pos);
		if (pos == line.length() - 1)
			name = line;
		return name;
	}

	private List<String> flip(List<String> array) {
		List<String> result = new ArrayList<String>();
		for (int i = array.size() - 1; i >= 0; i--) {
			result.add(array.get(i));
		}
		return result;
	}

	private void refresh(Player player) {
		for (String res : board.getEntries()) {
			boolean keep = false;
			for (String line : sLines) {
				if (MSG.parse(player, res).equals(MSG.parse(player, line)))
					keep = true;
			}
			if (!keep)
				board.resetScores(res);
		}
	}
}