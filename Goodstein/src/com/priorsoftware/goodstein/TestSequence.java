package com.priorsoftware.goodstein;

public class TestSequence {

	public static void main(final String[] args) {
		final int arg = Integer.valueOf(args[0]);
		final OrderedSum seq = OrderedSum.getBinaryExpansion(arg);
		System.out.println(seq);
		final double orig = seq.valueAt(2);
		System.out.println(orig);
	}
}
