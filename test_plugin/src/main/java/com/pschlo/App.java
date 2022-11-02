package com.pschlo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.bukkit.event.*;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.EventException;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
// import org.bukkit.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.BlockIterator;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;



public class App extends JavaPlugin implements Listener {


    //Map<UUID,Vector> beeMap = new HashMap<>();
    //int counter = 0;

    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");
        getServer().getPluginManager().registerEvents(this, this);
        int beeCreeperRange = 30;

        BukkitTask task = new BukkitRunnable() {
            public void run() {
                //counter++;
                List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                Collections.shuffle(onlinePlayers);

                for (Player p : onlinePlayers) {
                    for (Entity entity : p.getNearbyEntities(beeCreeperRange, beeCreeperRange, beeCreeperRange)) {

                        // check if bee
                        if (!(entity.getType() == EntityType.BEE))
                            continue;
                        Bee bee = (Bee) entity;

                        // check passengers
                        List<Entity> passengers = bee.getPassengers();
                        if (!(passengers.size() == 1 && passengers.get(0).getType() == EntityType.CREEPER))
                            continue;

                        if (bee.getAnger() > 0) {
                            Entity target = bee.getTarget();
                            if (target != null && target.getUniqueId().equals(p.getUniqueId()))
                                beeCreeper(bee);
                            continue;
                        }


                        // checks passed

                        bee.setAnger(800); // for some reason, AngerTime is always a random value between 400 and 800
                        bee.setTarget(p);
                        // p.sendMessage("anger: " + bee.getAnger());
                    }
                }
            }
        }.runTaskTimer(this, 0, 5);


