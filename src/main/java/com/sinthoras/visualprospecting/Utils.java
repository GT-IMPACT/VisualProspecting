package com.sinthoras.visualprospecting;

import com.sinthoras.visualprospecting.hooks.HooksClient;

import cpw.mods.fml.common.Loader;

public class Utils {

    public static boolean isJourneyMapInstalled() {
        return Loader.isModLoaded("journeymap");
    }

    public static int coordBlockToChunk(int blockCoord) {
        return blockCoord < 0 ? -((-blockCoord - 1) >> 4) - 1 : blockCoord >> 4;
    }

    public static int coordChunkToBlock(int chunkCoord) {
        return chunkCoord < 0 ? -((-chunkCoord) << 4) : chunkCoord << 4;
    }

    public static long chunkCoordsToKey(int chunkX, int chunkZ) {
        return (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
    }

    public static int mapToCenterOreChunkCoord(final int chunkCoord) {
        if (chunkCoord >= 0) {
            return chunkCoord - (chunkCoord % 3) + 1;
        } else {
            return chunkCoord - (chunkCoord % 3) - 1;
        }
    }

    public static int mapToCornerUndergroundFluidChunkCoord(final int chunkCoord) {
        return chunkCoord & 0xFFFFFFF8;
    }

    public static double journeyMapScaleToLinear(final int jzoom) {
        return Math.pow(2, jzoom);
    }

    public static boolean isLogicalClient() {
        return VPMod.proxy instanceof HooksClient;
    }

}
