package ds.plato.item.spell.transform;

import net.minecraft.init.Blocks;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellDelete extends AbstractSpellTransform {

	public SpellDelete(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		transformSelections(world, new ITransform() {
			@Override
			public Selection transform(Selection s) {
				// Create a copy here because we don't want to modify the selectionManager's selection list.
				//If we don't create a copy undo doesn't work
				return new Selection(s.getPos(), Blocks.air.getDefaultState());
			}
		});
	}
	
}
