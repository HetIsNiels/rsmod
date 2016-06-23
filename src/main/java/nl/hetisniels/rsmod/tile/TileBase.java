package nl.hetisniels.rsmod.tile;

import net.minecraft.tileentity.TileEntity;

abstract class TileBase extends TileEntity {
	@Override
	public double getMaxRenderDistanceSquared() {
		return 32 * 32;
	}
}
