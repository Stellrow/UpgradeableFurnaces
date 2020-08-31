package ro.Stellrow.UpgradeableFurnaces.sqllitehandling;

import ro.Stellrow.UpgradeableFurnaces.UltimateFurnace;

import java.util.logging.Level;

public class Error {
    public static void execute(UltimateFurnace plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(UltimateFurnace plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
