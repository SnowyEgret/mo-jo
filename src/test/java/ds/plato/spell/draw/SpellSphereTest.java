package ds.plato.spell.draw;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ds.plato.api.ISpell;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.draw.SpellSphere;
import ds.plato.pick.Pick;
import ds.plato.test.PlatoTest;
import ds.plato.undo.Transaction;

public class SpellSphereTest extends PlatoTest {

	HotbarSlot[] slotEntries;
	Pick[] picks;

	@Before
	public void setUp() {
		super.setUp();
		slotEntries = new HotbarSlot[] {new HotbarSlot(dirt, 0, 0)};
		picks = new Pick[] {new Pick(BlockPos.ORIGIN, dirt, null), new Pick(new BlockPos(9, 0, 0), dirt, null)};
		when(pickManager.isFinishedPicking()).thenReturn(true);
		when(undoManager.newTransaction()).thenReturn(new Transaction(undoManager));
	}

	@Test
	public void doNothing() {
	}

	//@Test
	public void invoke() {
		ISpell s = new SpellSphere(undoManager, selectionManager, pickManager);
		//s.invoke(picks, slotEntries);
		s.invoke(world, slotEntries);
		verify(world).setBlock(new BlockPos(9, 0, 0), dirt);
	}
}
