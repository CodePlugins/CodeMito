package br.com.codeplugins.mito;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import br.com.codeplugins.mito.commands.ComandoMito;
import br.com.codeplugins.mito.commands.ComandoMitoAdm;
import br.com.codeplugins.mito.listeners.LegendChatListeners;
import br.com.codeplugins.mito.listeners.MitoListeners;
import br.com.codeplugins.mito.manager.NPCManager;
import br.com.codeplugins.mito.utils.Config;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class Main extends JavaPlugin {

	private static Main instance;
	public static Config config;
	public static Config save;

	public void onEnable() {
		setInstance(this);
		debug("&aIniciando o plugin...");
		loadConfigs();
		loadHooks();
		registerCommands();
		registerEvents();
		new BukkitRunnable() {
			public void run() {
				for (NPC npc : CitizensAPI.getNPCRegistry()) {
					if (npc.getId() == 30000) {
						NPCManager.update();
					}
				}
			}
		}.runTaskLater(Main.getPlugin(Main.class), 20L * 3);
		debug("&aPlugin iniciado com sucesso!");
	}

	public void onDisable() {
		Iterator<Hologram> iterator = HologramsAPI.getHolograms(Main.getInstance()).iterator();
		while (iterator.hasNext()) {
			iterator.next().delete();
		}
	}

	void registerCommands() {
		new ComandoMito(this);
		new ComandoMitoAdm(this);
		debug("&fComandos registrados");
	}

	void loadHooks() {
		new LegendChatListeners(this);
	}

	void registerEvents() {
		new MitoListeners(this);
		debug("&fListeners registrados");
	}

	void loadConfigs() {
		Config.createConfig("config");
		Config.createConfig("save");
		save = new Config("save", this);
		config = new Config("config", this);
	}

	public static void debug(String msg) {
		Bukkit.getConsoleSender().sendMessage("§b[CodePlugins] §7" + msg.replace("&", "§"));
	}

	public static Main getInstance() {
		return instance;
	}

	public void setInstance(Main instance) {
		Main.instance = instance;
	}
}
