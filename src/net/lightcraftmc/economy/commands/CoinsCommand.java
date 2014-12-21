package net.lightcraftmc.economy.commands;

import net.lightcraftmc.economy.Economy;
import net.lightcraftmc.fusebox.util.AbstractCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand extends AbstractCommand{

	public CoinsCommand(String command) {
		super(command);
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String Label,
			String[] args) {
		if(!s.hasPermission("commands.coins"));
		if(args.length==0){
			s.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-[Coins]-=-=-=-=-=-=-");
			s.sendMessage(ChatColor.AQUA + "/coins give <player> <amount>");
			s.sendMessage(ChatColor.AQUA + "/coins take <player> <amount>");
			return true;
		}
		switch(args[0].toLowerCase()){
		case "give":{
			if(args.length!=3){
				s.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-[Coins Give]-=-=-=-=-=-=-");
				s.sendMessage(ChatColor.AQUA + "/coins give <player> <amount>");
				return false;
			}
			Player player;
			if((player = Bukkit.getPlayer(args[1]))==null){
				s.sendMessage(ChatColor.RED + "Unknown player " + args[1]);
				return false;
			}
			int i;
			try{
				i = Integer.parseInt(args[2]);
			}catch(Exception e){
				s.sendMessage(ChatColor.RED + "Cannot parse Integer " + args[2]);
				return false;
			}
			Economy.getInstance().giveCoins(player, i);
			return true;
		}
		case "take":{
			if(args.length!=3){
				s.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-[Coins Take]-=-=-=-=-=-=-");
				s.sendMessage(ChatColor.AQUA + "/coins take <player> <amount>");
				return false;
			}
			Player player;
			if((player = Bukkit.getPlayer(args[1]))==null){
				s.sendMessage(ChatColor.RED + "Unknown player " + args[1]);
				return false;
			}
			int i;
			try{
				i = Integer.parseInt(args[2]);
			}catch(Exception e){
				s.sendMessage(ChatColor.RED + "Cannot parse Integer " + args[2]);
				return false;
			}
			Economy.getInstance().takeCoins(player, i);
			return true;
		}
		default:{
			s.sendMessage(ChatColor.RED + "Unknown argument '" + args[0] + "', for command /" + cmd.getLabel() + "");
			return false;
		}
		}
	}

}
