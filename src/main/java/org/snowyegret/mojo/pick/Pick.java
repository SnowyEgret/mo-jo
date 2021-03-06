package org.snowyegret.mojo.pick;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.geom.EnumPlane;

public class Pick {

	private static final String KEY_POS = "pos";
	private static final String KEY_STATE = "state";

	private BlockPos pos;
	public EnumFacing side;
	private IBlockState state;

	public Pick(BlockPos pos, IBlockState state, EnumFacing side) {
		this.pos = pos;
		this.state = state;
		this.side = side;
	}

	public Point3d point3d() {
		return new Point3d(pos.getX(), pos.getY(), pos.getZ());
	}

	public Point3i point3i() {
		return new Point3i(pos.getX(), pos.getY(), pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}

	public EnumFacing getSide() {
		return side;
	}

	public void setState(IBlockState state) {
		this.state = state;
	}

	public IBlockState getState() {
		return state;
	}

	public EnumPlane getPlane() {
		switch (side) {
		case UP:
			return EnumPlane.HORIZONTAL_XZ;
		case DOWN:
			return EnumPlane.HORIZONTAL_XZ;
		case EAST:
			return EnumPlane.VERTICAL_YZ_NORTH_SOUTH;
		case WEST:
			return EnumPlane.VERTICAL_YZ_NORTH_SOUTH;
		case NORTH:
			return EnumPlane.VERTICAL_XY_EAST_WEST;
		case SOUTH:
			return EnumPlane.VERTICAL_XY_EAST_WEST;
		default:
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pick [pos=");
		builder.append(pos);
		builder.append(", side=");
		builder.append(side);
		builder.append(", state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}

	// TODO check state.equals (write a test case)
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pick other = (Pick) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (side != other.side)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	// TODO Interface for reading and writing NBT for transaction and selections #259
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong(KEY_POS, pos.toLong());
		tag.setInteger(KEY_STATE, Block.getStateId(state));
		return tag;
	}
}
