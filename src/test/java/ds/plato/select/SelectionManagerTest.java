package ds.plato.select;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.BlockPos;

import org.junit.Before;
import org.junit.Test;

import ds.plato.api.ISelect;
import ds.plato.api.IWorld;
import ds.plato.test.PlatoTest;

public class SelectionManagerTest extends PlatoTest {
	
	ISelect m;
	IWorld w;
	
	@Before
	public void setUp() {
		super.setUp();
		w = newStubWorld();
		m = new SelectionManager(blockSelected);
	}
	
	@Test
	public void select() {
		//System.out.println("[SelectionManagerTest.select] m=" + m);
		assertThat(m.size(), is(0));
		Selection s = m.select(w, p1);
		assertThat(m.size(), is(1));
		assertThat(m.getSelection(p1), equalTo(s));
	}

	@Test
	public void select_notTwice() {
		m.select(w, p1);
		m.select(w, p1);
		assertThat(m.size(), is(1));
	}

	@Test
	public void select_setsBlocktoBlockSelected() {
		m.select(w, p1);
		assertEquals(blockSelected, w.getBlock(p1));
	}

	@Test
	public void deselect() {
		Selection s = m.select(w, p1);
		assertThat(m.size(), is(1));
		m.deselect(w, s);
		assertThat(m.size(), is(0));
	}

	@Test
	public void deselect_resetsBlock() {
		Selection s = m.select(w, p1);
		m.deselect(w, s);
		assertEquals(air, w.getBlock(p1));
	}

	@Test
	public void getSelections() {
		List selections = new ArrayList();
		selections.add(m.select(w, p1));
		selections.add(m.select(w, p2));
		selections.add(m.select(w, p3));
		assertThat(m.size(), is(3));
		//FIXME
//		assertThat(m.getSelections().length(), hasItems(selections));
//		assertThat(m.getSelections(), hasItems(selections));
//		assertThat(selections, allOf(selections));
	}

	@Test
	public void getSelection() {
		Selection s = m.select(w, p1);
		assertThat(m.getSelection(p1), equalTo(s));
	}

	@Test
	public void isSelected() {
		m.select(w, p1);
		assertThat(m.isSelected(p1), is(true));
	}

	@Test
	public void size() {
		m.select(w, p1);
		m.select(w, p2);
		m.select(w, p3);
		assertThat(m.size(), is(3));
	}

	@Test
	public void clearSelections() {
		m.select(w, p1);
		m.select(w, p2);
		m.select(w, p3);
		m.clearSelections(w);
		assertThat(m.size(), is(0));
	}

//	@Test
//	public void removeSelection() {
//		Selection[] ss = arrayOfThreeSelections();
//		m.addSelection(ss[0]);
//		m.addSelection(ss[1]);
//		m.addSelection(ss[2]);
//		assertThat(m.removeSelection(1, 0, 0), is(ss[1]));
//		assertThat(m.size(), is(2));
//	}

}