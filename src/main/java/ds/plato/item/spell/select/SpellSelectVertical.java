package ds.plato.item.spell.select;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.player.IPlayer;

public class SpellSelectVertical extends AbstractSpellSelect {

	public SpellSelectVertical() {
		super(Select.EAST_WEST);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IPlayer player) {
		positions = player.getModifiers().isPressed(Modifier.SHIFT) ? Select.NORTH_SOUTH : Select.EAST_WEST;
		super.invoke(player);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
	
}
