    package me.nefarious.mobcoins;

import me.nefarious.mobcoins.commands.SetMobCoinsCommand;
import me.nefarious.mobcoins.commands.WithdrawMobCoinsCommand;
import me.nefarious.mobcoins.listener.BlockPlaceListener;
import me.nefarious.mobcoins.listener.EntityDeathListener;
import me.nefarious.mobcoins.listener.PlayerInteractionListener;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;

import me.nefarious.mobcoins.listener.PlayerJoinListener;
import me.nefarious.mobcoins.placeholder.PlayerDataHandler;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private PlayerDataHandler PlayerConfigManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        new EntityDeathListener(this);
        new BlockPlaceListener(this);
        new PlayerInteractionListener(this);
        new PlayerJoinListener(this);
        new SetMobCoinsCommand(this);
        new WithdrawMobCoinsCommand(this);


        // To-do: Move this into its own module - bad practice to keep it here
        if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")){
            getLogger().info("Is Enabled");
            PlaceholderAPI.registerPlaceholder(this, "mobcoins",
                    new PlaceholderReplacer() {
                        @Override
                        public String onPlaceholderReplace(PlaceholderReplaceEvent Event){
                            PlayerConfigManager = new PlayerDataHandler();
                            Player onlinePlayer = Event.getPlayer();

                            FileConfiguration playerConfig = PlayerConfigManager.fileGet(onlinePlayer);
                            Integer playerMobCoins = playerConfig.getInt("MobCoins");

                            return String.valueOf(playerMobCoins);
                        }
                    }
            );
        }

    }
}

// MobCoins for Skipzy72
// Author: Nefarious
