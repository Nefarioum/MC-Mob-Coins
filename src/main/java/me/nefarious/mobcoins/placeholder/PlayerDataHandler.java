package me.nefarious.mobcoins.placeholder;

import me.nefarious.mobcoins.Main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerDataHandler {
    private Main plugin = Main.getPlugin(Main.class);

    public FileConfiguration playerConfig;
    public File playerFolder;
    public File playerTemplate;

    public void fileSetup(Player player){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        playerFolder = new File(plugin.getDataFolder(), "plr-db");

        if(!playerFolder.exists()){
            playerFolder.mkdir();
        }

        playerTemplate = new File(playerFolder, player.getUniqueId() + ".yml");

        if(!playerTemplate.exists()){
            try{
                playerTemplate.createNewFile();
            } catch (IOException Event) {
                Bukkit.getLogger().info("Failed to create player file.. check if there is permissions?");
            }
        }

        playerConfig = YamlConfiguration.loadConfiguration(playerTemplate);

        playerConfig.addDefault("MobCoins", 0);
        playerConfig.options().copyDefaults(true);
        fileSave(player, playerConfig);
    }

    public FileConfiguration fileGet(Player player){
        playerTemplate = new File(plugin.getDataFolder() + "/plr-db", player.getUniqueId() + ".yml");

        if(playerTemplate.exists()){
            return YamlConfiguration.loadConfiguration(playerTemplate);
        } else {
        }
        return playerConfig;
    }

    public void fileSave(Player player, FileConfiguration alteredConfig){
        playerTemplate = new File(plugin.getDataFolder() + "/plr-db", player.getUniqueId() + ".yml");

        if(playerTemplate.exists()){
            try{
                alteredConfig.save(playerTemplate);
            } catch (IOException Event) {
                Bukkit.getLogger().info("Failed to save player file..");
            }
        }
    }
}
