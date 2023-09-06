package br.com.codeplugins.mito.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import br.com.codeplugins.mito.Main;
import br.com.codeplugins.mito.api.MitoAPI;
import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class LegendChatListeners implements Listener {

	private Main plugin;

	public LegendChatListeners(Main plugin) {
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
	public void SendMsg(ChatMessageEvent e) {
		Player p = e.getSender();

		if (e.getTags().contains("trymito")) {
			if (MitoAPI.getMito().equalsIgnoreCase(p.getName())) {
				e.setTagValue("trymito", Main.config.getString("Configuracoes.Tag").replace("&", "§"));
			}
		}
	}
}