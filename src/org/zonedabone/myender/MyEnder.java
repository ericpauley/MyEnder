package org.zonedabone.myender;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class MyEnder extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteracr(PlayerInteractEvent e) {
		if (e.isCancelled())
			return;
		if (e.getClickedBlock().getTypeId() == 130) {
			e.setCancelled(true);
			Inventory inv = Bukkit.createInventory(new EnderHolder(e.getPlayer()), 27, e.getPlayer().getDisplayName() + "'s Ender Chest");
			Configuration save = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "data"), e.getPlayer().getName() + ".dat"));
			for (int i = 0; i < 27; i++) {
				inv.setItem(i, save.getItemStack(Integer.toString(i)));
			}
			e.getPlayer().openInventory(inv);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof EnderHolder) {
			Player owner = ((EnderHolder) inv.getHolder()).getOwner();
			FileConfiguration save = new YamlConfiguration();
			for (int i = 0; i < 27; i++) {
				save.set(Integer.toString(i), inv.getItem(i));
			}
			try {
				save.save(new File(new File(getDataFolder(), "data"), e.getPlayer().getName() + ".dat"));
			} catch (IOException e1) {
				owner.sendMessage(ChatColor.RED + "[MyEnder] Your chest didn't save successfully.");
			}
		}
	}
	private class EnderHolder implements InventoryHolder {
		
		private Player owner;
		
		public Player getOwner() {
			return owner;
		}
		
		public EnderHolder(Player owner) {
			this.owner = owner;
		}
		
		@Override
		public Inventory getInventory() {
			return null;
		}
	}
}
