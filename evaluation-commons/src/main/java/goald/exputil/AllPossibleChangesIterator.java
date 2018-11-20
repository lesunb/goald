package goald.exputil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import goald.exputil.AllPossibleChangesIterator.CtxChangesIterator;
import goald.model.ContextChange;
import goald.model.util.ContextChangeBuilder;

public class AllPossibleChangesIterator implements Iterator<CtxChangesIterator>, Iterable<CtxChangesIterator> {

	private String[] ctxs;
	int last = 0, actual = 0;
	
	private AllPossibleChangesIterator(String ...ctx) {
		ctxs = ctx;
		last = 1 << ctx.length;
	}
	
	public static AllPossibleChangesIterator init(String ...ctx) {
		return new AllPossibleChangesIterator(ctx);
	}
	
	public static AllPossibleChangesIterator init(List<String> ctxs) {
		String[] a = new String[ctxs.size()];
		return new AllPossibleChangesIterator(ctxs.toArray(a));
	}
	
	public List<String> getContext(int n) {
		int mask = n;
		byte bitMask = 0x01;
		List<String> result = new ArrayList<>();
		
		for(int i = 0; i<ctxs.length && mask > 0; i++) {
			byte b = (byte) (mask & bitMask);
			if(b == bitMask){
				result.add(ctxs[i]);
			}
			mask = mask>>1;
		}
		return result;
	}
	
	public CtxChangesIterator getContextChangesIterator(int n) {
		return new CtxChangesIterator(getContext(n));
	}

	@Override
	public boolean hasNext() {
		return actual < last;
	}

	@Override
	public CtxChangesIterator next() {
		return new CtxChangesIterator(getContext(actual++));
	}

	@Override
	public Iterator<CtxChangesIterator> iterator() {
		return this;
	}
	
	
	public class CtxChangesIterator {
		List<String> actualCtxs;
		
		private CtxChangesIterator(List<String> actualCtxs) {
			this.actualCtxs = actualCtxs;
		}
		
		public List<String> getCtxs(){
			return this.actualCtxs;
		}
		
		public List<ContextChange> getCtxChanges(){
			List<ContextChange> possibleChanges = new ArrayList<>();
			for(String ctx: ctxs) {
				ContextChangeBuilder builder = ContextChangeBuilder.create();
				if(this.actualCtxs.contains(ctx)) {					
					possibleChanges.add(builder.remove(ctx).build());
				} else {
					possibleChanges.add(builder.add(ctx).build());
				}
			}
			return possibleChanges;
		}
	}
}
