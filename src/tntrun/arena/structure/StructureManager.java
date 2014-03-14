package tntrun.arena.structure;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class StructureManager {
	
	private String world;

	public String getWorldName() {
		return world;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	private Vector p1 = null;

	public Vector getP1() {
		return p1;
	}

	private Vector p2 = null;

	public Vector getP2() {
		return p2;
	}

	private HashSet<GameLevel> gamelevels = new HashSet<GameLevel>();

	public HashSet<GameLevel> getGameLevels() {
		return gamelevels;
	}

	private int gameleveldestroydelay = 2;

	public int getGameLevelDestroyDelay() {
		return gameleveldestroydelay;
	}

	private LoseLevel loselevel = new LoseLevel();

	public LoseLevel getLoseLevel() {
		return loselevel;
	}

	private Vector spawnpoint = null;

	public Vector getSpawnPointVector() {
		return spawnpoint;
	}
	
	public Location getSpawnPoint() {
		Location spawn = new Location(getWorld(), spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());
		return spawn;
	}

	private int maxPlayers = 6;

	public int getMaxPlayers() {
		return maxPlayers;
	}

	private int minPlayers = 2;

	public int getMinPlayers() {
		return minPlayers;
	}

	private double votesPercent = 0.75;

	public double getVotePercent() {
		return votesPercent;
	}

	private int timelimit = 180;

	public int getTimeLimit() {
		return timelimit;
	}

	private int countdown = 10;

	public int getCountdown() {
		return countdown;
	}

	private Rewards rewards = new Rewards();

	public Rewards getRewards() {
		return rewards;
	}
	
	// arena structure handler
	// main
	public boolean isInArenaBounds(Location loc) {
		if (loc.toVector().isInAABB(getP1(), getP2())) {
			return true;
		}
		return false;
	}

	public String isArenaConfigured() {
		if (getP1() == null ||getP2() == null || world == null) {
			return "Arena bounds not set";
		}
		if (gamelevels.size() == 0) {
			return "Arena gamelevels not set";
		}
		if (!loselevel.isConfigured()) {
			return "Arena looselevel not set";
		}
		if (spawnpoint == null) {
			return "Arena spawnpoint not set";
		}
		return "yes";
	}

	public void setArenaPoints(Location loc1, Location loc2) {
		this.world = loc1.getWorld().getName();
		this.p1 = loc1.toVector();
		this.p2 = loc2.toVector();
	}

	public boolean setGameLevel(String glname, Location loc1, Location loc2) {
		if (isInArenaBounds(loc1) && isInArenaBounds(loc2)) {
			GameLevel gl = getGameLevelByName(glname);
			if (gl == null) {
				gl = new GameLevel(glname);
				gamelevels.add(gl);
			}
			gl.setGameLocation(loc1, loc2);
			return true;
		}
		return false;
	}

	private GameLevel getGameLevelByName(String name) {
		for (GameLevel gl : gamelevels) {
			if (gl.getGameLevelName().equals(name)) {
				return gl;
			}
		}
		return null;
	}

	public void setGameLevelDestroyDelay(int delay) {
		gameleveldestroydelay = delay;
	}

	public boolean setLooseLevel(Location loc1, Location loc2) {
		if (isInArenaBounds(loc1) && isInArenaBounds(loc2)) {
			loselevel.setLooseLocation(loc1, loc2);
			return true;
		}
		return false;
	}

	public boolean setSpawnPoint(Location loc) {
		if (isInArenaBounds(loc)) {
			spawnpoint = loc.toVector();
			return true;
		}
		return false;
	}

	// additional
	public void setMaxPlayers(int maxplayers) {
		this.maxPlayers = maxplayers;
	}

	public void setMinPlayers(int minplayers) {
		this.minPlayers = minplayers;
	}

	public void setVotePercent(double votepercent) {
		this.votesPercent = votepercent;
	}

	public void setTimeLimit(int timelimit) {
		this.timelimit = timelimit;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public void setRewards(ItemStack[] rewards) {
		this.rewards.setRewards(rewards);
	}

	public void setRewards(int money) {
		this.rewards.setRewards(money);
	}
	
	
	
	// arena config handlers
	private File arenafile;
	public void setArenaFile(File file) {
		if (arenafile != null) {
			arenafile = file;
		}
	}

	public void saveToConfig() {
		FileConfiguration config = new YamlConfiguration();
		// save arena bounds
		try {
			config.set("world", world);
			config.set("p1", p1);
			config.set("p2", p2);
		} catch (Exception e) {
		}
		// save gamelevels
		for (GameLevel gl : gamelevels) {
			try {
				gl.saveToConfig(config);
			} catch (Exception e) {
			}
		}
		// save gamelevel destroy delay
		config.set("gameleveldestroydelay", gameleveldestroydelay);
		// save looselevel
		try {
			loselevel.saveToConfig(config);
		} catch (Exception e) {
		}
		// save spawnpoint
		try {
			config.set("spawnpoint", spawnpoint);
		} catch (Exception e) {
		}
		// save maxplayers
		config.set("maxPlayers", maxPlayers);
		// save minplayers
		config.set("minPlayers", minPlayers);
		// save vote percent
		config.set("votePercent", votesPercent);
		// save timelimit
		config.set("timelimit", timelimit);
		// save countdown
		config.set("countdown", countdown);
		// save rewards
		rewards.saveToConfig(config);
		try {
			config.save(arenafile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFromConfig() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(arenafile);
		// load arena world location
		world = config.getString("world", null);
		// load arena bounds
		p1 = config.getVector("p1", null);
		p2 = config.getVector("p2", null);
		// load gamelevels
		ConfigurationSection cs = config.getConfigurationSection("gamelevels");
		if (cs != null) {
			for (String glname : cs.getKeys(false)) {
				try {
					GameLevel gl = new GameLevel(glname);
					gl.loadFromConfig(config);
					gamelevels.add(gl);
				} catch (Exception e) {
				}
			}
		}
		// load gamelevel destroy delay
		gameleveldestroydelay = config.getInt("gameleveldestroydelay", gameleveldestroydelay);
		// load looselevel
		loselevel.loadFromConfig(config);
		// load spawnpoint
		spawnpoint = config.getVector("spawnpoint", null);
		// load maxplayers
		maxPlayers = config.getInt("maxPlayers", maxPlayers);
		// load minplayers
		minPlayers = config.getInt("minPlayers", minPlayers);
		// load vote percent
		votesPercent = config.getDouble("votePercent", votesPercent);
		// load timelimit
		timelimit = config.getInt("timelimit", timelimit);
		// load countdown
		countdown = config.getInt("countdown", countdown);
		// load rewards
		rewards.loadFromConfig(config);
	}
	
}