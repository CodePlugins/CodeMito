package br.com.codeplugins.mito.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import br.com.codeplugins.mito.Main;
import br.com.codeplugins.mito.api.MitoAPI;
import br.com.codeplugins.mito.manager.NPCManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class MitoListeners implements Listener {

	private Main plugin;

	public MitoListeners(Main plugin) {
		this.setPlugin(plugin);
		plugin.getServer().getPluginManager().registerEvents(this, (Plugin) plugin);
	}

	public Main getPlugin() {
		return plugin;
	}

	public void setPlugin(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle()
				.equals(Main.getInstance().getConfig().getString("TopMito.Name").replace("&", "§"))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void ChangeMito(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
			Player Vitima = e.getEntity();
			Player Assassino = Vitima.getKiller();
			if (MitoAPI.getMito().equalsIgnoreCase(Vitima.getName())) {
				MitoAPI.setMito(Assassino);
				for (NPC npc : CitizensAPI.getNPCRegistry()) {
					if (npc.getId() == 30000) {
						NPCManager.update();
					}
				}
				World world = Assassino.getWorld();
				world.strikeLightningEffect(Assassino.getLocation());
				Bukkit.broadcastMessage(Main.config.getString("Mensagens.NewMito").replace("&", "§")
						.replace("{killed}", Assassino.getName()).replace("{died}", Vitima.getName()));
				if (Main.config.getBoolean("Configuracoes.ExecutarComandos")) {
					for (String cmds : Main.config.getStringList("Configuracoes.Comandos")) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								cmds.replace("{player}", Assassino.getName()));
					}
				}
			}
		}
	}
}
