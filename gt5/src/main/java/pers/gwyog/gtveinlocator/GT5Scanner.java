package pers.gwyog.gtveinlocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.util.GT5OreLayerHelper;

public final class GT5Scanner {

    private static final int VEIN_GRID = 48;

    private GT5Scanner() {}

    public static List<FoundVein> scan(EntityPlayer player, int radiusBlocks) {
        World world = player.worldObj;
        int minX = (int) Math.floor(player.posX - radiusBlocks);
        int maxX = (int) Math.floor(player.posX + radiusBlocks);
        int minZ = (int) Math.floor(player.posZ - radiusBlocks);
        int maxZ = (int) Math.floor(player.posZ + radiusBlocks);
        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        Map<Long, FoundVein> found = new HashMap<>();
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = world.getChunkFromChunkCoords(cx, cz);
                for (int y = 0; y < 256; y++) {
                    for (int lx = 0; lx < 16; lx++) {
                        for (int lz = 0; lz < 16; lz++) {
                            int x = (cx << 4) + lx;
                            int z = (cz << 4) + lz;
                            if (x < minX || x > maxX || z < minZ || z > maxZ) {
                                continue;
                            }
                            Block block = chunk.getBlock(lx, y, lz);
                            int meta = chunk.getBlockMetadata(lx, y, lz);
                            if (!OreManager.isOre(block, meta)
                                .orElse(false)) {
                                continue;
                            }
                            try (OreInfo<?> info = OreManager.getOreInfo(world, x, y, z)) {
                                if (info == null || info.material == null || !info.isNatural) {
                                    continue;
                                }
                                String name = veinName(info.material);
                                if (name == null) {
                                    continue;
                                }
                                int centerX = center(x);
                                int centerZ = center(z);
                                long key = ((long) centerX << 32) ^ (centerZ & 0xffffffffL);
                                found.putIfAbsent(key, new FoundVein(name, centerX, y, centerZ));
                            } catch (Throwable ignored) {}
                        }
                    }
                }
            }
        }
        return new ArrayList<>(found.values());
    }

    private static String veinName(IOreMaterial material) {
        for (GT5OreLayerHelper.OreLayerWrapper wrapper : GT5OreLayerHelper.getOreVeinsByName()
            .values()) {
            if (wrapper.containsOre(material)) {
                return wrapper.veinName;
            }
        }
        return null;
    }

    private static int center(double ignored, int block) {
        return 24 + VEIN_GRID * Math.round((block - 24) / (float) VEIN_GRID);
    }

    private static int center(int block) {
        return 24 + VEIN_GRID * Math.round((block - 24) / (float) VEIN_GRID);
    }

    public static final class FoundVein {

        public final String name;
        public final int x;
        public final int y;
        public final int z;

        public FoundVein(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
