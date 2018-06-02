package goald.dam.model.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtils<T> {
	
	public Set<T> diffSet(Set<T> setA, Set<T> setB) {
		Set<T> result = new HashSet<T>();
		for(T a: setA) {
			if(!setB.contains(a)) {
				result.add(a);
			}
		}
		return result;
	}
}
