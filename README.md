# Clue Scroll Notifier Plugin

This plugin sends a notification or plays a sound when a clue scroll is obtained either by pickpocketing, fishing, woodcutting, or as a drop. 

To ensure it works correctly for monster drops, the in-game setting "Untradeable loot notifications" must be turned on.\
![image](https://github.com/SoaresPT/clue-scroll-notifier/assets/9897471/fd6788bf-38b9-4106-8f15-48a0e6b7b55b)


## Features

- **Sound Alerts:** Plays a sound when a clue scroll or related item is obtained.
- **Desktop Notifications:** Sends a desktop notification if the game is not in the foreground when a clue scroll or related item is obtained.
- **Configurable Settings:**
    - Adjust the volume of the sounds.
    - Toggle sound alerts on or off.
    - Toggle desktop notifications on or off.
    - **Notify on Clue Nests:** Toggle to notify when a bird's nest containing a clue scroll falls from a tree.
    - **Notify on Clue Scroll Drops:** Toggle to notify when a clue scroll drops from a monster or similar.
    - **Notify on Pickpockets:** Toggle to notify when a clue scroll is obtained via pickpocketing.
    - **Notify on Fishing:** Toggle to notify when a clue bottle is caught while fishing.
    - **Notify on Scroll Box Drops:** Toggle to notify when a scroll box drops from a monster (Leagues only). 
- **Custom Sounds:**
  - The sound files used by this plugin are located in your `.runelite` folder under `clue-scroll-notifier`. You can replace these files with your own custom `.wav` files as long as they match the naming convention `clue1.wav` to `clue5.wav`. Simply place your custom sound files in the folder and ensure they are named correctly.

## Credits
Adapted from [https://github.com/jarrodcabalzar/CasketSounds/tree/master](https://github.com/jarrodcabalzar/CasketSounds/tree/master)
