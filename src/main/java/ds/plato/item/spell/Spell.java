package ds.plato.item.spell;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import ds.plato.item.ItemBase;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;
import ds.plato.world.WorldWrapper;

public abstract class Spell extends ItemBase implements ISpell {

	protected IUndo undoManager;
	protected ISelect selectionManager;
	protected IPick pickManager;
	protected String message;
	protected SpellInfo info;
	protected final String CTRL = "ctrl,";
	protected final String ALT = "alt,";
	protected final String SHIFT = "shift,";
	protected final String X = "X,";
	protected final String Y = "Y,";
	protected final String Z = "Z,";
	private int numPicks;

	public Spell(int numPicks, IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(selectionManager);
		this.numPicks = numPicks;
		this.undoManager = undoManager;
		this.selectionManager = selectionManager;
		this.pickManager = pickManager;
		info = new SpellInfo(this);
	}

	@Override
	public void onMouseClickLeft(ItemStack stack, BlockPos pos, EnumFacing sideHit) {

		IPlayer player = Player.instance();
		IWorld w = player.getWorld();

		// Shift replaces the current selections with a region.
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && selectionManager.size() != 0) {
			BlockPos lastPos = selectionManager.lastSelection().getPos();
			IBlockState firstState = selectionManager.firstSelection().getState();
			// Fix for: MultiPlayer: First selection is not included when shift selecting a region #75
			// In MP selections block was not set fast enough so position was rejected
			// in SelectionManager.select in test for block instanceof BlockSelected
			// resulting in the first selection being left unselected. Only clear if we
			// need to.
			if (selectionManager.size() > 1) {
				selectionManager.clearSelections(w);
			}

			for (Object o : BlockPos.getAllInBox(lastPos, pos)) {
				BlockPos p = (BlockPos) o;
				if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
					// Only select blocks similar to first block
					IBlockState state = w.getActualState(p);
					if (state == firstState) {
						selectionManager.select(w, p);
					}
				} else {
					selectionManager.select(w, p);
				}
			}
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			if (selectionManager.isSelected(pos)) {
				selectionManager.deselect(w, pos);
			} else {
				selectionManager.select(w, pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		selectionManager.clearSelections(w);
		selectionManager.select(w, pos);
	}

	// FIXME Has no effect
	// @Override
	// public EnumAction getItemUseAction(ItemStack stack) {
	// return EnumAction.NONE;
	// }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (world.isRemote) {
			return true;
		}
		System.out.println("selectionManager=" + selectionManager);
		IWorld w = new WorldWrapper(world);
		pickManager.pick(w, pos, side);
		if (pickManager.isFinishedPicking()) {
			invoke(w, new Player(playerIn));
		}
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List rollOver, boolean par4) {
		rollOver.add(info.getDescription());
	}

	// ISpell --------------------------------------------

	@Override
	public abstract void invoke(IWorld world, IPlayer player);

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public SpellInfo getInfo() {
		return info;
	}

	@Override
	public int getNumPicks() {
		return numPicks;
	}

	@Override
	public boolean isPicking() {
		return pickManager.isPicking();
	}

	@Override
	public void reset(IWorld world) {
		pickManager.clearPicks(world);
		pickManager.reset(numPicks);
		message = null;
	}

	// Object -------------------------------------------------------

	// For Staff.addSpell(). Only one spell of each type on a staff
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
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
}
