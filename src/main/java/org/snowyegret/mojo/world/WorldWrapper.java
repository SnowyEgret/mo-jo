package org.snowyegret.mojo.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

// This wrapper is left over from early stage unit tests which have been abandoned
// It probably should be dumped because it mostly just delegates to field world
// Kept for now because it enables a global setBlockState policy
public class WorldWrapper implements IWorld {

	private World world;

	public WorldWrapper(World world) {
		this.world = world;
	}

	@Override
	public Block getBlock(BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		return b.getBlock();
	}

	@Override
	public void setState(BlockPos pos, IBlockState state) {
		// TODO is this any different than flag 3?
		// world.setBlockState(pos, state);
		world.setBlockState(pos, state, 1);
		world.markBlockForUpdate(pos);
		//world.updateEntities();
		// world.markChunkDirty(pos, null);
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public IBlockState getState(BlockPos pos) {
		return world.getBlockState(pos);
	}

	@Override
	public IBlockState getActualState(BlockPos p) {
		IBlockState state = getState(p);
		return state.getBlock().getActualState(state, world, p);
	}

	@Override
	public void update() {
		// System.out.println("Not yet implemented");
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return world.getTileEntity(pos);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WorldWrapper [world=");
		builder.append(world);
		builder.append("]");
		return builder.toString();
	}
}
