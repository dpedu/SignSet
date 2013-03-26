package me.dpedu.signset;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SignSetInstance {
	
	// Properties of this sign set
	public String url;
	public String world;
	public int x1,x2,y1,y2,z1,z2;
	public long freq;
	boolean flipx;
	boolean flipz;
	// Content of last time we hit the url
	public String lastFetch;
	
	public boolean hasUnplacedChanges = false;
	
	public SignSetInstance() {
		
	}
	
	public void updateContent(JavaPlugin p) {
		String nc = null;
		// Download text from the url
		try {
			nc = getUrl(this.url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If the new text isn't null (an error) and has changed from before, make note
		if(nc!=null && !nc.equals(lastFetch)) {
			this.lastFetch=nc;
			hasUnplacedChanges = true;
		}
		// Update the sign text one tick from now
		BukkitTask task2 = new worldUpdate(p, this).runTaskLater(p, 1);
	}
	
	public class worldUpdate extends BukkitRunnable {
		// Helper class for timed sign updates
		private final JavaPlugin plugin;
		SignSetInstance instance;
	    public worldUpdate(JavaPlugin plugin, SignSetInstance i) {
	        this.plugin = plugin;
	        this.instance = i;
	    }
	    public void run() {
	    	// Call the SignSetInstance's method to apply it's changes
	    	this.instance.applyToWorld(this.plugin);
	    }
	}
	
	public String getUrl(String url) throws Exception {
		// Return the contents from a url.
        URL targetUrl = new URL(url);
        URLConnection yc = targetUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        String data = "";
        while ((inputLine = in.readLine()) != null) 
           data = data + inputLine+"\n";
        in.close();
        return data;
    }
	public void applyToWorld(JavaPlugin p) {
		if(!hasUnplacedChanges) {
			// No changes, skip
			return;
		}
		Server s = p.getServer();
		World w = s.getWorld(this.world);
		// Split the lines into an array, and replace "&&" with the minecraft color code
		String[] lines = this.lastFetch.replace("&&", "¤").split("\n");
		int linePoint = 0;
		
		// Depending on what direction we need to go in, apply changes.
		if(flipx && flipz) {
			for(int y=y2;y>=y1;y--) {
				for(int x=x2;x>=x1;x--) {
					for(int z=z2;z>=z1;z--) {
						linePoint = applyTextToSign(w, x, y, z, lines, linePoint);
					}
				}
			}
		} else if(flipx && !flipz) {
			for(int y=y2;y>=y1;y--) {
				for(int x=x2;x>=x1;x--) {
					for(int z=z1;z<=z2;z++) {
						linePoint = applyTextToSign(w, x, y, z, lines, linePoint);
					}
				}
			}
		} else if(!flipx && flipz) {
			for(int y=y2;y>=y1;y--) {
				for(int x=x1;x<=x2;x++) {
					for(int z=z2;z>=z1;z--) {
						linePoint = applyTextToSign(w, x, y, z, lines, linePoint);
					}
				}
			}
		} else {
			for(int y=y2;y>=y1;y--) {
				for(int x=x1;x<=x2;x++) {
					for(int z=z1;z<=z2;z++) {
						linePoint = applyTextToSign(w, x, y, z, lines, linePoint);
					}
				}
			}
		}
	}
	
	public int applyTextToSign(World w, int x, int y, int z, String[] text, int startIndex) {
		// Fetch the block
		Block b = w.getBlockAt(x, y, z);
		// Skip it if it's not a sign.
		if(b.getType()!=Material.SIGN && b.getType()!=Material.SIGN_POST && b.getType()!=Material.WALL_SIGN) {
			return startIndex;
		}
		Sign sign = (Sign)b.getState();
		
		// Create the new String[4] array of the four lines.
		String[] newLines = new String[]{"", "", "", ""};
		for(int i=0;i<4;i++) {
			sign.setLine(i, "");
		}
		// Copy lines from our text array into the sign's array
		for(int i=0;i<4&&startIndex<text.length;i++) {
			sign.setLine(i, text[startIndex]);
			startIndex++;
		}
		// Update the sign
		sign.update();
		// return our progress in the list of lines
		return startIndex;
	}
	
}








