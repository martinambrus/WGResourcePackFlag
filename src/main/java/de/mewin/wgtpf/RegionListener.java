/*
 * Copyright (C) 2013 mewin<mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.mewin.wgtpf;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import com.mewin.util.Util;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;

public class RegionListener implements Listener
{
    private WorldGuardPlugin wgp;
    private WGTexturePackFlagPlugin plugin;

    public RegionListener(WGTexturePackFlagPlugin plugin, WorldGuardPlugin wgp)
    {
        this.wgp = wgp;
        this.plugin = plugin;
    }

    @EventHandler
    public void onRegionEntered(RegionEnteredEvent e) {
    	this.updateTexturePack(e.getPlayer());
    }

    @EventHandler
    public void onRegionLeft(RegionLeftEvent e) {
    	this.updateTexturePack(e.getPlayer());
    }

    private void updateTexturePack(Player player) {
    	// nothing to do for a player who's declined resource packs
    	if (this.plugin.playerHasPack.containsKey(player) && this.plugin.playerHasPack.get(player).equals(false)) {
    		return;
    	}

    	if (this.plugin.playerProtection.containsKey(player)) {
    		this.plugin.playerProtection.get(player).cancel();
    		this.plugin.playerProtection.remove(player);
    	}
    	this.plugin.playerPackDownloading.put(player, false);

        @SuppressWarnings({ "deprecation" })
        String tp = (String)Util.getFlagValue(this.wgp, player.getLocation(), (Flag<?>)WGTexturePackFlagPlugin.TEXTUREPACK_FLAG);

        if (player.getMetadata("rgTexture").size() > 0
                && player.getMetadata("rgTexture").get(0).asString().equalsIgnoreCase(tp))
        {
            return;
        }
        else
        {
            player.setMetadata("rgTexture", new FixedMetadataValue(this.plugin, tp));
        }

        if (tp != null)
        {
        	this.plugin.playerPackDownloading.put(player, true);
            player.setResourcePack(tp);
        }
        else
        {
            //player.setTexturePack("");
        }
    }
}