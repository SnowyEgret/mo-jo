package ds.plato.event;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.item.IItem;
import ds.plato.item.spell.transform.SpellFill;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class MouseHandler {

	private IUndo undoManager;
	private ISelect selectionManager;
	private IPick pickManager;
	private boolean orbiting;
	private Vec3 centroid;

	public MouseHandler(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		this.undoManager = undoManager;
		this.selectionManager = selectionManager;
		this.pickManager = pickManager;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseEvent(MouseEvent e) {

		// Do nothing if returning from a gui
		// TODO Seems not to work in multiplayer
		if (Minecraft.getMinecraft().isGamePaused()) {
			return;
		}

		IPlayer player = Player.getPlayer();

		// EntityPlayerSP pl = Minecraft.getMinecraft().thePlayer;
		IWorld w = player.getWorld();
		MovingObjectPosition cursor = Minecraft.getMinecraft().objectMouseOver;

		// Orbit if middle mouse button is down
		if (e.button == -1) {
			if (orbiting) {
				player.orbitAround(centroid, e.dx, e.dy);
			}
			//Do not cancel
			return;
		}

		// Clear selections or picks if player clicks on sky
		// Do not cancel event. Fix for Left click stuck in loop when block is broken against sky #60
		if (cursor.typeOfHit == MovingObjectType.MISS) {
			if (e.button == 0) {
				selectionManager.clearSelections(w);
				e.setCanceled(true);
				return;
			}
			if (e.button == 1) {
				pickManager.clearPicks();
				e.setCanceled(true);
				return;
			}
		}

		// Do nothing if player clicks on a mob
		if (cursor.typeOfHit == MovingObjectType.ENTITY) {
			return;
		}

		// Do nothing if player is holding nothing.
		ItemStack stack = player.getHeldItemStack();
		if (stack == null) {
			return;
		}
		Item heldItem = stack.getItem();

		// Select on mouse click left when player is holding a staff or spell
		// Picking is handled by onItemUse. Do not cancel event on mouse click right.
		if (heldItem instanceof IItem) {
			// System.out.println(item);
			if (e.button == 0) {
				if (e.buttonstate) {
					((IItem) heldItem).onMouseClickLeft(stack, cursor.getBlockPos(), cursor.sideHit);
				}
				e.setCanceled(true);
				return;
			}
			if (heldItem instanceof IItem) { 
				if (e.button == 0) {
					if (e.buttonstate) {
						((IItem) heldItem).onMouseClickLeft(stack, cursor.getBlockPos(), cursor.sideHit);
					}
					e.setCanceled(true);
					return;
				}
				// Orbit around selection centroid
				if (e.button == 2) {
					if (e.buttonstate) {
						if (selectionManager.size() != 0) {
							orbiting = true;
							centroid = selectionManager.getCentroid();						
						}
					} else {
						orbiting = false;
					}
					e.setCanceled(true);
					return;
				}
			}
		}

		// Fill the selections on mouse click right on a selected block and player is holding a block
		if (heldItem instanceof ItemBlock) {
			if (e.button == 1) {
				BlockPos pos = cursor.getBlockPos();
				if (selectionManager.isSelected(pos)) {
					Block b = ((ItemBlock) heldItem).getBlock();
					new SpellFill(undoManager, selectionManager, pickManager).invoke(w, new HotbarSlot(b, 0));
					e.setCanceled(true);
				}
			}
			return;
		}
	}
}
