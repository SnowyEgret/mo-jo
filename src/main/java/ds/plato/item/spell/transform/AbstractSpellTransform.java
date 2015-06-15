package ds.plato.item.spell.transform;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.IPlantable;
import ds.plato.Plato;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform() {
		super(1);
	}

	protected void transformSelections(IPlayer player, ITransform transformer) {
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

		if (selectionManager.getSelectionList().size() == 0) {
			return;
		}

		Jumper jumper = new Jumper(player);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		pickManager.clearPicks(player);
		List<UndoableSetBlock> setBlocks = new ArrayList<>();
		List<BlockPos> reselects = new ArrayList<>();
		for (Selection s : selections) {
			s = transformer.transform(s);
			BlockPos pos = s.getPos();
			IBlockState state = s.getState();
			if (state.getBlock() instanceof IPlantable) {
				// TODO only plant if block beneath can sustain a plant
				// if (world.getBlockState(pos).getBlock()
				// .canSustainPlant(world.getWorld(), pos, EnumFacing.UP, (IPlantable) state.getBlock())) {
				pos = pos.up();
				// } else {
				// continue;
				// }
			}
			jumper.setHeight(pos);
			setBlocks.add(new UndoableSetBlock(player.getWorld(), selectionManager, pos, state));
			reselects.add(pos);
		}

		jumper.jump();

		Transaction t = undoManager.newTransaction();
		for (UndoableSetBlock u : setBlocks) {
			t.add(u.set());
		}
		t.commit();

		selectionManager.select(player, reselects);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
