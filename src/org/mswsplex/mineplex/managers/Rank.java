package org.mswsplex.mineplex.managers;

public enum Rank {
	DEFAULT("&r"), HERO("&d&lHERO &r"), ULTRA("&b&lULTRA &r"), LEGEND("&a&lLEGEND &r"), TITAN("&C&LTITAN &r"), ETERNAL("&3&LETERNAL &r"),
	BUILDER("&9&LBUILDER &r"), MAPPER("&9&LMAPPER &r"), YT("&C&LYOUTUBE &r"), TRAINEE("&6&LTRAINEE &r"), MOD("&6&LMOD &r"),
	SRMOD("&6&LSR.MOD &r"), SUPPORT("&9&lSUPPORT &r"), DEV("&4&LDEV &r"), ADMIN("&4&LADMIN &r"), OWNER("&4&LOWNER &r");

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
