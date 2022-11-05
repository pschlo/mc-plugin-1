package com.pschlo;

import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import java.util.Set;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

class WorldAwareBox extends BoundingBox {
    World world;

    public static WorldAwareBox of(Vector corner1, Vector corner2, World world) {
        WorldAwareBox box = (WorldAwareBox) BoundingBox.of(corner1, corner2);
        return box.setWorld(world);
    }

    public static WorldAwareBox of(Location corner1, Location corner2) {
        WorldAwareBox box = (WorldAwareBox) BoundingBox.of(corner1, corner2);
        return box.setWorld(corner1.getWorld());
    }

    public static WorldAwareBox of(Block corner1, Block corner2) {
        WorldAwareBox box = (WorldAwareBox) BoundingBox.of(corner1, corner2);
        return box.setWorld(corner1.getWorld());
    }

    public static WorldAwareBox of(Block block) {
        WorldAwareBox box = (WorldAwareBox) BoundingBox.of(block);
        return box.setWorld(block.getWorld());
    }

    public static WorldAwareBox of(Vector center, double x, double y, double z, World world) {
        WorldAwareBox box = (WorldAwareBox) BoundingBox.of(center, x, y, z);
        return box.setWorld(world);
    }

    public static WorldAwareBox of(Location center, double x, double y, double z) {
        WorldAwareBox box = (WorldAwareBox) BoundingBox.of(center, x, y, z);
        return box.setWorld(center.getWorld());
    }


    public WorldAwareBox setWorld(World world) {
        this.world = world;
        return this;
    }


    // stops counting after maxCount
    public BlockCountRes countBlocks(Material material, int maxCount) {
        if (maxCount < 1) throw new IllegalArgumentException("maxCount must be at least 1");
        int count = 0;
        loop:
        for (int x=getMinBlockX(); x<=getMaxBlockX(); x++)
            for (int y=getMinBlockY(); y<=getMaxBlockY(); y++)
                for (int z=getMinBlockZ(); z<=getMaxBlockZ(); z++) {
                    if (world.getBlockAt(x, y, z).getType() == material) {
                        count++;
                        if (count == maxCount)
                            break loop;
                    }
                }
        return new BlockCountRes(count, (int) getVolume());
    }

    public BlockCountRes countBlocks(Material material) {
        return countBlocks(material, Integer.MAX_VALUE);
    }

    public boolean contains(Material material) {
        return countBlocks(material, 1).count > 0;
    }

    public boolean containsOnly(Material material) {
        BlockCountRes res = countBlocks(material);
        return res.count == res.total;
    }

    public int getMinBlockX() {
        return Location.locToBlock(getMinX());
    }
    public int getMinBlockY() {
        return Location.locToBlock(getMinY());
    }
    public int getMinBlockZ() {
        return Location.locToBlock(getMinZ());
    }
    public int getMaxBlockX() {
        return Location.locToBlock(getMaxX());
    }
    public int getMaxBlockY() {
        return Location.locToBlock(getMaxY());
    }
    public int getMaxBlockZ() {
        return Location.locToBlock(getMaxZ());
    }
}

