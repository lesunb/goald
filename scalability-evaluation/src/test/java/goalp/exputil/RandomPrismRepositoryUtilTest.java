package goalp.exputil;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RandomPrismRepositoryUtilTest {
	Deque<String> spaceDeque;
	@Before
	public void setup(){
		String[] space = {"A","B", "C"};
		spaceDeque = new ArrayDeque<>(Arrays.asList(space));
	}
	
	@Test
	public void testGetCombinations0() {
		Deque<Deque<String>> result = RandomPrismRepositoryUtil.getCombinations(spaceDeque, 0);
		Assert.assertEquals(1, result.size());
	}
	
	@Test
	public void testGetCombinations1() {
		Deque<Deque<String>> result = RandomPrismRepositoryUtil.getCombinations(spaceDeque, 1);
		Assert.assertEquals(3,result.size());
	}
	
	@Test
	public void testGetCombinations2() {	
		Deque<Deque<String>> result = RandomPrismRepositoryUtil.getCombinations(spaceDeque, 2);
		Assert.assertEquals(result.size(), 3);
	}
	
	
	@Test
	public void testGetCombinations3() {
		Deque<Deque<String>> result = RandomPrismRepositoryUtil.getCombinations(spaceDeque, 3);
		Assert.assertEquals(result.size(), 1);
	}
}
