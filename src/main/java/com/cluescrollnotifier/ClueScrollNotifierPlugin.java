package com.cluescrollnotifier;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
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
		name = "Clue Scroll Sounds"
)
public class ClueScrollNotifierPlugin extends Plugin
{
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
	protected void startUp() throws Exception
	{
		executor.submit(() -> {
			FileManager.ensureDownloadDirectoryExists();
			FileManager.downloadAllMissingSounds(okHttpClient);
		});

		log.info("ClueScrollNotifier started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		soundEngine.close();
		log.info("ClueScrollNotifier stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getMessage().toLowerCase().contains("clue scroll")) {
			if (config.playSound()) {
				playRandomClueScrollSound();
			}
			if (config.showNotification()) {
				notifier.notify("Clue scroll found!");
			}
		}
	}

	private void playRandomClueScrollSound() {
		int whichSound = ThreadLocalRandom.current().nextInt(1, 3);

		switch(whichSound)
		{
			case(2):
				log.info("Playing CASKET2 sound.");
				soundEngine.playClip(Sound.CASKET2);
				break;
			default:
				log.info("Playing default CASKET sound.");
				soundEngine.playClip(Sound.CASKET);
		}
	}

	@Provides
	ClueScrollNotifierConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ClueScrollNotifierConfig.class);
	}
}