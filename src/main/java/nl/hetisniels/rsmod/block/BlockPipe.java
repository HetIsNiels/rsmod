package nl.hetisniels.rsmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import nl.hetisniels.rsmod.RSMod;
import nl.hetisniels.rsmod.tile.TilePipe;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPipe extends BlockBase implements IBlockHighlight, ITileEntityProvider {
	private static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(4 * (1F / 16F), 4 * (1F / 16F), 4 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F));
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(4 * (1F / 16F), 4 * (1F / 16F), 0 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F), 4 * (1F / 16F));
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(12 * (1F / 16F), 4 * (1F / 16F), 4 * (1F / 16F), 16 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F));
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(4 * (1F / 16F), 4 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F), 16 * (1F / 16F));
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0 * (1F / 16F), 4 * (1F / 16F), 4 * (1F / 16F), 4 * (1F / 16F), 12 * (1F / 16F), 12 * (1F / 16F));
	private static final AxisAlignedBB AABB_UP = new AxisAlignedBB(4 * (1F / 16F), 12 * (1F / 16F), 4 * (1F / 16F), 12 * (1F / 16F), 16 * (1F / 16F), 12 * (1F / 16F));
	private static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(4 * (1F / 16F), 0 * (1F / 16F), 4 * (1F / 16F), 12 * (1F / 16F), 4 * (1F / 16F), 12 * (1F / 16F));

	private static final PropertyBool NORTH = PropertyBool.create("north");
	private static final PropertyBool EAST = PropertyBool.create("east");
	private static final PropertyBool SOUTH = PropertyBool.create("south");
	private static final PropertyBool WEST = PropertyBool.create("west");
	private static final PropertyBool UP = PropertyBool.create("up");
	private static final PropertyBool DOWN = PropertyBool.create("down");

	public BlockPipe(String name) {
		super(name);

		setHardness(0.8F);
		setCreativeTab(RSMod.CREATIVE_TAB);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePipe();
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH,
				EAST,
				SOUTH,
				WEST,
				UP,
				DOWN);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(NORTH, hasConnectionWith(world, pos.north(), EnumFacing.SOUTH))
				.withProperty(EAST, hasConnectionWith(world, pos.east(), EnumFacing.WEST))
				.withProperty(SOUTH, hasConnectionWith(world, pos.south(), EnumFacing.NORTH))
				.withProperty(WEST, hasConnectionWith(world, pos.west(), EnumFacing.EAST))
				.withProperty(UP, hasConnectionWith(world, pos.up(), EnumFacing.DOWN))
				.withProperty(DOWN, hasConnectionWith(world, pos.down(), EnumFacing.UP));
	}

	private static boolean hasConnectionWith(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return (world.getBlockState(pos).getBlock().hasTileEntity() &&
				world.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) ||
				world.getBlockState(pos).getBlock() instanceof BlockPipe;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Nullable
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d origin, Vec3d direction) {
		state = getActualState(state, world, pos);

		RayTraceResult result = AABB_BASE.expandXyz(0.01).offset(pos).calculateIntercept(origin, direction);

		if (result == null && state.getValue(NORTH))
			result = AABB_NORTH.offset(pos).calculateIntercept(origin, direction);

		if (result == null && state.getValue(EAST))
			result = AABB_EAST.offset(pos).calculateIntercept(origin, direction);

		if (result == null && state.getValue(SOUTH))
			result = AABB_SOUTH.offset(pos).calculateIntercept(origin, direction);

		if (result == null && state.getValue(WEST))
			result = AABB_WEST.offset(pos).calculateIntercept(origin, direction);

		if (result == null && state.getValue(UP))
			result = AABB_UP.offset(pos).calculateIntercept(origin, direction);

		if (result == null && state.getValue(DOWN))
			result = AABB_DOWN.offset(pos).calculateIntercept(origin, direction);

		return result == null ? null : new RayTraceResult(result.hitVec.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), result.sideHit, pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity) {
		state = getActualState(state, world, pos);

		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE);

		if (state.getValue(NORTH))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH);

		if (state.getValue(EAST))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST);

		if (state.getValue(SOUTH))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH);

		if (state.getValue(WEST))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST);

		if (state.getValue(UP))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP);

		if (state.getValue(DOWN))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return super.getBoundingBox(state, source, pos);
	}

	public boolean drawBlockHighlight(World world, EntityPlayer player, BlockPos blockPos, Block block, float partialTicks) {
		IBlockState state = player.worldObj.getBlockState(blockPos).getActualState(world, blockPos);

		if (!(block instanceof BlockPipe) || !(state.getBlock() instanceof BlockPipe))
			return false;

		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);

		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

		//fixme when mappings are updated
		//todo render own lines instead of multiple boxes

		/*RenderGlobal.drawSelectionBoundingBox(AABB_BASE.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));

		if (state.getValue(NORTH))
			RenderGlobal.drawSelectionBoundingBox(AABB_NORTH.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));

		if (state.getValue(EAST))
			RenderGlobal.drawSelectionBoundingBox(AABB_EAST.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));

		if (state.getValue(SOUTH))
			RenderGlobal.drawSelectionBoundingBox(AABB_SOUTH.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));

		if (state.getValue(WEST))
			RenderGlobal.drawSelectionBoundingBox(AABB_WEST.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));

		if (state.getValue(UP))
			RenderGlobal.drawSelectionBoundingBox(AABB_UP.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));

		if (state.getValue(DOWN))
			RenderGlobal.drawSelectionBoundingBox(AABB_DOWN.expandXyz(0.001).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-d0, -d1, -d2));*/

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);

		return false; //fixme change to true when fixed
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TilePipe();
	}
}
