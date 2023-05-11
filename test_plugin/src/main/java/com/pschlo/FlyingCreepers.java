package com.pschlo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FlyingCreepers implements Listener {

    static final int beeCreeperRange = 30;

    public FlyingCreepers(Scheduler scheduler) {

        Runnable task = new BukkitRunnable() {
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
                        
                        // check if looking at player
                        if (!(bee.hasLineOfSight(p)))
                            continue;
                        else
                            Bukkit.broadcastMessage("line of siight");

                        if (bee.getAnger() > 0) {
                            Entity target = bee.getTarget();
                            if (target != null && target.getUniqueId().equals(p.getUniqueId())) {
                                //adjustSpeed(bee);
                            }
                            continue;
                        }


                        // checks passed
                        Bukkit.broadcastMessage("attack");
                        bee.setAnger(800); // for some reason, AngerTime is always a random value between 400 and 800
                        bee.setTarget(p);
                        // p.sendMessage("anger: " + bee.getAnger());
                    }
                }
            }
        };

        // scheduler.runEvery(task, 5);
    }

    @EventHandler
    public void onEntityBlockDamage(EntityDamageEvent e) {
        // detect suffocating creeper
        if (!(e.getEntityType() == EntityType.CREEPER &&
            e.getEntity().isInsideVehicle() &&
            e.getEntity().getVehicle().getType() == EntityType.BEE &&
            e.getCause() == DamageCause.SUFFOCATION))
            return;

        // prevent suffocation    
        e.setCancelled(true);
    }

    @EventHandler
    public void onCreeperSpawn(CreatureSpawnEvent e) {
        if (!(e.getEntityType() == EntityType.CREEPER &&
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

        Creeper creeper = (Creeper) e.getEntity();
        //creeper.setMaxFuseTicks(1);  // explode instantly when in range
        Bee bee = (Bee) world.spawnEntity(e.getLocation().add(0, 3, 0), EntityType.BEE);
        bee.addPassenger(creeper);
        //Bukkit.broadcastMessage("rem when far away: "+bee.getRemoveWhenFarAway() + creeper.getRemoveWhenFarAway());
    }

    public void adjustSpeed(Bee bee) {
        Vector currVel = bee.getVelocity().clone();
        //Vector oldVel = beeMap.getOrDefault(bee.getUniqueId(), currVel).clone();
        Vector newVel = currVel.clone();
        //Vector diff = currVel.clone().subtract(oldVel);
        //double length = currVel.length();


        /*double t = 1;
        if (currVel.getX() > t || currVel.getY() > t || currVel.getZ() > t) {
            Bukkit.broadcastMessage("TOO FAST! " + currVel);
        }*/


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
    }

    // given the current speed (length of velocity vector), this function returns how much length should be added to the velocity vector
    public static double func(double speed, double maxSpeed) {
        /*
        // bell curve
        double height = 2;
        double stretch = 0.2;  // must be greater than 0
        double x_shift = 0.3;  // positive means right shift
        double hardness = 1;  // must be non-negative
        double y_shift = 0.9;  // where the bottom line is

        double res = (height-y_shift) * Math.pow(2, -Math.pow((x-x_shift)/stretch, 2*hardness)) + y_shift;
        */
        

        /*
        function consists of 5 parts:
            1) constant
            2) linear increase
            3) constant
            4) linear decrease
            5) constant
        */

        // the function results for the different parts
        double level1 = 0;
        //double level3 = 0.5*maxSpeed;
        double level3 = 0.15;
        double level5 = 0;

        // at which speeds which parts begin/end
        double end1 = 0;
        double start3 = maxSpeed*0.3;
        double end3 = maxSpeed*0.8;
        double start5 = maxSpeed;

        if (speed < end1)
            // part 1
            return level1;
        else if (speed < start3)
            // part 2
            return ((level3-level1)/(start3-end1)) * (speed-end1) + level1;
        else if (speed < end3)
            // part 3
            return level3;
        else if (speed < start5)
            // part 4
            return ((level5-level3)/(start5-end3)) * (speed-end3) + level3;
        else
            // part 5
            return level5;
    }
}
