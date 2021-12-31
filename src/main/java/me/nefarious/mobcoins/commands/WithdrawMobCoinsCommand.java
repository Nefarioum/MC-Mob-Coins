package me.nefarious.mobcoins.commands;

import me.nefarious.mobcoins.Main;
import me.nefarious.mobcoins.other.MobCoins;
import me.nefarious.mobcoins.placeholder.PlayerDataHandler;
import me.nefarious.mobcoins.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WithdrawMobCoinsCommand implements CommandExecutor {
    private Main plugin;
    private PlayerDataHandler PlayerConfigManager;
    private MobCoins CoinCreation;

    public WithdrawMobCoinsCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("withdrawmobcoins").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be executed from the console!");
            return false;
        }
        Player player = (Player) sender;

        if(player.hasPermission("withdrawmobcoins.use")){
            if(args.length == 1){
                try{
                    PlayerConfigManager = new PlayerDataHandler();
                    FileConfiguration playerConfig = PlayerConfigManager.fileGet(player);

                    Integer coinsAmountToWithdraw = Integer.parseInt(args[0]);
                    Integer coinsAmountInStorage = playerConfig.getInt("MobCoins");

                    if(coinsAmountToWithdraw <= coinsAmountInStorage){
                        if(Integer.signum(coinsAmountToWithdraw) == -1) { player.sendMessage(Utils.chat("&4[*] &fYou cannot withdraw a negative amount of coins!")); return false;}
                        Integer remainingBalance = (coinsAmountInStorage - coinsAmountToWithdraw);

                        CoinCreation = new MobCoins();
                        ItemStack Coin = CoinCreation.mobCoinObject(coinsAmountToWithdraw);

                        Integer inventorySlotsFree = 0;
                        for (ItemStack itemStack : player.getInventory().getContents()) {
                            if (itemStack == null) {
                                inventorySlotsFree += Coin.getType().getMaxStackSize();
                            } else if (itemStack.getItemMeta().getDisplayName().equals(Coin.getItemMeta().getDisplayName())) {
                                inventorySlotsFree += itemStack.getType().getMaxStackSize() - itemStack.getAmount();
                            }
                        }

                        inventorySlotsFree = inventorySlotsFree - (64*5);

                        if(Coin.getAmount() <= inventorySlotsFree) {
                            player.getInventory().addItem(Coin);
                            sender.sendMessage(Utils.chat("&a[*] &fYou have withdrawn " + coinsAmountToWithdraw + " mobcoins. Your balance is now " + remainingBalance));

                            playerConfig.set("MobCoins", remainingBalance);
                            PlayerConfigManager.fileSave(player, playerConfig);

                            return true;

                        } else {

                            ItemStack modifiedCoin = CoinCreation.mobCoinObject(inventorySlotsFree);
                            Integer coinsRemaining = coinsAmountToWithdraw - inventorySlotsFree;

                            playerConfig.set("MobCoins", (remainingBalance + coinsRemaining));
                            PlayerConfigManager.fileSave(player, playerConfig);

                            sender.sendMessage(Utils.chat("&a[*] &fYou do not have enough space in your inventory to hold all these coins. " + coinsRemaining + " has been sent back to your storage."));

                            player.getInventory().addItem(modifiedCoin);

                            return true;
                        }

                    } else {
                        player.sendMessage(Utils.chat("&4[*] &fYou cannot withdraw more mob coins than you have!"));
                        return false;
                    }

                } catch(NumberFormatException numberEvent) {
                    player.sendMessage(Utils.chat("&4[*] &fPlease enter a valid number to withdraw!"));
                }
                return false;
            }
                sender.sendMessage(Utils.chat("&4[*] &cWrong Usage: &f/"+ string +" <amount>"));
                return false;
        } else {
            player.sendMessage(Utils.chat("&4[*] &fYou do not have permission to use this command!"));
            return false;
        }
    }
}
