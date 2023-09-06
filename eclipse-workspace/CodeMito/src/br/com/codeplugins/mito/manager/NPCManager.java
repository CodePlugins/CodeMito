package br.com.codeplugins.mito.manager;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import br.com.codeplugins.mito.Main;
import br.com.codeplugins.mito.api.MitoAPI;
import br.com.codeplugins.mito.utils.SerializeLocation;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class NPCManager {

	public static void createNPC(Location location) {
		Iterator<Hologram> iterator = HologramsAPI.getHolograms(Main.getInstance()).iterator();
		while (iterator.hasNext()) {
			iterator.next().delete();
		}

		Main.config.set("Local", SerializeLocation.getSerializedLocation(location));
		Main.config.save();

		if (CitizensAPI.getNPCRegistry().getById(30000) != null
				&& CitizensAPI.getNPCRegistry().getById(30000).isSpawned()) {
			CitizensAPI.getNPCRegistry().getById(30000).despawn();
		}

		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID(), 30000, "");
		npc.data().set("player-skin-name", MitoAPI.getMito());
		npc.spawn(location);

		Hologram hologram = HologramsAPI.createHologram(Main.getInstance(), location.clone().add(0.0,
				Main.getInstance().getConfig().getDouble("Configuracoes.Hologram-Altura"), 0.0));

		npc.data().set("hologram", hologram);

		int i = 0;
		for (String hd : Main.config.getStringList("Configuracoes.Hologramas")) {
			hologram.insertTextLine(i, hd.replace("&", "§").replace("{mito}", MitoAPI.getMito()));
			i = i + 1;
		}
	}

	public static boolean deleteNPC() {
		NPC npc = CitizensAPI.getNPCRegistry().getById(30000);
		if (npc != null && npc.isSpawned()) {
			Hologram hologram = npc.data().get("hologram");
			if (hologram != null) {
				hologram.delete();
			}
			npc.despawn();
			npc.destroy();
			return true;
		}
		return false;
	}

	public static void update() {

		Iterator<Hologram> iterator = HologramsAPI.getHolograms(Main.getInstance()).iterator();
		while (iterator.hasNext()) {
			iterator.next().delete();
		}

		Location deserializedLocation = SerializeLocation.getDeserializedLocation(Main.config.getString("Local"));

		int i = 30000;
		for (NPC npc : CitizensAPI.getNPCRegistry()) {
			if (npc.getId() == i) {
				npc.despawn();
			}
		}

		CitizensAPI.getNPCRegistry().getById(i).despawn();
		NPC npc2 = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID(), i, "");
		npc2.data().set("player-skin-name", MitoAPI.getMito());
		npc2.spawn(deserializedLocation);
		Hologram hologram = HologramsAPI.createHologram(Main.getInstance(), deserializedLocation.clone().add(0.0,
				Main.getInstance().getConfig().getDouble("Configuracoes.Hologram-Altura"), 0.0));
		int i2 = 0;
		for (String hd : Main.config.getStringList("Configuracoes.Hologramas")) {
			hologram.insertTextLine(i2, hd.replace("&", "§").replace("{mito}", MitoAPI.getMito()));
			i2 = i2 + 1;
		}
	}
}
