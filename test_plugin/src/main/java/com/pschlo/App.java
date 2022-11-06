package com.pschlo;

import org.bukkit.entity.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.*;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
// import org.bukkit.*;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.concurrent.ThreadLocalRandom;


public class App extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");
        Scheduler scheduler = new Scheduler(this, getServer().getScheduler());
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new FlyingCreepers(scheduler), this);
        manager.registerEvents(new FlyingSkeletons(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        //Player p = e.getPlayer();
        //World world = p.getWorld();
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
        //Vector new_vector = arrow.getVelocity().multiply(0.1);
        // e.getProjectile().setVelocity(new_vector);
    }



    // returns true with given probability between 0 and 100
    // always returns false for prob <= 0 and always true for prob >= 100
    public static boolean getRandTrue(int prob) {
        return ThreadLocalRandom.current().nextInt(0, 100) < prob;
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
