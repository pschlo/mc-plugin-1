package com.pschlo;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {
    private final BukkitScheduler scheduler;
    private final Plugin plugin;

    public Scheduler(Plugin plugin, BukkitScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    public void runEvery(Runnable runnable, long period) {
        this.scheduler.runTaskTimer(this.plugin, runnable, 0, period);
    }
}
