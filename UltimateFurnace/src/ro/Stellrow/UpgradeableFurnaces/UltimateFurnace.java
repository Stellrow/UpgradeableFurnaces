package ro.Stellrow.UpgradeableFurnaces;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import ro.Stellrow.UpgradeableFurnaces.sqllitehandling.SQLite;

import java.util.ArrayList;
import java.util.List;

public class UltimateFurnace extends JavaPlugin {

    private UltimateFurnaceManager upgradeableFurnacesManager = new UltimateFurnaceManager(this);

    public NamespacedKey furnaceKey = new NamespacedKey(this, "FurnaceKey");
    private SQLite sqLite = new SQLite(this);
    public ItemStack furnace;

    public void onEnable(){
        loadConfig();
        sqLite.load();
        sqLite.initialize();
        new FastFurnaceEvents(this).init();
        upgradeableFurnacesManager.init();
        getCommand("ultimatefurnace").setExecutor(new UpgFurnaceCommands(this));
        furnace=buildFurnace();
        upgradeableFurnacesManager.start();

        if(getConfig().getBoolean("GeneralConfig.allowCrafting")){
            loadRecipe();
        }

    }
    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void reloadFurnaces(){
        upgradeableFurnacesManager.init();
    }

    public UltimateFurnaceManager getUpgradeableFurnacesManager(){return upgradeableFurnacesManager;}

    public ItemStack buildFurnace(){
        ItemStack is = new ItemStack(Material.FURNACE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',getConfig().getString("ItemConfig.name")));
        List<String> lore = new ArrayList<>();
        for(String s : getConfig().getStringList("ItemConfig.lore")){
            lore.add(ChatColor.translateAlternateColorCodes('&',s));
        }
        im.getPersistentDataContainer().set(furnaceKey, PersistentDataType.STRING,"UltimateFurnace");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    private void loadRecipe(){
        ShapelessRecipe recipe = new ShapelessRecipe(furnaceKey, furnace);
        recipe.addIngredient(Material.FURNACE);
        recipe.addIngredient(5,Material.IRON_INGOT);
        recipe.addIngredient(3,Material.STONE);
        getServer().addRecipe(recipe);
    }

    public SQLite getSqLite(){return sqLite;}


}
