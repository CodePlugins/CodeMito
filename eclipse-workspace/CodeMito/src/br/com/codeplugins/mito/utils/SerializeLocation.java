package br.com.codeplugins.mito.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializeLocation {

	public static String getSerializedLocation(Location location) {
		return String.valueOf(location.getX()) + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw()
				+ ";" + location.getPitch() + ";" + location.getWorld().getUID();
	}

	public static Location getDeserializedLocation(String s) {
		if (s.equalsIgnoreCase("null")) {
			return null;
		}
		String[] split = s.split(";");
		return new Location(Bukkit.getServer().getWorld(UUID.fromString(split[5])), Double.parseDouble(split[0]),
				Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]),
				Float.parseFloat(split[4]));
	}
}
