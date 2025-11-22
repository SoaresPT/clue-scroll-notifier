package com.cluescrollnotifier;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.TileItem;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.Notifier;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import net.runelite.client.events.ConfigChanged;

@Slf4j
@PluginDescriptor(
		name = "Clue Scroll Notifier"
)
public class ClueScrollNotifierPlugin extends Plugin {

	@Inject
	private Client client;
	@Inject
	private ClueScrollNotifierConfig config;
	@Inject
	private SoundEngine soundEngine;
	@Inject
	private ScheduledExecutorService executor;
	@Inject
	private Notifier notifier;

	private static final List<Integer> CLUE_ITEM_IDS = Arrays.asList(
			ItemID.WC_CLUE_NEST_BEGINNER,
			ItemID.WC_CLUE_NEST_EASY,
			ItemID.WC_CLUE_NEST_MEDIUM,
			ItemID.WC_CLUE_NEST_HARD,
			ItemID.WC_CLUE_NEST_ELITE
	);

	@Override
	protected void startUp() throws Exception {
		executor.submit(FileManager::initialize);
		log.info("ClueScrollNotifier started!");
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("ClueScrollNotifier stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		ChatMessageType type = chatMessage.getType();
		String message = chatMessage.getMessage().toLowerCase();

		if (type == ChatMessageType.GAMEMESSAGE && (message.contains("untradeable drop: clue scroll") || message.contains("untradeable drop: scroll box"))) {
			if (config.notifyClueScrollDrops()) {
				notify("Clue scroll drop!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you steal a clue scroll")) {
			if (config.notifyPickpockets()) {
				notify("You stole a clue scroll!");
			}
		}

		if (type == ChatMessageType.SPAM && (message.contains("you catch a clue bottle") || message.contains("you catch a scroll box"))) {
			if (config.notifyFishing()) {
				notify("You caught a clue scroll!");
			}
		}

		if (type == ChatMessageType.SPAM && (message.contains("you find a clue geode") || message.contains("you find a scroll box"))) {
			if (config.notifyMining()) {
				notify("You found a clue scroll!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you sort through the") && message.contains("salvage and find") && message.contains("scroll box")) {
			if (config.notifySalvaging()) {
				notify("You found a scroll box while salvaging!");
			}
		}
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned itemSpawned) {
		TileItem item = itemSpawned.getItem();
		if (config.notifyClueNests() && CLUE_ITEM_IDS.contains(item.getId())) {
			notify("A bird's nest with a clue has fallen out of the tree!");
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		if (!event.getGroup().equals("ClueScrollNotifier")) {
			return;
		}

		if (event.getKey().equals("testSound")) {
			// Play the test sound with current settings
			if (config.playSound()) {
				soundEngine.playClip(config.customSoundFile());
			}
		}
	}

	private void notify(String message) {
		if (config.playSound()) {
			soundEngine.playClip(config.customSoundFile());
		}
		if (config.showNotification()) {
			notifier.notify(message);
		}
	}

	@Provides
	ClueScrollNotifierConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ClueScrollNotifierConfig.class);
	}
}