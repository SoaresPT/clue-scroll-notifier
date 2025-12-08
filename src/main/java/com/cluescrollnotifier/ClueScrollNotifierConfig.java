package com.cluescrollnotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("ClueScrollNotifier")
public interface ClueScrollNotifierConfig extends Config {

	@ConfigSection(
			name = "General Settings",
			description = "General configuration settings",
			position = 0
	)
	String generalSettings = "generalSettings";

	@ConfigSection(
			name = "Notification Settings",
			description = "Notification configuration settings",
			position = 1
	)
	String notificationSettings = "notificationSettings";

	@ConfigSection(
			name = "Debug",
			description = "Debug and testing options",
			position = 2,
			closedByDefault = true
	)
	String debugSettings = "debugSettings";

	@ConfigItem(
			keyName = "playSound",
			name = "Play Sound",
			description = "Toggle to play sound on clue scroll found.",
			position = 0,
			section = generalSettings
	)
	default boolean playSound() {
		return true;
	}

	@ConfigItem(
			keyName = "customSoundFile",
			name = "Custom Sound File",
			description = "Select a custom sound file to play for notifications",
			position = 1,
			section = generalSettings
	)
	default Sound customSoundFile() {
		return Sound.CLUE1;
	}

	@Range(min = 0, max = 200)
	@ConfigItem(
			keyName = "announcementVolume",
			name = "Sound Volume",
			description = "Adjust how loud the sounds are played.",
			position = 1,
			section = generalSettings
	)
	default int announcementVolume() {
		return 100;
	}

	@ConfigItem(
			keyName = "showNotification",
			name = "Show Notification",
			description = "Toggle to send a notification on clue scroll found.",
			position = 2,
			section = generalSettings
	)
	default boolean showNotification() {
		return true;
	}

	@ConfigItem(
			keyName = "notifyClueNests",
			name = "Notify on Clue Nests",
			description = "Toggle to notify on clue nests falling from trees.",
			position = 0,
			section = notificationSettings
	)
	default boolean notifyClueNests() {
		return true;
	}

	@ConfigItem(
			keyName = "notifyClueScrollDrops",
			name = "Notify on Clue Scroll Drops",
			description = "Toggle to notify on clue scroll drops. Make sure Untradeable loot notifications is enabled on the game settings.",
			position = 1,
			section = notificationSettings
	)
	default boolean notifyClueScrollDrops() {
		return true;
	}

	@ConfigItem(
			keyName = "notifyPickpockets",
			name = "Notify on Pickpockets",
			description = "Toggle to notify on pickpocketing clues.",
			position = 2,
			section = notificationSettings
	)
	default boolean notifyPickpockets() {
		return true;
	}

	@ConfigItem(
			keyName = "notifyFishing",
			name = "Notify on Fishing",
			description = "Toggle to notify on getting a clue bottle while fishing.",
			position = 3,
			section = notificationSettings
	)
	default boolean notifyFishing() {
		return true;
	}

	@ConfigItem(
			keyName = "notifyMining",
			name = "Notify on Mining",
			description = "Toggle to notify on getting a clue geode while mining.",
			position = 4,
			section = notificationSettings
	)
	default boolean notifyMining() {
		return true;
	}

	@ConfigItem(
			keyName = "notifySalvaging",
			name = "Notify on Salvaging",
			description = "Toggle to notify on getting a scroll box or clue scroll while sorting salvage.",
			position = 5,
			section = notificationSettings
		)
	default boolean notifySalvaging() {
		return true;
	}

	@ConfigItem(
			keyName = "testSound",
			name = "Test Sound",
			description = "Click to test the selected sound and volume",
			position = 0,
			section = debugSettings
	)
	default boolean testSound() {
		return false;
	}
}