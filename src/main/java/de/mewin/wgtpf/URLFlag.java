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

import org.bukkit.command.CommandSender;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StringFlag;

public class URLFlag extends StringFlag
{
    public URLFlag(String name)
    {
        super(name);
    }

    public URLFlag(String name, RegionGroup group)
    {
        super(name, group);
    }

    @Override
    public String parseInput(WorldGuardPlugin plugin, CommandSender sender, String input) throws InvalidFlagFormat
    {
        String lower = input.toLowerCase();
        if (!(lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("ftp://") || lower.startsWith("file://")))
        {
            input = "http://" + input;
        }
        return super.parseInput(plugin, sender, input);
    }
}