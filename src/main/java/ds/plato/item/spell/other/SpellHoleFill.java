package ds.plato.item.spell.other;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Sets;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.select.Select;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.IUndoable;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public class SpellHoleFill extends Spell {

	public SpellHoleFill() {
		super(1);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.water_bucket };
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		//IUndo undoManager = player.getUndoManager();

		boolean isHorizontal = modifiers.isPressed(Modifier.CTRL);
		boolean useBlockInHotbar = modifiers.isPressed(Modifier.SHIFT);
		
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		pickManager.clearPicks(player);
		Set<IUndoable> setBlocks = Sets.newHashSet();
		for (Selection s : selections) {
			BlockPos[] pos = isHorizontal ? Select.HORIZONTAL : Select.BELOW_INCLUSIVE;
			for (BlockPos p : pos) {
				p = p.add(s.getPos());
				Block b = player.getWorld().getBlock(p);
				if (b == Blocks.air || b == Blocks.water) {
					if (useBlockInHotbar) {
						setBlocks.add(new UndoableSetBlock(player.getWorld(), p, player.getHotbar().firstBlock()));
					} else {
						setBlocks.add(new UndoableSetBlock(player.getWorld(), p, s.getState()));
					}
				}
			}
		}
		//Transaction t = undoManager.newTransaction();
		Transaction t = new Transaction(player);
		t.addAll(setBlocks);
		t.commit();
	}
	
}
