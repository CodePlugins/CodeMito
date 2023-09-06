package br.com.codeplugins.mito.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import br.com.codeplugins.mito.Main;

public class Config extends YamlConfiguration {

	private File bruteFile;
	private Plugin plugin;

	public static void createConfig(String file) {
		if (!new File(Main.getInstance().getDataFolder(), file + ".yml").exists()) {
			Main.getInstance().saveResource(file + ".yml", false);
		}
	}

	public Config(String name, Plugin plugin) {
		this.plugin = plugin;

		bruteFile = new File(plugin.getDataFolder(), name.matches(".*(?i).yml$") ? name : name.concat(".yml"));

		try {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdir();
			}

			if (!bruteFile.exists()) {
				bruteFile.createNewFile();
			}

			load(bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public void saveDefault() {
		if (plugin.getResource(bruteFile.getName()) == null) {
			System.err.println("[" + plugin.getName() + "] Nao foi possivel salvar o arquivo");
			System.err
					.println("[" + plugin.getName() + "] default da config " + bruteFile.getName() + " pois o jar nao");
			System.err.println("[" + plugin.getName() + "] contem um arquivo com teste nome.");
		} else {
			plugin.saveResource(bruteFile.getName(), true);
		}
	}

	public void set(String path) {
		super.set(path, "");
		this.save();
	}

	@Override
	public Object get(String path) {
		return super.get(path);
	}

	@Override
	public String getString(String path) {
		return super.getString(path);
	}

	@Override
	public int getInt(String path) {
		return super.getInt(path);
	}

	@Override
	public boolean getBoolean(String path) {
		return super.getBoolean(path);
	}

	@Override
	public List<String> getStringList(String path) {
		return super.getStringList(path);
	}

	public void save() {
		try {
			super.save(bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		try {
			load(bruteFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}