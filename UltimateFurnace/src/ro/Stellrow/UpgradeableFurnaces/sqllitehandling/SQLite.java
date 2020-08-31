package ro.Stellrow.UpgradeableFurnaces.sqllitehandling;

import ro.Stellrow.UpgradeableFurnaces.UltimateFurnace;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database{
    String dbname;
    public SQLite(UltimateFurnace instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "ultimateFurnaces");
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS ultimateFurnaces (" +
            "`uuid` INTEGER PRIMARY KEY," +
            "`world` varchar(50) NOT NULL," +
            "`x` INT NOT NULL," +
            "`y` INT NOT NULL," +
            "`z` INT NOT NULL" +
            ");";


    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}