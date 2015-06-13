package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellSelectFloor extends AbstractSpellSelect {

	public SpellSelectFloor() {
		super(Select.horizontal);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		IPick pickManager = player.getPickManager();
		EnumFacing side = pickManager.getPicks()[0].side;
		switch (side) {
		case UP:
			setConditions(new IsOnGround());
			break;
		case DOWN:
			setConditions(new IsOnCeiling());
			break;
			//$CASES-OMITTED$
		default:
			return;
		}
		super.invoke(world, player);
	}
	
}
