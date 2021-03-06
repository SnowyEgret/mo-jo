package org.snowyegret.mojo.item.spell.other;

import java.util.Arrays;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

import org.snowyegret.geom.IntegerDomain;
import org.snowyegret.geom.VoxelSet;

public class SpellMengerSponge extends Spell {

	private int level = 0;
	private List<BlockPos> deletes = Lists.newArrayList();

	public SpellMengerSponge() {
		super(1);
	}

	@Override
	public void invoke(Player player) {
		// TODO use enclosing cube
		delete(player.getSelectionManager().voxelSet());

		player.clearSelections();
		player.clearPicks();

		IBlockState air = Blocks.air.getDefaultState();
		List<IUndoable> undoables = Lists.newArrayList();
		for (BlockPos pos : deletes) {
			undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), air));
		}
		player.getTransactionManager().doTransaction(undoables);
	}

	private void delete(VoxelSet voxels) {

		// Run through this set testing for containment in each sub domain. Depending on which domain it is contained
		// by, add it to the set of points to be set to air. Recurse on each sub voxel set.
		level++;
		Iterable<IntegerDomain> domains = voxels.divideDomain(3);
		List<Integer> domainsToDelete = Arrays.asList(4, 10, 12, 13, 14, 16, 22);
		for (Point3i p : voxels) {
			IntegerDomain domain = null;
			int i = 0;
			for (IntegerDomain d : domains) {
				if (d.contains(p)) {
					domain = d;
					domain.count = i;
					break;
				}
				i++;
			}
			if (domainsToDelete.contains(domain.count)) {
				deletes.add(new BlockPos(p.x, p.y, p.z));
			}
		}

		for (VoxelSet set : voxels.divide(3)) {
			if (set.size() >= 9) {
				delete(set);
			}
		}

		level--;
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
