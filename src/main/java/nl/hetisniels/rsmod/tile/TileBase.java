package nl.hetisniels.rsmod.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileBase extends TileEntity {
    @Override
    public double getMaxRenderDistanceSquared() {
        return 32 * 32;
    }

    public void dropItems(World world, BlockPos pos) {

    }
}
