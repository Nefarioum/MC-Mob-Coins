package me.nefarious.mobcoins.listener;

import com.mysql.jdbc.Util;
import me.nefarious.mobcoins.Main;

import me.nefarious.mobcoins.other.MobCoins;
import me.nefarious.mobcoins.placeholder.PlayerDataHandler;
import me.nefarious.mobcoins.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInteractionListener implements Listener {
    private Main plugin;
    private PlayerDataHandler PlayerConfigManager;
    private MobCoins CoinCreation;

    public PlayerInteractionListener(Main plugin){
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent Event){
        Player player = Event.getPlayer();
        Action playerAction = Event.getAction();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack playerItem = playerInventory.getItemInMainHand();

        if(playerItem == null || playerItem.getType() == Material.AIR) return;


        if(playerAction.equals(Action.RIGHT_CLICK_AIR) && (playerItem.getItemMeta().getDisplayName().equals("§6§lMob Coin"))){
            if(player.getGameMode().equals(GameMode.SURVIVAL)){
                PlayerConfigManager = new PlayerDataHandler();
                if(plugin.getConfig().getBoolean("MobCoins_DepositStack")) {
                    player.sendMessage(Utils.chat("&4[*] &fYou have redeemed " + playerItem.getAmount() + " mobcoin(s)."));

                    FileConfiguration playerConfig = PlayerConfigManager.fileGet(player);
                    playerConfig.set("MobCoins", (playerConfig.getInt("MobCoins") + playerItem.getAmount()));
                    PlayerConfigManager.fileSave(player, playerConfig);

                    player.getInventory().removeItem(player.getInventory().getItemInMainHand());
                } else {
                    CoinCreation = new MobCoins();
                    ItemStack Coin = CoinCreation.mobCoinObject(1);

                    Integer inventoryCoins = 0;
                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if ((itemStack != null) && (itemStack.getItemMeta().getDisplayName().equals(Coin.getItemMeta().getDisplayName()))){
                            inventoryCoins += itemStack.getAmount();
                            player.getInventory().removeItem(itemStack);
                        }
                    }
                    player.sendMessage(Utils.chat("&4[*] &fYou have redeemed " + inventoryCoins + " mobcoin(s)."));

                    FileConfiguration playerConfig = PlayerConfigManager.fileGet(player);
                    playerConfig.set("MobCoins", (playerConfig.getInt("MobCoins") + inventoryCoins));
                    PlayerConfigManager.fileSave(player, playerConfig);
                }
            } else {
                player.sendMessage(Utils.chat("&4[*] &fMob coins can only be redeemed in Survival Mode!"));
            }
        }
    }
}
