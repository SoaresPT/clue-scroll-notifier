package com.cluescrollnotifier;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;

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

		if (type == ChatMessageType.GAMEMESSAGE && message.contains("a bird's nest falls out of the tree")) {
			log.info("Received chat message: '{}' of type {}", chatMessage.getMessage(), type);
			if (config.notifyClueNests()) {
				notify("A bird's nest with a clue has fallen out of the tree!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you catch a clue bottle")) {
			log.info("Received chat message: '{}' of type {}", chatMessage.getMessage(), type);
			if (config.notifyFishing()) {
				notify("You caught a clue bottle!");
			}
		}

		if (type == ChatMessageType.SPAM && message.contains("you find a clue geode")) {
			log.info("Received chat message: '{}' of type {}", chatMessage.getMessage(), type);
			if (config.notifyMining()) {
				notify("You found a clue geode!");
			}
		}
	}

	private void notify(String message) {
		if (config.playSound()) {
			playRandomClueScrollSound();
		}
		if (config.showNotification()) {
			notifier.notify(message);
		}
	}

	private void playRandomClueScrollSound() {
		int whichSound = ThreadLocalRandom.current().nextInt(1, 3);
		switch (whichSound) {
			case 2:
				log.info("Playing CLUE2 sound.");
				soundEngine.playClip(Sound.CLUE2);
				break;
			default:
				log.info("Playing default CLUE sound.");
				soundEngine.playClip(Sound.CLUE);
		}
	}

	@Provides
	ClueScrollNotifierConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ClueScrollNotifierConfig.class);
	}
}