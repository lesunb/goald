package goald.exputil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * @author grodrigues
 *
 */
public class RandomPrismRepositoryUtil {

	
	public static String concat(Deque<String> deque){
		StringBuilder sb = new StringBuilder();
		for(String str: deque){
			sb.append(str);
		}
		if(sb.length()>0){
			return sb.toString();
		}
		return null;
	}
	
	@SafeVarargs
	public static <T> T randomChoice(float prob, T... choises) {
		if(prob > Math.random()) {
			return choises[0];
		}else {
			return choises[1];
		}
	}
	
	/**
	 * Get a k-combination of elements in Deque 'contexts'
	 * @param variables
	 * @return
	 */
	public static Deque<Deque<String>> getCombinations (final Deque<String> elements, int k){
		if(k > elements.size()){
			throw new IllegalStateException("Can't make a k-combination of elements, if |elements| < k");
		}
		Deque<Deque<String>> combination = new ArrayDeque<Deque<String>>();
		 if(k == 0){
			 //if k == 0, one empty deque
			 combination.add(new ArrayDeque<String>());
			 return combination;
		} else{
			
			Deque<String> myElements = copy(elements);
			//remove the first
			String first = myElements.pop();
			
			//sub combinations. with and without the fist element
			Deque<Deque<String>> subCombinationA = getCombinations(myElements, k-1 );
			//with first
			subCombinationA.forEach(element ->{
				Deque<String> newElement = new ArrayDeque<>(element);
				newElement.push(first);
				combination.add(newElement);
			});
			if(myElements.size() >= k){
				combination.addAll(getCombinations(myElements, k));
			}
			
		}
		return combination;		
	}
	
	public static Deque<String> copy(Deque<String> origin){
		Deque<String> copy = new ArrayDeque<String>();
		origin.forEach(element ->{
			copy.push(element);
		});
		return copy;
	}
	
	public static List<String> listOfPElements(String[] origin, int p){
		Set<String> setOfElements = getPElements( Arrays.asList(origin), p);
		return new ArrayList<>(setOfElements);		
	}
	
	public static Set<String> getPElements(final List<String> origin, int p){
		Set<String> result = new HashSet<String>();
		while(result.size() < p){
			Double luck = Math.floor(Math.random()*origin.size());
			result.add(origin.get(luck.intValue()));
		}
		return result;
	}

	
}
