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

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public class WGTexturePackFlagPlugin extends JavaPlugin
{
    public static final URLFlag TEXTUREPACK_FLAG = new URLFlag("texturepack");
    public Map<Player, BukkitTask> playerProtection = new HashMap<Player, BukkitTask>();
    public Map<Player, Boolean> playerHasPack = new HashMap<Player, Boolean>();
    public Map<Player, Boolean> playerPackDownloading = new HashMap<Player, Boolean>();
    private WGCustomFlagsPlugin wgcf;
    private WorldGuardPlugin wgp;
    private RegionListener listener;
    private PlayerListener listener2;
    
    @Override
    public void onEnable()
    {
        if (!this.getWGCF())
        {
        	this.getLogger().log(Level.SEVERE, "Could not find WGCustomFlags.");
        	this.getPluginLoader().disablePlugin(this);
            return;
        }
        else
        {
        	this.getLogger().log(Level.INFO, "Hooked into WGCustomFlags.");
        }
        
        if (!this.getWorldGuard())
        {
        	this.getLogger().log(Level.SEVERE, "Could not find WorldGuard.");
        	this.getPluginLoader().disablePlugin(this);
            return;
        }
        else
        {
        	this.getLogger().log(Level.INFO, "Hooked into WorldGuard.");
        }
        
        this.listener = new RegionListener(this, this.wgp);
        this.listener2 = new PlayerListener(this);
        this.getServer().getPluginManager().registerEvents(this.listener, this);
        this.getServer().getPluginManager().registerEvents(this.listener2, this);
        
        this.wgcf.addCustomFlag(TEXTUREPACK_FLAG);
    }
    
    private boolean getWGCF()
    {
        Plugin plug = this.getServer().getPluginManager().getPlugin("WGCustomFlags");
        if (plug == null || !(plug instanceof WGCustomFlagsPlugin))
        {
            return false;
        }
        else
        {
        	this.wgcf = (WGCustomFlagsPlugin) plug;
            return true;
        }
    }
    
    private boolean getWorldGuard()
    {
        Plugin plug = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plug == null || !(plug instanceof WorldGuardPlugin))
        {
            return false;
        }
        else
        {
        	this.wgp = (WorldGuardPlugin) plug;
            return true;
        }
    }
}