package ds.plato.item.spell.other;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.undo.Transaction;
import ds.plato.world.IWorld;

public class SpellDrop extends Spell {


	public SpellDrop(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		boolean fill = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		Transaction transaction = undoManager.newTransaction();
		for (Selection s : selectionManager.getSelections()) {
			Block b = world.getBlock(s.getPos().add(0,-1,0));
			int drop = 0;
			while (b == Blocks.air) {
				drop++;
				Block nextBlockDown = world.getBlock(s.getPos().add(0,-drop-1,0));
				if (fill || nextBlockDown != Blocks.air) {
					transaction.add(new UndoableSetBlock(world, selectionManager, s.getPos().add(0, -drop, 0), s.getBlock()).set());
				}
				b = nextBlockDown;
			}
			selectionManager.deselect(world, s);
			if (deleteOriginal) {
				transaction.add(new UndoableSetBlock(world, selectionManager, s.getPos(), Blocks.air).set());
			}
		}
		transaction.commit();
		pickManager.clearPicks();
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

//	@Override
//	public IModelCustom getModel() {
//		return model;
//	}

}
