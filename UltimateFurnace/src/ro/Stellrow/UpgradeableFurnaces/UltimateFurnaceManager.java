package ro.Stellrow.UpgradeableFurnaces;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;

import java.util.ArrayList;
import java.util.List;

public class UltimateFurnaceManager {
    private List<Furnace> upgradeableFurnaces = new ArrayList<>();
    private final UltimateFurnace pl;
    private int tickToAdd = 10;

    public UltimateFurnaceManager(UltimateFurnace pl) {
        this.pl = pl;
    }
    public void init(){
        upgradeableFurnaces.clear();
        tickToAdd=pl.getConfig().getInt("GeneralConfig.ticksToAdd");
        for(Furnace furnace : pl.getSqLite().getAllFurnaces()){
            addFurnace(furnace);
        }
    }





    public void addFurnace(Furnace furnace){
        upgradeableFurnaces.add(furnace);
    }
    public void removeFurnace(Furnace furnace){
        upgradeableFurnaces.remove(furnace);
    }

    public void start(){
        Bukkit.getScheduler().runTaskTimer(pl,()->{
            for(Furnace furnace1 : upgradeableFurnaces){
                if(furnace1.getBlock().getType()== Material.AIR){
                    upgradeableFurnaces.remove(furnace1);
                    return;
                }
                Furnace blockData = (Furnace) furnace1.getBlock().getState();
                Furnace furnace = blockData;

                if(furnace.getBurnTime()==0){
                    return;
                }
                if(furnace.getCookTimeTotal()==0){
                    return;
                }

                if(furnace.getCookTimeTotal()<furnace.getCookTime()+tickToAdd){
                    furnace.setCookTime((short) (furnace.getCookTimeTotal()-1));
                    furnace.update(true);
                    return;
                }
                furnace.setCookTime((short) (furnace.getCookTime()+ tickToAdd));
                furnace.setBurnTime((short) (furnace.getBurnTime()-tickToAdd));
                furnace.update(true);
            }
        },0,1);
    }

}
