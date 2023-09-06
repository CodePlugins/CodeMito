package br.com.codeplugins.mito.commands;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.codeplugins.mito.Main;
import br.com.codeplugins.mito.api.MitoAPI;
import br.com.codeplugins.mito.manager.NPCManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class ComandoMitoAdm extends Command {

	static String cashFormato(double m) {
		BigDecimal valor = new BigDecimal(m);
		NumberFormat nf = new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("pt", "BR")));
		return nf.format(valor);
	}

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private Main plugin;

	public ComandoMitoAdm(Main plugin) {
		super("mitoadm");
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
		if (!p.hasPermission("codemito.admin")) {
			p.sendMessage(Main.config.getString("Mensagens.NoHavePermission").replace("&", "§"));
			return false;
		}
		if (args.length == 0) {
			p.sendMessage("");
			p.sendMessage("§a/mitoadm set <Player> §8- §7setar o mito");
			p.sendMessage("§a/mitoadm setnpc §8- §7setar o npc do mito");
			p.sendMessage("§c/mitoadm delnpc §8- §7deletar o npc do mito");
			p.sendMessage("");
			return false;
		}

		if (args[0].equals("setnpc")) {
			p.sendMessage("§aVocê setou o npc do mito do pvp.");
			NPCManager.createNPC(p.getLocation());
			return false;
		}

		if (args[0].equals("delnpc")) { 
			if (NPCManager.deleteNPC()) {
				p.sendMessage("§aVocê deletou o npc do mito do pvp.");
			} else {
				p.sendMessage("§cNão foi possível deletar o npc do mito.");
			}
			return false;
		}

		if (args[0].equalsIgnoreCase("set")) {
			if (args.length < 2) {
				p.sendMessage("§eUtilize: §7/mitoadm set <Player>");
			} else {
				Player target = Bukkit.getPlayerExact(args[1]);
				if (target == null) {
					p.sendMessage("§cEste Jogador Não existe!");
					return false;
				} else {
					MitoAPI.setMito(target);
					p.sendMessage("§aVocê setou o novo mito do pvp.");
					int i = 30000;
					for (NPC npc : CitizensAPI.getNPCRegistry()) {
						if (npc.getId() == i) {
							NPCManager.update();
						}
					}
					target.getWorld().strikeLightningEffect(target.getLocation());
					Bukkit.broadcastMessage(Main.config.getString("Mensagens.SetMito").replace("&", "§")
							.replace("{player}", target.getName()));
					if (Main.config.getBoolean("Configuracoes.ExecutarComandos")) {
						for (String cmds : Main.config.getStringList("Configuracoes.Comandos")) {
							Main.debug(cmds.replace("{player}", target.getName()));
							Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
									cmds.replace("{player}", target.getName()));
						}
					}
					return false;
				}
			}
		}
		return false;
	}
}
