package com.cluescrollnotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("ClueScrollNotifier")
public interface ClueScrollNotifierConfig extends Config
{
	@Range(
			min = 0,
			max = 200
	)
	@ConfigItem(
			keyName = "announcementVolume",
			name = "Sound volume",
			description = "Adjust how loud the sounds are played.",
			position = 1
	)
	default int announcementVolume() {
		return 100;
	}

	@ConfigItem(
			keyName = "playSound",
			name = "Play Sound",
			description = "Toggle to play sound on clue scroll found.",
			position = 0
	)
	default boolean playSound() {
		return true;
	}

	@ConfigItem(
			keyName = "showNotification",
			name = "Show Notification",
			description = "Toggle to show notification on clue scroll found.",
			position = 2
	)
	default boolean showNotification() {
		return true;
	}
}