package tntrun.arena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class GameLevel {

	private Vector sandp1 = null;
	private Vector sandp2 = null;

	private Vector tntp1 = null;
	private Vector tntp2 = null;
	
	
	protected boolean isConfigured()
	{
		if (sandp1 != null && sandp2 != null && tntp1 != null && tntp2 != null)
		{
			return true;
		}
		return false;
	}
	
	
	protected boolean isSandLocation(Location loc)
	{
		if (loc.toVector().isInAABB(sandp1.add(new Vector(0,-1,0)), sandp2.add(new Vector(0,1,0))))
		{
			return true;
		}
		return false;
	};
	
	protected void destroyBlock(Location loc, World w)
	{
		w.getBlockAt(loc).setType(Material.AIR);
		w.getBlockAt(loc.add(0,-1,0)).setType(Material.AIR);
	}
	
	
	protected void setGameLocation(Location p1, Location p2, World w)
	{
		this.sandp1 = p1.add(0, 1, 0).toVector();
		this.sandp2 = p2.toVector();
		
		this.tntp1 = p1.toVector();
		this.tntp2 = p2.add(0, -1, 0).toVector();
		fillArea(w);
	}
	
	protected void regenGameLocation(World w)
	{
		fillArea(w);
	}
	
	private void fillArea(World w)
	{
		int y = tntp1.getBlockY();
		for (int x = tntp1.getBlockX(); x<=tntp2.getBlockX(); x++)
		{
			for (int z = tntp1.getBlockZ(); z<=tntp2.getBlockZ(); z++)
			{
				w.getBlockAt(x, y, z).setType(Material.TNT);
			}
		}
		y = sandp1.getBlockY();
		for (int x = sandp1.getBlockX(); x<=sandp2.getBlockX(); x++)
		{
			for (int z = sandp1.getBlockZ(); z<=sandp2.getBlockZ(); z++)
			{
				w.getBlockAt(x, y, z).setType(Material.SAND);
			}
		}
	}
	
	

	protected void saveToConfig(String arenaname, String levelname, FileConfiguration config)
	{
		
	}
	
	protected void loadFromConfig(String arenaname, String levelname, FileConfiguration config)
	{
		
	}
	
}