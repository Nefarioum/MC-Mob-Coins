package me.nefarious.mobcoins.listener;

import me.nefarious.mobcoins.Main;
import me.nefarious.mobcoins.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {
    private Main plugin;

    public BlockPlaceListener(Main plugin){
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent Event){
        Player Player = Event.getPlayer();
        ItemStack item = Event.getItemInHand();

        if(item.getItemMeta().getDisplayName().equals(Utils.chat("&6&lMob Coin"))){
            Event.setCancelled(true);
        }
    }
}
