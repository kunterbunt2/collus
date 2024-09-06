package de.bushnaq.abdalla.pluvia.game.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import de.bushnaq.abdalla.pluvia.util.PersistentRandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPersistentRandomGenerator {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@BeforeEach
	void init(TestInfo testInfo) {
		logger.info(String.format("%s", testInfo.getDisplayName()));
	}

	@Test
	public void sortOneGame() {
		int							values[]	= new int[1000];
		PersistentRandomGenerator	rg1			= new PersistentRandomGenerator();
		PersistentRandomGenerator	rg2			= new PersistentRandomGenerator();
		for (int i = 0; i < 1000; i++) {
			int bound = 1 + (int) (Math.random() * 1000);
			values[i] = rg1.nextInt(bound);
		}
		rg2.set(rg1.getSeed(), rg1.getCalls());
		for (int i = 0; i < 1000; i++) {
			int bound = 1 + (int) (Math.random() * 1000);
			assertEquals(rg1.nextInt(bound), rg2.nextInt(bound), "expected random values to be same");
		}

	}

}
