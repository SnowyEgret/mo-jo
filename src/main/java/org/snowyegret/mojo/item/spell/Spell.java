package org.snowyegret.mojo.item.spell;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.gui.IOverlayable;
import org.snowyegret.mojo.item.ItemBase;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.player.Player;

public abstract class Spell extends ItemBase implements IOverlayable {

	protected OverlayInfo info;
	private int numPicks;

	public Spell(int numPicks) {
		this.numPicks = numPicks;
		info = new OverlayInfo(this);
	}

	// FIXME Has no effect
//	@Override
//	public EnumAction getItemUseAction(ItemStack stack) {
//		return EnumAction.BLOCK;
//	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (world.isRemote) {
			return true;
		}
		Player player = new Player(playerIn);
		PickManager pickManager = player.getPickManager();
		pickManager.pick(pos, side);
		if (pickManager.isFinishedPicking()) {
			invoke(player);
		}
		return true;

	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		rollOver.add(info.getDescription());
	}

	public abstract void invoke(Player player);

	@SideOnly(Side.CLIENT)
	public OverlayInfo getOverlayInfo() {
		return info;
	}

	public int getNumPicks() {
		return numPicks;
	}

	// Object -------------------------------------------------------

	// For Staff.addSpell(). Only one spell of each type on a staff
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() == obj.getClass())
			return true;
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		return builder.toString();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
