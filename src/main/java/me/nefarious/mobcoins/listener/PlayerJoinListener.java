package me.nefarious.mobcoins.listener;

import me.nefarious.mobcoins.Main;
import me.nefarious.mobcoins.placeholder.PlayerDataHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getPluginManager;

public class PlayerJoinListener implements Listener{
    private Main plugin;
    private PlayerDataHandler PlayerConfigManager;

    public PlayerJoinListener(Main plugin){
        this.plugin = plugin;

        getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent Event){
        PlayerConfigManager = new PlayerDataHandler();
        PlayerConfigManager.fileSetup(Event.getPlayer());

        }
    }

