package com.cluescrollnotifier;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.TileItem;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

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
	private OkHttpClient okHttpClient;
	@Inject
	private Notifier notifier;

	private static final List<Integer> CLUE_ITEM_IDS = Arrays.asList(
			ItemID.CLUE_NEST_BEGINNER,
			ItemID.CLUE_NEST_EASY,
			ItemID.CLUE_NEST_MEDIUM,
			ItemID.CLUE_NEST_HARD,
			ItemID.CLUE_NEST_ELITE
	);

	@Override
	protected void startUp() throws Exception {
		executor.submit(() -> {
			FileManager.ensureDownloadDirectoryExists();
			FileManager.downloadAllMissingSounds(okHttpClient);
		});
		log.info("ClueScrollNotifier started!");
	}

	@Override
	protected void shutDown() throws Exception {
		soundEngine.close();
		log.info("ClueScrollNotifier stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		ChatMessageType type = chatMessage.getType();
		String message = chatMessage.getMessage().toLowerCase();

		if (type == ChatMessageType.GAMEMESSAGE && message.contains("untradeable drop: clue scroll")) {
			if (config.notifyClueScrollDrops()) {
				notify("Got a clue scroll drop!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you steal a clue scroll")) {
			if (config.notifyPickpockets()) {
				notify("You stole a clue scroll!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you catch a clue bottle")) {
			if (config.notifyFishing()) {
				notify("You caught a clue bottle!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you find a clue geode")) {
			if (config.notifyMining()) {
				notify("You found a clue geode!");
			}
		}

		if (type == ChatMessageType.GAMEMESSAGE && message.contains("untradeable drop: scroll box")) {
			if (config.notifyScrollBoxDrops()) {
				notify("Got a scroll box drop!");
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