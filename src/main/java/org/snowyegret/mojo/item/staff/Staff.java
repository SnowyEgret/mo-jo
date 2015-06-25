package org.snowyegret.mojo.item.staff;

import java.util.List;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.gui.GuiHandler;
import org.snowyegret.mojo.item.ItemBase;
import org.snowyegret.mojo.item.spell.ISpell;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.player.Player;

public abstract class Staff extends ItemBase implements IStaff {

	static final int MAX_NUM_SPELLS = 9;
	//public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MoJo.ID+":staff", "inventory");

	// Item--------------------------------------------------------

	// Adds information to rollover in creative tab
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		if (isEmpty(stack)) {
			rollOver.add(EnumChatFormatting.RED + "No spells on staff");
		} else {
			rollOver.add(EnumChatFormatting.GREEN + " " + numSpells(stack) + " spells on staff");
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1; // return any value greater than zero
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float sx, float sy, float sz) {

		// To compare item stacks and their tags on both sides
		//System.out.println("tag=" + stack.getTagCompound());
		IPlayer player = Player.instance(playerIn);
		Modifiers modifiers = player.getModifiers();		

		// Return if called from the client thread
		if (world.isRemote) {
			return true;
		}

		// We are on the server side. Open staff gui if space bar is down
		if (modifiers.isPressed(Modifier.SPACE)) {
			player.openGui(GuiHandler.GUI_STAFF);
			return true;
		}

		// Get the current spell on this staff and use it
		if (!isEmpty(stack)) {
			Spell s = getSpell(stack, player.getPickManager());
			s.onItemUse(stack, playerIn, world, pos, side, sx, sy, sz);
			return true;
		}

		return false;
	}

	// IStaff ----------------------------------------------------------------------

	@Override
	public Spell getSpell(ItemStack stack, PickManager pickManager) {
		// System.out.println("tag=" + stack.getTagCompound());
		// Throwable().printStackTrace();
		if (isEmpty(stack)) {
			return null;
		}
		TagStaff t = new TagStaff(stack);
		Spell s = t.getSpell();
		if (s == null) {
			s = nextSpell(stack, pickManager);
		}
		return s;
	}

	@Override
	public Spell nextSpell(ItemStack stack, PickManager pickManager) {
		TagStaff t = new TagStaff(stack);
		Spell s = null;
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			if (t.getIndex() == MAX_NUM_SPELLS - 1) {
				t.setIndex(0);
			} else {
				t.incrementIndex(1);
			}
			s = t.getSpell();
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}

		// TODO ForgeEventHandler also runs on client side and onLivingUpdate calls player.getSpell -> staff.nextSpell
		// KeyHandler runs on client side and calls next spell when key tab is pressed
		// if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
		// System.out.println("Sending NextSpellMessage to server.");
		// Plato.network.sendToServer(new NextSpellMessage());
		// }
		return s;
	}

	@Override
	public ISpell prevSpell(ItemStack stack, PickManager pickManager) {
		TagStaff t = new TagStaff(stack);
		ISpell s = null;
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			if (t.getIndex() == 0) {
				t.setIndex(MAX_NUM_SPELLS - 1);
			} else {
				t.incrementIndex(-1);
			}
			s = t.getSpell();
			if (s == null) {
				continue;
			} else {
				pickManager.reset(s.getNumPicks());
				break;
			}
		}

		// KeyHandler runs on client side and calls previous spell when key ctrl-tab is pressed
		// if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
		// System.out.println("Sending PrevSpellMessage to server.");
		// Plato.network.sendToServer(new PrevSpellMessage());
		// }
		return s;
	}

	@Override
	public int numSpells(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		int numSpells = 0;
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			ISpell s = t.getSpell(i);
			if (s != null)
				numSpells++;
		}
		return numSpells;
	}

	@Override
	public boolean isEmpty(ItemStack stack) {
		TagStaff t = new TagStaff(stack);
		for (int i = 0; i < MAX_NUM_SPELLS; i++) {
			ISpell s = t.getSpell(i);
			if (s != null) {
				return false;
			}
		}
		return true;
	}
}
