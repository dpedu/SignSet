package me.dpedu.signset;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SignSet extends JavaPlugin {
	
	private Vector<SignSetInstance> sets = new Vector<SignSetInstance>();
	
	public void onEnable() {
		// Load the list of signs we track from the config.yml
		FileConfiguration config = this.getConfig();
		List<Object> setList = (List<Object>) config.getList("signsets");
		for(int i=0;i<setList.size();i++) {
			Map<String,Object> conf = (Map<String,Object>)setList.get(i);
			SignSetInstance x = new SignSetInstance();
			x.url = (String) conf.get("url");
			x.world = (String) conf.get("world");
			x.x1 = (Integer) conf.get("x1");
			x.x2 = (Integer) conf.get("x2");
			x.y1 = (Integer) conf.get("y1");
			x.y2 = (Integer) conf.get("y2");
			x.z1 = (Integer) conf.get("z1");
			x.z2 = (Integer) conf.get("z2");
			x.flipz = (Boolean) conf.get("flipz");
			x.flipx = (Boolean) conf.get("flipx");
			x.freq = ((Integer)conf.get("freq")).longValue();
			sets.add(x);
			// For each set, start a repeating task per the frequency to update the text/signs
			new httpUpdate(this, x).runTaskTimer(this, 1, x.freq); 
		}
	}
	public class httpUpdate extends BukkitRunnable {
		// Helper class for performing time updates
		private final JavaPlugin plugin;
		SignSetInstance instance;
	    public httpUpdate(JavaPlugin plugin, SignSetInstance i) {
	        this.plugin = plugin;
	        this.instance = i;
	    }
	    public void run() {
	    	// Simply call the update method of the object representing a set of signs.
	    	this.instance.updateContent(this.plugin);
	    }
	}
}
