package com.pschlo;

import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;


class WorldAwareBox extends BoundingBox implements Iterable<Block> {
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


    // stops counting after findAtMost findings
    public BlockCountRes countBlocks(Material material, int findAtMost) {
        if (findAtMost < 1) throw new IllegalArgumentException("findAtMost must be at least 1");
        int numFound = 0;
        int numChecked = 0;
        for (Block block : this) {
            numChecked++;
            if (block.getType() == material) {
                numFound++;
                if (numFound == findAtMost)
                    break;
            }
        }
        return new BlockCountRes(numFound, numChecked);
    }

    public BlockCountRes countBlocks(Material material) {
        return countBlocks(material, Integer.MAX_VALUE);
    }

    public boolean contains(Material material) {
        return countBlocks(material, 1).numFound > 0;
    }

    public boolean containsOnly(Material material) {
        BlockCountRes res = countBlocks(material);
        return res.numFound == res.numChecked;
    }

    public static int locToBlock(double loc) {
        return Location.locToBlock(loc);
    }

    @Override
    public Iterator<Block> iterator() {
        return new BoxIterator(this);
    }

}

