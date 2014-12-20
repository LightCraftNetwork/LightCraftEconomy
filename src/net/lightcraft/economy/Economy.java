package net.lightcraft.economy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Economy extends JavaPlugin{

	private static Economy main;
	private Connection connection;
	private MySQL mysql;
	private PreparedStatement tokens, coins, add;

	public void onEnable(){
		main = this;
		setupConnection();
		createSQLTables();
		createPreparedStatements();
	}

	private void setupConnection(){
		this.mysql = new MySQL(this, new Configuration(this, "mysql.yml"));
		getConnection();
	}

	private void createSQLTables(){
		try{
			getConnection().prepareStatement("CREATE TABLE `coins` (`uuid` VARCHAR(36) NOT NULL,`coins` INT NOT NULL DEFAULT '0');").execute();
			getConnection().prepareStatement("CREATE TABLE `tokens` (`uuid` VARCHAR(36) NOT NULL,`tokens` INT NOT NULL DEFAULT '0')").execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	private void createPreparedStatements(){
		try{
			coins = getConnection().prepareStatement("SELECT * FROM `coins` WHERE uuid = ?;");
			tokens = getConnection().prepareStatement("SELECT * FROM `tokens` WHERE uuid = ?;");
			add = getConnection().prepareStatement("INSERT INTO `coins`(`uuid`) VALUES (?)");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public int getCoins(Player player){
		try{
			coins.setString(1, player.getUniqueId().toString());
			ResultSet rs = coins.executeQuery();
			if(rs.first()){
				return rs.getInt("coins");
			}else{
				add.setString(1, player.getUniqueId().toString());
				add.execute();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getTokens(Player player){
		try{
			tokens.setString(1, player.getUniqueId().toString());
			ResultSet rs = tokens.executeQuery();
			if(rs.first()){
				return rs.getInt("coins");
			}else{
				add.setString(1, player.getUniqueId().toString());
				add.execute();
				return 0;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return -1;
	}

	public static Economy getInstance(){
		return main;
	}

	private Connection getConnection(){
		if(connection==null)this.mysql.openConnection();
		if(!isConnected()){
			try{
				this.connection.close();
			} catch (SQLException e) {   
			}
			this.connection = this.mysql.openConnection();
		}
		return this.connection;
	}

	private boolean isConnected(){
		if(connection==null)return false;
		try{
			return this.connection.isValid(0);
		} catch (SQLException e) {  
			return(false);  
		}
	}
}
