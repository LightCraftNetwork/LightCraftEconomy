package net.lightcraftmc.economy;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.lightcraftmc.economy.commands.CoinsCommand;
import net.lightcraftmc.economy.commands.TokensCommand;
import net.lightcraftmc.fusebox.configuration.Configuration;
import net.lightcraftmc.fusebox.mysql.MySQL;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Economy extends JavaPlugin{

	private static Economy main;
	private Connection connection;
	private MySQL mysql;
	private PreparedStatement tokens, coins, addtokens, addcoins, setcoins, settokens;
	private BigInteger max = new BigInteger(String.valueOf(Integer.MAX_VALUE)), min = new BigInteger(String.valueOf(Integer.MIN_VALUE));

	public void onEnable(){
		main = this;
		new CoinsCommand("coins").register();
		new TokensCommand("tokens").register();
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
			getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coins` (`uuid` VARCHAR(36) NOT NULL,`coins` INT NOT NULL DEFAULT '0');").execute();
			getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS`tokens` (`uuid` VARCHAR(36) NOT NULL,`tokens` INT NOT NULL DEFAULT '0');").execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	private void createPreparedStatements(){
		try{
			coins = getConnection().prepareStatement("SELECT * FROM `coins` WHERE uuid = ?;");
			tokens = getConnection().prepareStatement("SELECT * FROM `tokens` WHERE uuid = ?;");
			addcoins = getConnection().prepareStatement("INSERT INTO `coins`(`uuid`) VALUES (?);");
			addtokens = getConnection().prepareStatement("INSERT INTO `tokens`(`uuid`) VALUES (?);");
			setcoins = getConnection().prepareStatement("UPDATE `coins` SET `coins` = ? WHERE `playername` = ?;");
			settokens = getConnection().prepareStatement("UPDATE `coins` SET `tokens` = ? WHERE `playername` = ?;");
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
				addcoins.setString(1, player.getUniqueId().toString());
				addcoins.execute();
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
				return rs.getInt("tokens");
			}else{
				addtokens.setString(1, player.getUniqueId().toString());
				addtokens.execute();
				return 0;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	public void giveCoins(Player player, int amount){
		try{
			int currentint = getCoins(player);
			BigInteger current = new BigInteger(String.valueOf(currentint));
			BigInteger add = new BigInteger(String.valueOf(amount));
			int set = amount;
			BigInteger temp;
			if((temp = current.add(add)).longValue()>max.longValue()){
				set = Integer.MAX_VALUE;
			}else if(temp.longValue()<min.longValue()){
				set = Integer.MIN_VALUE;
			}else{
				set = currentint + amount;
			}
			setcoins.setInt(1, set);
			setcoins.setString(2, player.getUniqueId().toString());
			setcoins.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void giveTokens(Player player, int amount){
		try{
			int currentint = getTokens(player);
			BigInteger current = new BigInteger(String.valueOf(currentint));
			BigInteger add = new BigInteger(String.valueOf(amount));
			int set = amount;
			BigInteger temp;
			if((temp = current.add(add)).longValue()>max.longValue()){
				set = Integer.MAX_VALUE;
			}else if(temp.longValue()<min.longValue()){
				set = Integer.MIN_VALUE;
			}else{
				set = currentint + amount;
			}
			settokens.setInt(1,set);
			settokens.setString(2, player.getUniqueId().toString());
			settokens.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void takeCoins(Player player, int amount){
		try{
			int currentint = getCoins(player);
			BigInteger current = new BigInteger(String.valueOf(currentint));
			BigInteger add = new BigInteger(String.valueOf(amount));
			int set = amount;
			BigInteger temp;
			if((temp = current.add(add)).longValue()>max.longValue()){
				set = Integer.MAX_VALUE;
			}else if(temp.longValue()<min.longValue()){
				set = Integer.MIN_VALUE;
			}else{
				set = currentint - amount;
			}
			setcoins.setInt(1, set);
			setcoins.setString(2, player.getUniqueId().toString());
			setcoins.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void takeTokens(Player player, int amount){
		try{
			int currentint = getTokens(player);
			BigInteger current = new BigInteger(String.valueOf(currentint));
			BigInteger add = new BigInteger(String.valueOf(amount));
			int set = amount;
			BigInteger temp;
			if((temp = current.add(add)).longValue()>max.longValue()){
				set = Integer.MAX_VALUE;
			}else if(temp.longValue()<min.longValue()){
				set = Integer.MIN_VALUE;
			}else{
				set = currentint - amount;
			}
			settokens.setInt(1,set);
			settokens.setString(2, player.getUniqueId().toString());
			settokens.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static Economy getInstance(){
		return main;
	}

	private Connection getConnection(){
		if(connection==null)this.connection = this.mysql.openConnection();
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
