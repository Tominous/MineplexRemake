package org.mswsplex.mineplex.msws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mswsplex.mineplex.commands.DefaultCommand;
import org.mswsplex.mineplex.events.ChatListener;
import org.mswsplex.mineplex.events.OnJoinListener;
import org.mswsplex.mineplex.events.ServerListPingListener;
import org.mswsplex.mineplex.managers.CPlayer;
import org.mswsplex.mineplex.managers.PlayerManager;
import org.mswsplex.mineplex.managers.SBoard;
import org.mswsplex.mineplex.utils.MSG;

public class Mineplex extends JavaPlugin {
	public FileConfiguration config, data, lang, gui;
	public File configYml = new File(getDataFolder(), "config.yml"), dataYml = new File(getDataFolder(), "data.yml"),
			langYml = new File(getDataFolder(), "lang.yml"), guiYml = new File(getDataFolder(), "guis.yml"),
			swearsFile = new File(getDataFolder(), "swears.txt");

	private PlayerManager pManager;

	private List<String> swears;

	public void onEnable() {
		if (!configYml.exists())
			saveResource("config.yml", true);
		if (!langYml.exists())
			saveResource("lang.yml", true);
		if (!guiYml.exists())
			saveResource("guis.yml", true);
		if (!swearsFile.exists())
			saveResource("swears.txt", true);
		config = YamlConfiguration.loadConfiguration(configYml);
		data = YamlConfiguration.loadConfiguration(dataYml);
		lang = YamlConfiguration.loadConfiguration(langYml);
		gui = YamlConfiguration.loadConfiguration(guiYml);

		MSG.plugin = this;
		pManager = new PlayerManager(this);

		new DefaultCommand(this);
		new ServerListPingListener(this);
		new OnJoinListener(this);
		new ChatListener(this);

		new SBoard(this).register();

		boolean save = false;
		for (String section : config.getConfigurationSection("Scoreboard").getKeys(false)) {
			List<String> lines = config.getStringList("Scoreboard." + section);
			if (lines == null || lines.isEmpty())
				continue;
			for (int i = 0; i < lines.size(); i++) {
				String tmp = lines.get(i);
				lines.remove(i);
				while (lines.contains(tmp) || tmp.equals("")) {
					tmp = tmp + " ";
					save = true;
				}
				lines.add(i, tmp);
			}
			if (save) {
				config.set("Scoreboard." + section, lines);
				saveConfig();
			}
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setPlayerListName(
					MSG.color(getPlayerManager().getRank(player).getPrefix() + " &r") + player.getName());
		}

		swears = new ArrayList<>();

		MSG.log("Registering swear words");

		try (BufferedReader br = new BufferedReader(new FileReader(swearsFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				swears.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(swears, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.length() - s1.length();
			}
		});

		MSG.log("Successfully registered " + swears.size() + " swear words.");
	}

	public void saveData() {
		try {
			data.save(dataYml);
		} catch (Exception e) {
			MSG.log("&cError saving data file");
			MSG.log("&a----------Start of Stack Trace----------");
			e.printStackTrace();
			MSG.log("&a----------End of Stack Trace----------");
		}
	}

	public List<String> getSwears() {
		return swears;
	}

	public void saveConfig() {
		try {
			config.save(configYml);
		} catch (Exception e) {
			MSG.log("&cError saving data file");
			MSG.log("&a----------Start of Stack Trace----------");
			e.printStackTrace();
			MSG.log("&a----------End of Stack Trace----------");
		}
	}

	public PlayerManager getPlayerManager() {
		return pManager;
	}

	public CPlayer getCPlayer(Player player) {
		return pManager.getPlayer(player);
	}
}
