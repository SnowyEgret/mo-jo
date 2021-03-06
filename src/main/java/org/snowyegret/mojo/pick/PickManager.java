package org.snowyegret.mojo.pick;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockHighlight;
import org.snowyegret.mojo.block.BlockHightlightTileEntity;
import org.snowyegret.mojo.message.client.PickMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.world.IWorld;

public class PickManager {

	private LinkedList<Pick> picks = new LinkedList<>();
	private LinkedList<Pick> lastPicks = new LinkedList<>();
	private int numPicks = 0;
	private Player player;

	public PickManager(Player player) {
		this.player = player;
	}

	public Pick pick(BlockPos pos, EnumFacing side) {
		Pick pick = pick(player.getWorld(), pos, side);
		player.sendMessage(new PickMessage(this));
		return pick;
	}

	public void clearPicks() {
		clearPicks(player.getWorld());
		player.sendMessage(new PickMessage(this));
	}

	public void repick() {
		repick(player.getWorld());
		player.sendMessage(new PickMessage(this));
	}

	// -------------------------------------------------------------------------

	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	public Pick getPick(BlockPos pos) {
		for (Pick p : picks) {
			if (pos.equals(p.getPos())) {
				return p;
			}
		}
		return null;
	}

	public boolean isPicking() {
		return picks.size() > 0 && !isFinishedPicking();
	}

	public boolean isFinishedPicking() {
		return (picks.size() == numPicks);
	}

	public void setNumPicks(int numPicks) {
		this.numPicks = numPicks;
	}

	public Pick firstPick() {
		return getPicks()[0];
	}

	public Pick lastPick() {
		try {
			return picks.getLast();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PickManager [numPicks=");
		builder.append(numPicks);
		builder.append(", picks=");
		builder.append(picks);
		builder.append("]");
		return builder.toString();
	}

	// Private -------------------------------------------------------------------------

	private Pick pick(IWorld world, BlockPos pos, EnumFacing side) {

		if (picks.size() == numPicks) {
			return null;
		}

		IBlockState state = world.getActualState(pos);
		BlockHightlightTileEntity tileEntity;

		if (state.getBlock() instanceof BlockHighlight) {
			tileEntity = (BlockHightlightTileEntity) world.getTileEntity(pos);
			state = tileEntity.getPrevState();
			// If the BlockSelected is left in the world after a crash, prevState will be null.
			// When selecting these blocks to delete or fill them, the previous state can simply be blockSelected
			if (state == null) {
				System.out.println("tileEntity.getPrevState() is null. Setting to BlockSelected default state.");
				state = MoJo.blockHighlight.getDefaultState();
			}
		}

		Pick pick = new Pick(pos, state, side);
		picks.add(pick);
		world.setState(pos, MoJo.blockHighlight.getDefaultState());
		tileEntity = (BlockHightlightTileEntity) world.getTileEntity(pos);
		tileEntity.setPrevState(state);
		tileEntity.setColor(BlockHighlight.COLOR_PICKED);

		return pick;
	}

	private void clearPicks(IWorld world) {
		for (Pick pick : getPicks()) {
			IBlockState state = world.getActualState(pick.getPos());
			// When picking a BlockSelected in an AbstractSpellSelect when selecting BlockSelected left
			// in world after a crash, there is no state on the tile entity.
			world.setState(pick.getPos(), pick.getState());
		}
		lastPicks.clear();
		lastPicks.addAll(picks);
		picks.clear();
	}

	private void repick(IWorld world) {
		if (lastPicks != null) {
			for (Pick p : lastPicks) {
				pick(world, p.getPos(), p.side);
			}
		}
	}
}
