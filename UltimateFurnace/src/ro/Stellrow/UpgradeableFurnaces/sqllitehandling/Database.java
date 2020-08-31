package ro.Stellrow.UpgradeableFurnaces.sqllitehandling;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import ro.Stellrow.UpgradeableFurnaces.UltimateFurnace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class Database {
    UltimateFurnace plugin;
    Connection connection;
    public String table = "ultimateFurnaces";
    public Database(UltimateFurnace instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }


    public List<Furnace> getAllFurnaces(){
        List<Furnace> toReturn = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM "+table);
            rs = ps.executeQuery();
            while(rs.next()){
                String world;
                int x;
                int y;
                int z;
                world = rs.getString("world");
                x = rs.getInt("x");
                y = rs.getInt("y");
                z = rs.getInt("z");
                Block b = new Location(Bukkit.getWorld(world),x,y,z).getBlock();
                if(b.getType()!= Material.FURNACE){

                }else {
                    toReturn.add((Furnace) b.getState());
                }
            }

        } catch (SQLException e) {

        }finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return toReturn;
    }
    // Now we need methods to save things to the database
    public void addFurnace(Furnace toAdd){
        Location loc = toAdd.getLocation();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + table + " (world,x,y,z) VALUES(?,?,?,?)");
            ps.setString(1,toAdd.getBlock().getWorld().getName());
            ps.setInt(2,loc.getBlockX());
            ps.setInt(3,loc.getBlockY());
            ps.setInt(4,loc.getBlockZ());
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void removeFurnace(Furnace toRemove){
        Location loc = toRemove.getLocation();
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM "+table +" WHERE world=? AND x=? AND y=? AND z=?");
            ps.setString(1,loc.getWorld().getName());
            ps.setInt(2,loc.getBlockX());
            ps.setInt(3,loc.getBlockY());
            ps.setInt(4,loc.getBlockZ());
            ps.executeUpdate();
            return;

        } catch (SQLException e) {
        }finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }
    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}