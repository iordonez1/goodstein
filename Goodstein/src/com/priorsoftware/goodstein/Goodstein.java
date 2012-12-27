package com.priorsoftware.goodstein;

public final class Goodstein {
	private OrderedSum seq;
	private long base = 2;

	public Goodstein(final int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		seq = OrderedSum.getBinaryExpansion(n);
	}

	public void next() {
		if (seq.isZero())
			return;

		final long gap = seq.getGap();

		seq = seq.incrementBase(base, base + gap);
		base += gap;
		
		seq = gap == 1? seq.decrement(base) : seq.dropLast();
	}

	public final String toString() {
		final StringBuffer ret = new StringBuffer("b = ");
		ret.append(base);
		ret.append(" : ");
		ret.append(seq);
//		ret.append(" = ");
//		ret.append(seq.valueAt(base));
		return ret.toString();
	}
}
