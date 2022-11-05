package com.pschlo;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.bukkit.World;
import org.bukkit.block.Block;


public class BoxIterator implements Iterator<Block>{
    World world;
    int minX, minY, minZ;
    int maxX, maxY, maxZ;
    int x, y, z;
    boolean hasNext;

    public BoxIterator(WorldAwareBox box) {
        this.world = box.world;

        this.minX = WorldAwareBox.locToBlock(box.getMinX());
        this.minY = WorldAwareBox.locToBlock(box.getMinY());
        this.minZ = WorldAwareBox.locToBlock(box.getMinZ());

        this.maxX = WorldAwareBox.locToBlock(box.getMaxX());
        this.maxY = WorldAwareBox.locToBlock(box.getMaxY());
        this.maxZ = WorldAwareBox.locToBlock(box.getMaxZ());

        this.x = minX;
        this.y = minY;
        this.z = minZ;

        this.hasNext = true;
    }


    public boolean hasNext() {
        return hasNext;
    }

    public Block next() {
        if (!(hasNext))
            throw new NoSuchElementException();

        Block block = world.getBlockAt(x, y, z);

        if (z < maxZ) {
            z++;
            return block;
        }

        z = minZ;
        if (y < maxY) {
            y++;
            return block;
        }

        y = minY;
        if (x < maxX) {
            x++;
            return block;
        }

        this.hasNext = false;
        return block;
    }
}
