package br.com.codeplugins.mito.commands;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.codeplugins.mito.Main;
import br.com.codeplugins.mito.api.MitoAPI;
import br.com.codeplugins.mito.top.TopMito;

public class ComandoMito extends Command {

	private Main plugin;

	public ComandoMito(Main plugin) {
		super("mito");
		this.setPlugin(plugin);
		this.register();
	}

	public Main getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Main plugin) {
		this.plugin = plugin;
	}

	private void register() {
		try {
			Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			cmap.setAccessible(true);
			CommandMap map = (CommandMap) cmap.get(Bukkit.getServer());
			map.register("codeplugins", (Command) this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Player p = (Player) sender;
		if (args.length == 0) {
			if (MitoAPI.getMito().equals("Nenhum")) {
				sender.sendMessage("§cNo momento não ha um mito do pvp.");
				return false;
			} else {
				sender.sendMessage(
						Main.config.getString("Mensagens.Mito").replace("&", "§").replace("{mito}", MitoAPI.getMito()));
				return false;
			}
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("top")) {
				TopMito.loadPlayers();
				TopMito.load();
				TopMito.send(p);
				return false;
			}
			return false;
		}
		return false;
	}
}
