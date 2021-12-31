package me.nefarious.mobcoins.commands;

import me.nefarious.mobcoins.Main;
import me.nefarious.mobcoins.placeholder.PlayerDataHandler;
import me.nefarious.mobcoins.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetMobCoinsCommand implements CommandExecutor {
    private Main plugin;
    private PlayerDataHandler PlayerConfigManager;

    public SetMobCoinsCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("setmobcoins").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!(sender instanceof Player)){
            // To-do: Add console commands for easier mob-coins handling
            sender.sendMessage("This command cannot be executed from the console! - feature coming soon tho");
            return false;
        }
        Player player = (Player) sender;

        if(player.hasPermission("setmobcoins.use")){
            if(args.length == 2){
                Player playerToExecuteCommand = Bukkit.getPlayer(args[0]);
                try {
                    try {
                        Integer coinsAmount = Integer.parseInt(args[1]);
                        PlayerConfigManager = new PlayerDataHandler();
                        FileConfiguration playerConfig = PlayerConfigManager.fileGet(playerToExecuteCommand);
                        playerConfig.set("MobCoins", coinsAmount);
                        PlayerConfigManager.fileSave(playerToExecuteCommand, playerConfig);

                        sender.sendMessage(Utils.chat("&a[*] &fThe player " + playerToExecuteCommand.getName() + " mobcoins is now set to: " + coinsAmount));
                    } catch(NumberFormatException NumberEvent){
                        player.sendMessage(Utils.chat("&4[*] &fPlease enter a valid number to set balance!"));
                    }
                } catch(NullPointerException Event){
                    player.sendMessage(Utils.chat("&4[*] &fThe player " + args[0] + " could not be found!"));

                }
                return true;
            } else if(args.length <= 1){
                sender.sendMessage(Utils.chat("&4[*] &cWrong Usage: &f/"+ string +" <name> <amount>"));
                return false;
            }
        } else {
            player.sendMessage(Utils.chat("&4[*] &fYou do not have permission to use this command!"));
        }
        return false;
    }
}

