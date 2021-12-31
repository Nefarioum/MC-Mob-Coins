package me.nefarious.mobcoins.other;

import me.nefarious.mobcoins.Main;
import me.nefarious.mobcoins.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MobCoins {
    private Main plugin = Main.getPlugin(Main.class);

    public ItemStack mobCoinObject(Integer coinAmount){
        ItemStack Coin = new ItemStack(Material.SUNFLOWER, coinAmount);

        ItemMeta CoinMeta = Coin.getItemMeta();
        CoinMeta.setDisplayName(Utils.chat("&6&lMob Coin"));

        ArrayList CoinLore = new ArrayList();

        CoinLore.add(ChatColor.WHITE + " Right click to redeem MobCoin! (to-do) ");
        CoinMeta.setLore(CoinLore);

        Coin.setItemMeta(CoinMeta);
        return Coin;
    }
}
