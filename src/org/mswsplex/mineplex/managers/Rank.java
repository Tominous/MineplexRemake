package org.mswsplex.mineplex.managers;

public enum Rank {
	DEFAULT(""), HERO("&d&lHERO"), ULTRA("&b&lULTRA"), LEGEND("&a&lLEGEND"), TITAN("&C&LTITAN"), ETERNAL("&3&LETERNAL"),
	BUILDER("&9&LBUILDER"), MAPPER("&9&LMAPPER"), YT("&C&LYOUTUBE"), TRAINEE("&6&LTRAINEE"), MOD("&6&LMOD"),
	SRMOD("&6&LSR.MOD"), SUPPORT("&9&lSUPPORT"), DEV("&4&LDEV"), ADMIN("&4&LADMIN"), OWNER("&4&LOWNER");

	private String prefix;

	private Rank(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isDonor() {
		return this.ordinal() >= Rank.DEFAULT.ordinal();
	}

	public boolean isModerationStaff() {
		return this.ordinal() >= Rank.TRAINEE.ordinal();
	}

	public boolean isAdminStaff() {
		return this.ordinal() >= Rank.DEV.ordinal();
	}
	
	public boolean meets(Rank rank) {
		return this.ordinal() >= rank.ordinal();
	}
}
