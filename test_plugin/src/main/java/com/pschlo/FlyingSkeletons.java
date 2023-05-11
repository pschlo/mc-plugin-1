package com.pschlo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FlyingSkeletons implements Listener {

    @EventHandler
    public void onSkeletonSpawn(CreatureSpawnEvent e) {
        if (!(e.getEntityType() == EntityType.SKELETON &&
            //e.getSpawnReason() == SpawnReason.NATURAL &&
            !(e.getEntity().isInsideVehicle())))
            return;

        int PROB_FLY = 10;
        if (!(App.getRandTrue(PROB_FLY)))
            return;

        Location loc = e.getLocation();
        World world = loc.getWorld();

        WorldAwareBox box = WorldAwareBox.of(loc.clone().add(-3, 2, -3), loc.clone().add(3, 5, 3));
        if (!(box.containsOnly(Material.AIR)))
            return;

        Phantom phantom = (Phantom) world.spawnEntity(e.getLocation().add(0, 3, 0), EntityType.PHANTOM);
        phantom.addPassenger(e.getEntity());
    }

    @EventHandler
    public void onEntityBlockDamage(EntityDamageEvent e) {
        // detect skeleton riding a phantom and suffocating
        if (!(e.getEntityType() == EntityType.SKELETON &&
            e.getEntity().isInsideVehicle() &&
            e.getEntity().getVehicle().getType() == EntityType.PHANTOM &&
            e.getCause() == DamageCause.SUFFOCATION))
            return;

        // prevent suffocation    
        e.setCancelled(true);
    }

}
