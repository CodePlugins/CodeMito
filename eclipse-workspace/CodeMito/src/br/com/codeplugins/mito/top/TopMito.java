package br.com.codeplugins.mito.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import br.com.codeplugins.mito.Main;
import br.com.codeplugins.mito.api.MitoAPI;
import br.com.codeplugins.mito.utils.ItemBuilder;

public class TopMito {

	public static Inventory inv;

	public static HashMap<String, Integer> topmito = new HashMap<String, Integer>();

	public static void loadPlayers() {
		for (String lista : Main.save.getConfigurationSection("Jogadores.").getKeys(false)) {
			topmito.put(lista, Main.save.getInt("Jogadores." + lista));
		}
	}

	public static void load() {
		TopMito.inv = Bukkit.createInventory((InventoryHolder) null, 36,
				Main.getInstance().getConfig().getString("TopMito.Name").replace("&", "§"));
		int i = 0;

		for (Entry<String, Integer> list : MitoAPI.entriesSortedByValues(TopMito.topmito)) {
			String jogador = list.getKey();
			++i;
			ItemBuilder item = new ItemBuilder(Material.SKULL_ITEM);
			item.durability(3);
			item.owner(jogador);
			item.name(Main.getInstance().getConfig().getString("TopMito.Icone.Name").replace("&", "§")
					.replace("{position}", String.valueOf(i) + "§").replace("{player}", jogador)
					.replace("{quantia}", "" + list.getValue()));

			ArrayList<String> array = new ArrayList<String>();
			for (String se : Main.getInstance().getConfig().getStringList("TopMito.Icone.Lore")) {
				array.add(se.replace("&", "§").replace("{position}", String.valueOf(i) + "§")
						.replace("{player}", jogador).replace("{quantia}", "" + list.getValue()));
			}
			item.listLore(array);
			TopMito.inv.setItem(getFreeSlot(TopMito.inv), item.build());
		}
		for (int i2 = i + 1; i2 < 11; ++i2) {
			TopMito.inv.setItem(getFreeSlot(TopMito.inv), new ItemBuilder(Material.BARRIER).name("§c-//-").build());
		}
	}

	public static void send(Player player) {
		player.openInventory(TopMito.inv);
	}

	public static int getFreeSlot(Inventory inv) {
		int[] ints = { 11, 12, 13, 14, 15, 20, 21, 22, 23, 24 };
		int[] array;
		for (int length = (array = ints).length, j = 0; j < length; ++j) {
			int i = array[j];
			if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
				return i;
			}
		}
		return -1;
	}
}
