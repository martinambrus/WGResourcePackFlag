package de.mewin.wgtpf;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRemoveProtection extends BukkitRunnable {
	private WGTexturePackFlagPlugin plugin;
    private Player player;

    public PlayerRemoveProtection(WGTexturePackFlagPlugin plugin, Player player) {
    	this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
    	//Bukkit.getLogger().info("removing protection of " + this.player.getName());
    	this.plugin.playerProtection.remove(this.player);
    	this.plugin.playerPackDownloading.put(this.player, false);
    }
}