        /*new BukkitRunnable() {
            public void run() {
                Bukkit.broadcastMessage("speedup!");
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntitiesByClass(Bee.class)) {
                        entity.setVelocity(entity.getVelocity().multiply(2));
                        //Bukkit.broadcastMessage("velocity is now " + entity.getVelocity().length());
                    }
                }
            }
        }.runTaskTimer(this, 20*10, 15);*/
        
    }

    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }

    public void beeCreeper(Bee bee) {
        Vector currVel = bee.getVelocity().clone();
        //Vector oldVel = beeMap.getOrDefault(bee.getUniqueId(), currVel).clone();
        Vector newVel = currVel.clone();
        //Vector diff = currVel.clone().subtract(oldVel);
        double length = currVel.length();
        //double diffLength = diff.length();
        //double newLength = length;

        //diff.setY(0);

        /*double t = 1;
        if (currVel.getX() > t || currVel.getY() > t || currVel.getZ() > t) {
            Bukkit.broadcastMessage("TOO FAST! " + currVel);
        }*/


        //double newX = currVel.getX() * func(currVel.getX());
        //double newY = currVel.getY() * func(currVel.getY());
        //double newZ = currVel.getZ() * func(currVel.getZ());

        //newVel = new Vector(newX, newY, newZ);

        // adapt diff
        // min_speed should be about 0.15
        /*double min_speed = 0.15;
        double min_diff = 0;
        double max_diff = 1;
        double adaDiffX, adaDiffY, adaDiffZ;

        if (Math.abs(newX) > min_speed && min_diff < Math.abs(diffX) && Math.abs(diffX) < max_diff)
            adaDiffX = diffX * 10 * Math.abs(newX);
        else
            adaDiffX = 0;

        if (Math.abs(newY) > min_speed && min_diff < Math.abs(diffY) && Math.abs(diffY) < max_diff)
            adaDiffY = diffY * 10 * Math.abs(newY);
        else
            adaDiffY = 0;

        if (Math.abs(newZ) > min_speed && min_diff < Math.abs(diffZ) && Math.abs(diffZ) < max_diff)
            adaDiffZ = diffZ * 10 * Math.abs(newZ);
        else
            adaDiffZ = 0;*/

            
        //double adaDiffX = Math.abs(newX) > 0.2 ? diffX * 5 * Math.abs(newX) : 0;
        //double adaDiffY = Math.abs(newY) > 0.2 ? diffY * 5 * Math.abs(newY) : 0;
        //double diffY = 0;
        //double adaDiffZ = Math.abs(newZ) > 0.2 ? diffZ * 5 * Math.abs(newZ) : 0;
        
        //Vector adaDiff = new Vector(adaDiffX, adaDiffY, adaDiffZ);


        // get change in direction of velocity (orthogonal projection)
        //Vector diffProjVel;
        //if (newVel.length() > 0)
        //    diffProjVel = newVel.clone().multiply(diff.dot(newVel)/newVel.dot(newVel));
        //else
        //    diffProjVel = new Vector();

        //ewVel.add(diffProjVel.multiply(Math.abs(newVel.length())));


        // factor to increase speed by
        // normal max speed of bee: 0.15
        double factor = func(newVel.length(), 0.3);
        // increase speed
        if (newVel.length() > 0)
            newVel.add(newVel.normalize().multiply(factor));

        // check if bee is flying towards player
        //Location locPlayer = p.getEyeLocation(); //bee.getTarget().getLocation();
        //Location locBee = bee.getLocation();
        //Vector beeToPlayer = locPlayer.subtract(locBee).toVector().normalize();
        //Vector beeDirection = newVel.clone().normalize();
        /*if (beeToPlayer.dot(beeDirection) < 0 || (beeToPlayer.dot(beeDirection) > 0.9 && bee.hasLineOfSight(p))) {
            newVel.add(beeToPlayer.clone().multiply(func(newVel.length(), 1)));
            if (counter % 1 == 0)
                Bukkit.broadcastMessage("flying towards player!");
        }*/

        /*BlockIterator blocks = new BlockIterator(p.getWorld(), locBee.toVector(), beeToPlayer, 0, beeCreeperRange);
        Block block;
        boolean isOnlyAir = true;
        while (blocks.hasNext()) {
            block = blocks.next();
            if (block.getType() != Material.AIR)
                isOnlyAir = false;
        }
        if (isOnlyAir && newVel.clone().setY(0).length() < 0.4)
            newVel.add(beeToPlayer.clone().multiply(0.2));
            Bukkit.broadcastMessage("isOnlyAir: " + isOnlyAir);




        //double maxDiff = Math.max(Math.abs(diff.getZ()), Math.max(Math.abs(diff.getX()), Math.abs(diff.getY())));
        //Vector adjDiff = new Vector();
        // filter both too small and too big diffs
        // big diffs are filtered because e.g. when player attacks bee
        // increase every coordinate according to their speed
        //newVel.length() > 0.1
        //if (0 < diffLength && maxDiff < 0.4) {
        //    adjDiff.setX(diff.getX() * Math.abs(newVel.getX() * 7));
        //    adjDiff.setY(diff.getY() * Math.abs(newVel.getY() * 7));
        //    adjDiff.setZ(diff.getZ() * Math.abs(newVel.getZ() * 7));
        //}



        // project velocity onto diff direction
        /*Vector velProjDiff;
        Vector vel = newVel.clone();

        if (diff.length() > 0) {
            if (vel.dot(diff) < 0)
                vel.multiply(-1);
            velProjDiff = diff.clone().multiply(vel.dot(diff)/diff.dot(diff));
        }
        else
            velProjDiff = new Vector();

        adjDiff.add(velProjDiff.clone().multiply(1));
        */


        //newVel.add(projDiff);
    

        //newVel = currVel;

        //if (length > 0 && length < 0.5) {
        //    newVel.multiply(1.2);
        //    newLength = newLength*2;
        //}
        // enhance diff effect
        //newVel.add(diff.clone().multiply(4*newLength));
        
        //diff.subtract(beeMap.getOrDefault(bee.getUniqueId(), new Vector(0, 0, 0)));
        //if (length > 0 && length < 0.5) newVel = currVel.add(diff.multiply(0)).multiply(10);
        //add(diff.multiply(0.05))
        //newVel = currVel.add(diff.multiply(0.2));

        newVel.setY(currVel.getY());
        bee.setVelocity(newVel);

        //beeMap.put(bee.getUniqueId(), newVel);
        /*
        if (counter % 4 == 0) {
            //Bukkit.broadcastMessage("old: " + currVel + "new: " + newVel);
            Bukkit.broadcastMessage("old diff: " + diff);
            //Bukkit.broadcastMessage("adjusted diff: " + diffProjVel);
            Bukkit.broadcastMessage("X: " + newVel.getX() + " Y: "+newVel.getY()+" Z: "+newVel.getZ());
            Bukkit.broadcastMessage("");
        } */

        //Vector newVel = currVel.multiply(0.5/currVel.length());
        //Bukkit.broadcastMessage("" + bee.getUniqueId() + " speed is " + bee.getVelocity().length());
        //Vector newVel = new Vector(oldVel.getX()*1.2, oldVel.getY(), oldVel.getZ()*1.2);
        //bee.setVelocity(bee.getEyeLocation().getDirection().multiply(0.2));
        // check if bee is already attacking someone
    }

    public static double func(double speed, double maxSpeed) {
        double height = 2;
        double stretch = 0.2;  // must be greater than 0
        double x_shift = 0.3;  // positive means right shift
        double hardness = 1;  // must be non-negative
        double y_shift = 0.9;  // where the bottom line is

        //double res = (height-y_shift) * Math.pow(2, -Math.pow((x-x_shift)/stretch, 2*hardness)) + y_shift;
        
        //double a = 2;
        /*
        if (x < 0.25) {
            return 2;
        } else if (x < 0.375) {
            return -(32)*x*x + (16)*x + (0);
        } else if (x < 0.5) {
            return (32)*x*x - (32)*x + (9);
        } else {
            return 1;
        } */


        double lvl_start = 0;
        //double lvl_mid = 0.5*maxSpeed;
        double lvl_mid = 0.15;
        double lvl_end = 0;

        double start_mid = maxSpeed*0.3;
        double start_end = maxSpeed*0.8;
        double end_end = maxSpeed;

        if (speed < start_mid)  
            return ((lvl_mid-lvl_start)/start_mid) * speed + lvl_start;
        else if (speed < start_end)
            return lvl_mid;
        else if (speed < end_end)
            return ((lvl_end-lvl_mid)/(end_end-start_end)) * (speed-start_end) + lvl_mid;
        else
            return lvl_end;
        //return res;
    }

    @EventHandler
    public void onCreatureSpawn2(CreatureSpawnEvent e) {
        if (!(e.getEntityType() == EntityType.CREEPER &&
            //e.getSpawnReason() == SpawnReason.NATURAL &&
            !(e.getEntity().isInsideVehicle())))
            return;
        
        int percentage = 10;
    
        int randNum = ThreadLocalRandom.current().nextInt(0, 100/percentage);
        if (randNum != 0)
            return;

        Location loc = e.getLocation();
        World world = loc.getWorld();

        if (!allBlocksInRegion(Material.AIR, loc.clone().add(-3, 2, -3), loc.clone().add(3, 5, 3)))
            return;

        Creeper creeper = (Creeper) e.getEntity();
        //creeper.setMaxFuseTicks(1);  // explode instantly when in range
        Bee bee = (Bee) world.spawnEntity(e.getLocation().add(0, 3, 0), EntityType.BEE);
        bee.addPassenger(creeper);
        //Bukkit.broadcastMessage("rem when far away: "+bee.getRemoveWhenFarAway() + creeper.getRemoveWhenFarAway());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        World world = p.getWorld();
        //Bukkit.broadcastMessage("Welcome to the server!");
        /*
        Bee bee = (Bee) world.spawnEntity(p.getLocation(), EntityType.BEE);
        bee.setTarget(p);
        bee.setAnger(20*60);
        bee.getNearbyEntities(0, 0, 0)*/
    }

    @EventHandler
    public void ProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;
        Snowball ball = (Snowball) event.getEntity();
        if (!(ball.getShooter() instanceof Player)) return;
        ball.setVelocity(ball.getVelocity().clone().multiply(5));
}

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        Arrow arrow = (Arrow) e.getProjectile();
        arrow.setGravity(false);
        Vector new_vector = arrow.getVelocity().multiply(0.1);
        // e.getProjectile().setVelocity(new_vector);
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

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (!(e.getEntityType() == EntityType.SKELETON &&
            //e.getSpawnReason() == SpawnReason.NATURAL &&
            !(e.getEntity().isInsideVehicle())))
            return;

        int flyingSkeletonPercent = 10;
        
        int randNum = ThreadLocalRandom.current().nextInt(0, 100/flyingSkeletonPercent);
        if (randNum != 0)
            return;

        Location loc = e.getLocation();
        World world = loc.getWorld();

        // TODO: use builtin Box class
        if (!allBlocksInRegion(Material.AIR, loc.clone().add(-3, 2, -3), loc.clone().add(3, 5, 3)))
            return;

        Phantom phantom = (Phantom) world.spawnEntity(e.getLocation().add(0, 3, 0), EntityType.PHANTOM);
        phantom.addPassenger(e.getEntity());
    }
    
    public static boolean allBlocksInRegion(Material material, Location pos1, Location pos2) {
        // delta represents pos2-pos1 in blocks
        int delta_x = pos2.getBlockX()-pos1.getBlockX();
        int delta_y = pos2.getBlockY()-pos1.getBlockY();
        int delta_z = pos2.getBlockZ()-pos1.getBlockZ();

        double step_x = Math.copySign(1, delta_x);
        double step_y = Math.copySign(1, delta_y);
        double step_z = Math.copySign(1, delta_z);

        //Bukkit.broadcastMessage("delta_x is " + delta_x);
        //Bukkit.broadcastMessage("delta_y is " + delta_y);
        //Bukkit.broadcastMessage("delta_z is " + delta_z);

        Location pos = new Location(pos1.getWorld(), 0, 0, 0);

        pos.setX(pos1.getX());
        for (int dx=0; dx<=Math.abs(delta_x); dx++) {
            
            pos.setY(pos1.getY());
            for (int dy=0; dy<=Math.abs(delta_y); dy++) {

                pos.setZ(pos1.getZ());
                for (int dz=0; dz<=Math.abs(delta_z); dz++) {

                    Material currMat = pos.getBlock().getType();
                    if (currMat != material) {
                        //Bukkit.broadcastMessage("" + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " check failed " + currMat);
                        return false;
                    }

                    // if (dy == 0) Bukkit.broadcastMessage("" + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " check passed " + currMat);
                    //if (dy == 0) Bukkit.broadcastMessage("z is " + pos.getZ() + ". Increasing z by " + Math.signum(delta_z));
                    pos.setZ(pos.getZ()+step_z);
                }
                pos.setY(pos.getY()+step_y);
            }
            pos.setX(pos.getX()+step_x);
        }

        return true;
    }

    /*
    private boolean isLookingAt(LivingEntity e1, Entity e2) {
        Location eyeLocation = e1.getEyeLocation();
        BoundingBox box = e2.getBoundingBox();
        box.

        Vector toEntity = e2.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        
        return dot > 0.99D;
    }
    */

    
}
