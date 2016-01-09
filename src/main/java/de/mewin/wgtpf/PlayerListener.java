package de.mewin.wgtpf;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class PlayerListener implements Listener {

    private WGTexturePackFlagPlugin plugin;

    public PlayerListener(WGTexturePackFlagPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerResourcePackStatusEvent(PlayerResourcePackStatusEvent event) {
    	Player player = event.getPlayer();
    	String eventName = event.getStatus().name();

    	if (eventName.equalsIgnoreCase("declined") || eventName.equalsIgnoreCase("failed_download")) {
    		if (this.plugin.playerProtection.containsKey(player)) {
    			this.plugin.playerProtection.get(player).cancel();
    			this.plugin.playerProtection.remove(player);
    		}

    		this.plugin.playerPackDownloading.put(player, false);

    		if (eventName.equalsIgnoreCase("declined")) {
    			this.plugin.playerHasPack.put(player, false);
    		}
    	} else if (eventName.equalsIgnoreCase("successfully_loaded")) {
    		this.plugin.playerProtection.put(player, new PlayerRemoveProtection(this.plugin, player).runTaskLater(this.plugin, 200));
    	} else if (eventName.equalsIgnoreCase("accepted")) {
    		this.plugin.playerHasPack.put(player, true);
    	}
    }

    // prevent any damage and pushing of the player while loading a resource pack
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent event) {
    	if (this.plugin.playerProtection.size() == 0 && this.plugin.playerPackDownloading.size() == 0) {
    		return;
    	}

    	if (event instanceof EntityDamageByEntityEvent && event.getEntity() instanceof Player) {
    	    EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
    	    Player p = (Player) event.getEntity();
    	    Entity damager = edbeEvent.getDamager();

    	    if (this.plugin.playerProtection.containsKey(p) || (this.plugin.playerPackDownloading.containsKey(p) && this.plugin.playerPackDownloading.get(p).equals(true))) {
    	    	damager.sendMessage(ChatColor.GREEN + "This player is protected while loading a Resource Pack.");
	            event.setCancelled(true);
	        }
    	}
    }

    // remove player from declined list in case they clicked Decline accidentally and want to load the pack
    // on their next login
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogout(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	if (this.plugin.playerHasPack.containsKey(player)) {
    		this.plugin.playerHasPack.remove(player);
    	}
    }
}
