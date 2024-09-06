package de.bushnaq.abdalla.pluvia.util;

import java.util.Random;

/**
 * A random generator that can be resumed later
 *
 * @author kunterbunt
 *
 */
public class PersistentRandomGenerator {
	protected int		calls	= 0;
	protected Random	rand	= null;
	protected int		seed	= 0;

	public PersistentRandomGenerator() {
		rand = new Random(seed);
	}

	public int getCalls() {
		return calls;
	}

	public int getSeed() {
		return seed;
	}

	public int nextInt(int bound) {
		calls++;
		return rand.nextInt(bound);
	}

	public void set(int randomSeed, int randCalls) {
		this.seed = randomSeed;
		rand = new Random(randomSeed);
		this.calls = 0;
		for (int i = 0; i < randCalls; i++) {
			this.calls++;
			rand.nextInt(1);
		}
	}

	public void setSeed(int seed) {
		this.seed = seed;
		rand = new Random(seed);
		calls = 0;
	}

}
