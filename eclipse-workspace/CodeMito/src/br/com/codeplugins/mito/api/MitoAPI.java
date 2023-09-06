package br.com.codeplugins.mito.api;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.entity.Player;

import br.com.codeplugins.mito.Main;

public class MitoAPI {

	public static void setMito(Player p) {
		Main.save.set("Mito-Atual", p.getName());
		Main.save.set("Jogadores." + p.getName(), getPegouMito(p) + 1);
		Main.save.save();
	}

	public static String getMito() {
		if (Main.save.getString("Mito-Atual").equals("Nenhum")) {
			return "Nenhum";
		} else {
			return Main.save.getString("Mito-Atual");
		}
	}

	public static Integer getPegouMito(Player p) {
		return Main.save.getInt("Jogadores." + p.getName());
	}
	
	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e2.getValue().compareTo(e1.getValue());
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
}
