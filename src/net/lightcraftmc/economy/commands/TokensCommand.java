package net.lightcraftmc.economy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.lightcraftmc.economy.Economy;
import net.lightcraftmc.fusebox.util.AbstractCommand;

public class TokensCommand extends AbstractCommand{

	public TokensCommand(String command) {
		super(command);
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String Label,
			String[] args) {
		if(!s.hasPermission("economy.tokens"))return false;
		if(args.length==0){
			s.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-[Tokens]-=-=-=-=-=-=-");
			s.sendMessage(ChatColor.AQUA + "/tokens give <player> <amount>");
			s.sendMessage(ChatColor.AQUA + "/tokens take <player> <amount>");
			return true;
		}
		switch(args[0].toLowerCase()){
		case "give":{
			if(args.length!=3){
				s.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-[Tokens Give]-=-=-=-=-=-=-");
				s.sendMessage(ChatColor.AQUA + "/tokens give <player> <amount>");
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
			Economy.getInstance().giveTokens(player, i);
			return true;
		}
		case "take":{
			if(args.length!=3){
				s.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-[Tokens Take]-=-=-=-=-=-=-");
				s.sendMessage(ChatColor.AQUA + "/tokens take <player> <amount>");
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
			Economy.getInstance().takeTokens(player, i);
			return true;
		}
		default:{
			s.sendMessage(ChatColor.RED + "Unknown argument '" + args[0] + "', for command /" + cmd.getLabel() + "");
			return false;
		}
		}

	}
}
