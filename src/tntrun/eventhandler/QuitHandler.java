/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package tntrun.eventhandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class QuitHandler implements Listener {

	private TNTRun plugin;
	public QuitHandler(TNTRun plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
		Arena arena = plugin.pdata.getPlayerArena(player.getName());
		//ignore if player is not in arena
		if (arena == null) {return;}
		arena.leavePlayer(player);
	}

	
	
}