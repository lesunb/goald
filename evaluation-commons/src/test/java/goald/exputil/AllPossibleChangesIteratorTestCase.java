package goald.exputil; 

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.exputil.AllPossibleChangesIterator;
import goald.exputil.AllPossibleChangesIterator.CtxChangesIterator;
import goald.model.ContextChange;
import goald.model.ContextChange.OP;

public class AllPossibleChangesIteratorTestCase {

	public AllPossibleChangesIterator it = AllPossibleChangesIterator.init("1","2","3");
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testGetZeroCtx() {
		List<String> ctxs = it.getContext(0);
		assertEquals(0, ctxs.size());
	}
	
	@Test
	public void testGetFirstCtx() {
		List<String> ctxs = it.getContext(1);
		assertEquals(1, ctxs.size());
		assertEquals("1", ctxs.get(0));
	}
	
	@Test
	public void testCountAllCtx() {
		int counter = 0;
		List<String> last = null;
		for(CtxChangesIterator ctxs: it) {
			counter++;
			last = ctxs.getCtxs();
		}
		assertEquals(8, counter);
		assertEquals(3, last.size());
	}

	@Test
	public void testCtxChangesIterator() {
		CtxChangesIterator ite = it.getContextChangesIterator(3);
		List<ContextChange> changes = ite.getCtxChanges();
		assertEquals(3, ite.getCtxChanges().size());
		
		assertEquals(OP.REMOVED, changes.get(0).getOp());
		assertEquals("1", changes.get(0).getLabel());
		assertEquals(OP.REMOVED, changes.get(1).getOp());
		assertEquals(OP.ADDED, changes.get(2).getOp());
	}

}
