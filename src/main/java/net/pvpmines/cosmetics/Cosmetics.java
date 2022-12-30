package net.pvpmines.cosmetics;

import net.pvpmines.cosmetics.particles.ParticleType;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Cosmetics {

    public static HashMap<UUID, ParticleType> particles = new HashMap<>();
    public static ArrayList<UUID> armor = new ArrayList<>();

    public void cosmeticEvent(PlayerMoveEvent event) {
        if (!particles.containsKey(event.getPlayer().getUniqueId())) return;
        Location location = event.getPlayer().getLocation();
        location.setY(location.getY() + 1);
        event.getPlayer().getWorld().playEffect(location, getEffectByENUM(event.getPlayer()), 0);
    }
    public ParticleType getEffectFromString(String string){

        if (string.equalsIgnoreCase("flame")) {
            return ParticleType.FLAMES;
        } else if (string.equalsIgnoreCase("water")) {
            return ParticleType.WATER;
        } else if (string.equalsIgnoreCase("lava")) {
            return ParticleType.LAVA;
        } else if (string.equalsIgnoreCase("heart")) {
            return ParticleType.HEART;
        }
        return null;
    }

    public void applyEffect(UUID uuid, ParticleType effectType){
        if (!particles.containsKey(uuid)) {
            particles.put(uuid, effectType);
        } else {
            particles.replace(uuid, particles.get(uuid), effectType);
        }
    }

    public Effect getEffectByENUM(Player player){
        if (particles.get(player.getUniqueId()).equals(ParticleType.FLAMES)) {
            return Effect.MOBSPAWNER_FLAMES;
        } else if (particles.get(player.getUniqueId()).equals(ParticleType.WATER)) {
            return Effect.WATERDRIP;
        } else if (particles.get(player.getUniqueId()).equals(ParticleType.LAVA)) {
            return Effect.LAVADRIP;
        } else if (particles.get(player.getUniqueId()).equals(ParticleType.HEART)) {
            return Effect.HEART;
        }

        return null;
    }

    public void clearEffect(UUID uuid){
        particles.remove(uuid);
    }

    public void clearAllEffect() {
        particles.clear();
    }
}
