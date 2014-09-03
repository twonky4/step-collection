package de.alexanderkose.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

public class TestUtils {
	public static void assertNotEmpty(Collection<String> nextStep) {
		assertNotNull(nextStep);
		assertFalse(nextStep.isEmpty());
	}

	public static void assertEmpty(Collection<String> nextStep) {
		assertNotNull(nextStep);
		assertTrue(nextStep.isEmpty());
	}

	public static <T extends Comparable<T>> StepCollection<T> newGreatestList(
			int size) {
		return new StepCollection<>(size, false);
	}

	public static <T extends Comparable<T>> StepCollection<T> newSmallestList(
			int size) {
		return new StepCollection<>(size);
	}
}
