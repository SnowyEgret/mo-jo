package org.snowyegret.mojo.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.util.Vec3;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.SelectionManager;

import ds.geom.GeomUtil;

public class SpellRotate90 extends AbstractSpellMatrix {

	public SpellRotate90() {
		super(1);
		info.addModifiers(Modifier.ALT, Modifier.X, Modifier.Y, Modifier.Z);
	}

	@Override
	public void invoke(IPlayer player) {

		Modifiers modifiers = player.getModifiers();
		boolean rotateAboutCentroid = modifiers.isPressed(Modifier.SHIFT);

		Pick[] picks = player.getPicks();
		Point3d center = null;
		if (rotateAboutCentroid) {
			Vec3 c = player.getSelectionManager().getCentroid();
			center = new Point3d(c.xCoord, c.yCoord, c.zCoord);
		} else {
			center = picks[0].point3d();
		}

		// First check for modifier keys, then use face to determine which plane to rotate in
		Matrix4d matrix;
		if (modifiers.isPressed(Modifier.X)) {
			matrix = GeomUtil.newRotX90Matrix(center);
		} else if (modifiers.isPressed(Modifier.Y)) {
			matrix = GeomUtil.newRotY90Matrix(center);
		} else if (modifiers.isPressed(Modifier.Z)) {
			matrix = GeomUtil.newRotZ90Matrix(center);
		} else {
			switch (picks[0].getPlane()) {
			case XY:
				matrix = GeomUtil.newRotZ90Matrix(center);
				break;
			case XZ:
				matrix = GeomUtil.newRotY90Matrix(center);
				break;
			case YZ:
				matrix = GeomUtil.newRotX90Matrix(center);
				break;
			default:
				matrix = GeomUtil.newRotY90Matrix(center);
				break;
			}
		}

		transformSelections(player, matrix);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
