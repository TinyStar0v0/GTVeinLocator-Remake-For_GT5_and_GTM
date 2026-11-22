package pers.gwyog.gtveinlocator;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.sinthoras.visualprospecting.VisualProspecting_API;
import com.sinthoras.visualprospecting.database.OreVeinPosition;
import com.sinthoras.visualprospecting.database.UndergroundFluidPosition;

import pers.gwyog.gtveinlocator.network.GTVLNetwork;
import pers.gwyog.gtveinlocator.network.LocatorMessage;

final class VPIntegration {

    private VPIntegration() {}

    static void publish(EntityPlayer player, List<GT5Scanner.FoundVein> found, int radiusBlocks) {
        if (!(player instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
        try {
            int dim = player.dimension;
            int x = (int) Math.floor(player.posX);
            int z = (int) Math.floor(player.posZ);
            for (GT5Scanner.FoundVein vein : found) {
                VisualProspecting_API.LogicalServer.notifyOreGeneration(dim, vein.x, vein.z, vein.name);
            }
            List<OreVeinPosition> ores = VisualProspecting_API.LogicalServer
                .prospectOreVeinsWithinRadius(dim, x, z, radiusBlocks);
            List<UndergroundFluidPosition> fluids = VisualProspecting_API.LogicalServer
                .prospectUndergroundFluidsWithingRadius(player.worldObj, x, z, radiusBlocks);
            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(serverPlayer, ores, fluids);
        } catch (Throwable throwable) {
            GTVLNetwork.INSTANCE.sendTo(new LocatorMessage(found), serverPlayer);
        }
    }

    static void sendFallback(EntityPlayer player, List<GT5Scanner.FoundVein> found) {
        if (player instanceof EntityPlayerMP) {
            GTVLNetwork.INSTANCE.sendTo(new LocatorMessage(found), (EntityPlayerMP) player);
        }
    }
}
