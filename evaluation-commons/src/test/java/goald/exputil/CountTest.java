package goald.exputil;

import org.junit.Test;

import goald.evaluation.response.SplitTimer;

public class CountTest {

	SplitTimer splitTimer = SplitTimer.create();

	@Test
	public void count() {

		splitTimer.begin();
		splitTimer.split("start");
		for (Long i = 0l; i < 140000l; i++) {

		}
		splitTimer.split("split");
		for (Long i = 0l; i < 1400000l; i++) {

		}
		splitTimer.split("split");
		for (Long i = 0l; i < 14000000l; i++) {

		}
		splitTimer.split("split");

		splitTimer.split("finish");
		System.out.println(splitTimer.result());

	}

}
