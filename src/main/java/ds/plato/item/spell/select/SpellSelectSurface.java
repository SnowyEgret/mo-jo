package ds.plato.item.spell.select;

import net.minecraft.util.EnumFacing;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;

public class SpellSelectSurface extends AbstractSpellSelect {

	public SpellSelectSurface() {
		super(Select.all);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void invoke(IPlayer player) {
		IPick pickManager = player.getPickManager();
		EnumFacing side = pickManager.getPicks()[0].side;
		boolean ignoreSide = false;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			positions = Select.all;
			ignoreSide = true;
		} else {
			positions = Select.planeForSide(side);
		}
		setConditions(new IsOnSurface(side, ignoreSide));	
		super.invoke(player);
	}
	
}
