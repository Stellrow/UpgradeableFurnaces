package ro.Stellrow.UpgradeableFurnaces;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class UpgFurnaceCommands implements CommandExecutor {
    private final UltimateFurnace pl;

    public UpgFurnaceCommands(UltimateFurnace pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String sa, String[] args) {
        if(sender.hasPermission("ultimatefurnace.give")){
            if(args.length==3&&args[0].equalsIgnoreCase("give")){
                Player p = Bukkit.getPlayer(args[1]);
                if(p==null){sender.sendMessage("Player is offline!");
                return true;}
                int amount;
                try{
                    amount = Integer.parseInt(args[2]);
                    ItemStack toGive = pl.furnace.clone();
                    toGive.setAmount(amount);
                    p.getInventory().addItem(toGive);
                }catch (IllegalArgumentException ex){
                    sender.sendMessage("Wrong amount!");
                    return true;
                }
            }
        }
        return true;
    }
}
