package me.nefarious.mobcoins.listener;

import me.nefarious.mobcoins.Main;
import me.nefarious.mobcoins.other.MobCoins;
import me.nefarious.mobcoins.placeholder.PlayerDataHandler;
import me.nefarious.mobcoins.utils.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.*;

public class EntityDeathListener implements Listener {
    private Main plugin;
    private PlayerDataHandler PlayerConfigManager;
    private MobCoins CoinCreation;

    public EntityDeathListener(Main plugin){
        this.plugin = plugin;

        getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent Event){
        if(plugin.getConfig().getBoolean("general.MobCoins_Enabled")){
            if(Event.getEntity().getKiller() instanceof Player){

                double MathRandom = Math.random();
                double dropChance = getDropChance(Event.getEntity().getName());

                getLogger().info("| Name: " + Event.getEntity().getName() + " | Drop Chance (unmodified): " + dropChance + " | Drop Chance (modified): " + (dropChance*100) + "% |");

                if((MathRandom < dropChance)){
                    Player PlayerWhoKilled = Event.getEntity().getKiller();
                    Entity MobWhoDied = (Entity) Event.getEntity();

                    CoinCreation = new MobCoins();
                    ItemStack Coin = CoinCreation.mobCoinObject(1);

                    if(plugin.getConfig().getBoolean("general.MobCoins_MobsDropCoins")) {
                        MobWhoDied.getLocation().getWorld().dropItem(MobWhoDied.getLocation(), Coin);
                    } else {
                        PlayerConfigManager = new PlayerDataHandler();

                        FileConfiguration playerConfig = PlayerConfigManager.fileGet(PlayerWhoKilled);
                        int currentMobCoins = playerConfig.getInt("MobCoins");

                        playerConfig.set("MobCoins", (currentMobCoins + 1));
                        PlayerConfigManager.fileSave(PlayerWhoKilled, playerConfig);
                    }

                    PlayerWhoKilled.sendMessage(Utils.chat("&8[&c*&8] &f You have earned a mob coin from killing a " + MobWhoDied.getName()));
                } else if((plugin.getConfig().getBoolean("general.MobCoins_OnlyUseListedMobs")) && (dropChance == 0.0)){
                    getLogger().info("No coins awarded: this mob was not listed!");
                }
            }
        }
    }

    private double getDropChance(String entity){
        int UserInputNum = (plugin.getConfig().getInt("general.MobCoins_DefaultPercentage"));

        if(!plugin.getConfig().contains("mobs." + entity) && (!plugin.getConfig().getBoolean("general.MobCoins_OnlyUseListedMobs"))){
            return ((double)UserInputNum/100);
        } else if((!plugin.getConfig().contains("mobs." + entity)) && (plugin.getConfig().getBoolean("general.MobCoins_OnlyUseListedMobs"))){
            return 0;
        }
        UserInputNum = (plugin.getConfig().getInt("mobs." + entity));
        return ((double) UserInputNum/100);
    }
}
