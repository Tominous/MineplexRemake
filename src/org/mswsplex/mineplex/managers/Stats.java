package org.mswsplex.mineplex.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.mswsplex.mineplex.msws.Mineplex;
import org.mswsplex.mineplex.utils.MSG;

public class Stats implements ConfigurationSerializable {
	private CPlayer cp;

	private long playtime;
	private int gems, shards, level;
	private Rank rank;

	private float levelProgress;

	public Stats(CPlayer cp, Mineplex plugin) {
		this.cp = cp;
		playtime = (long) cp.getSaveDouble("stats.playtime");
		gems = (int) cp.getSaveInteger("stats.gems");
		shards = (int) cp.getSaveInteger("stats.shards");
		rank = Rank.valueOf(cp.getSaveString("stats.rank"));
		levelProgress = (float) cp.getSaveDouble("stats.levelprogress");
		level = cp.getSaveInteger("stats.level");
	}

	public long getPlaytime() {
		return playtime;
	}

	public int getGems() {
		return gems;
	}

	public int getShards() {
		return shards;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
		if (cp.getPlayer().isOnline())
			((Player) cp.getPlayer()).setPlayerListName(MSG.color(rank.getPrefix() + cp.getPlayer().getName()));
	}

	public float getLevelProgress() {
		return this.levelProgress;
	}

	public int getCosmeticAmount(String cosmetic) {
		return cp.getSaveInteger("cosmetic." + cosmetic);
	}

	public void saveData() {
		cp.setSaveData("stats", this.serialize());
		cp.saveData();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playtime", (double) playtime);
		data.put("gems", gems);
		data.put("shards", shards);
		data.put("rank", rank + "");
		data.put("levelprogress", levelProgress);
		data.put("level", 0);
		return data;
	}

}
