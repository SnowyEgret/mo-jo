package org.snowyegret.mojo.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;

import net.minecraft.util.BlockPos;
import ds.geom.matrix.TranslationMatrix;

public class SpellCopy extends AbstractSpellMatrix {

	public SpellCopy() {
		super(2);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IPlayer player) {
		Pick[] picks = player.getPickManager().getPicks();
		invoke(player, picks[0].getPos(), picks[1].getPos());
		// Point3d from = picks[0].point3d();
		// Point3d to = picks[1].point3d();
		// Vector3d v = new Vector3d();
		// v.sub(to, from);
		// Matrix4d matrix = new TranslationMatrix(v);
		// boolean deleteOriginal = modifiers.isPressed(Modifier.SHIFT);
		// transformSelections(player, matrix, deleteOriginal);
		// pickManager.repick(player);
	}

	public void invoke(IPlayer player, BlockPos from, BlockPos to) {
		Vector3d v = new Vector3d();
		v.sub(new Point3d(to.getX(), to.getY(), to.getZ()), new Point3d(from.getX(), from.getY(), from.getZ()));
		Matrix4d matrix = new TranslationMatrix(v);
		boolean deleteOriginal = player.getModifiers().isPressed(Modifier.SHIFT);
		transformSelections(player, matrix, deleteOriginal);
		player.getPickManager().repick(player);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}