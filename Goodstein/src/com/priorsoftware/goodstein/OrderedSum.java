package com.priorsoftware.goodstein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class OrderedSum {
	public abstract List<Summand> getSummands();

	public static OrderedSum ZERO = new OrderedSum() {
		@Override
		public List<Summand> getSummands() {
			return Collections.emptyList();
		}
	};

	public static OrderedSum getSingleton(final Summand primitive) {
		if (primitive.isZero())
			return ZERO;

		return new OrderedSum() {
			@Override
			public List<Summand> getSummands() {
				return Collections.singletonList(primitive);
			}
		};
	}

	private static OrderedSum fromList(final List<Summand> list) {
		return new OrderedSum() {
			@Override
			public List<Summand> getSummands() {
				return list;
			}
		};
	}

	public boolean isZero() {
		return getSummands().isEmpty();
	}

	public boolean isOne() {
		final List<Summand> elements = getSummands();

		final int sz = elements.size();
		if (sz != 1)
			return false;

		final Summand element = elements.get(0);
		if (element.getFactor() != 1)
			return false;

		return element.getExponent().isZero();
	}

	private static List<Summand> allButLast(final List<Summand> elements) {
		final int sz = elements.size();
		final List<Summand> ret = new ArrayList<Summand>();
		for (int i = 0; i < sz-1; ++i)
			ret.add(elements.get(i));
		return ret;
	}

	public OrderedSum decrement(final long base) {
		final List<Summand> elements = getSummands();

		final int sz = elements.size();
		if (sz == 0)
			throw new IllegalStateException();

		final Summand last = elements.get(sz - 1);
		final OrderedSum lastDec = last.decrement(base);
		if (lastDec.isZero()) {
			if (sz == 1)
				return ZERO;
			return fromList(allButLast(elements));
		}

		final List<Summand> ret = allButLast(elements);
		final List<Summand> newElements = lastDec.getSummands();
		ret.addAll(newElements);
		return fromList(ret);
	}

	public long getGap() {
		final List<Summand> elements = getSummands();

		final int sz = elements.size();
		if (sz == 0)
			return 0;

		final Summand last = elements.get(sz - 1);

		return last.getExponent().isZero()? last.getFactor() : 1;
	}

	public OrderedSum incrementBase(final long oldBase, final long newBase) {
		final List<Summand> elements = getSummands();

		final int sz = elements.size();
		if (sz == 0)
			return this;

		final List<Summand> ret = new ArrayList<Summand>(sz);
		for (final Summand element : elements) {
			ret.add(element.incrementBase(oldBase, newBase));
		}
		
		return fromList(ret);
	}

	public static OrderedSum add(final Summand ms, final OrderedSum r) {
		if (r.isZero()) {
			if (ms.isZero())
				return ZERO;
			return getSingleton(ms);
		}

		if (ms.isZero()) {
			return r;
		}

		final List<Summand> elements = r.getSummands();
		final List<Summand> ret = new ArrayList<Summand>();
		ret.add(ms);
		ret.addAll(elements);
		
		return fromList(ret);
	}

	public static final OrderedSum getBinaryExpansion(final int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		if (n <= 2)
			return getSingleton(Summand.getPrimitive(n));

		int k = n;
		int e = 0;
		final List<Summand> ret = new ArrayList<Summand>();
		while (true) {
			if (k == 0)
				break;
			if ((k & 1) == 1) {
				final Summand el = Summand.get(1, getBinaryExpansion(e));
				ret.add(el);
			}
			e++;
			k >>= 1;
		}
		Collections.reverse(ret);
		return fromList(ret);
	}

	public final String toString() {
		final List<Summand> elements = getSummands();
		if (elements.isEmpty())
			return "";

		final StringBuffer ret = new StringBuffer();
		boolean firstDone = true;
		for (final Summand element : elements) {
			if (firstDone) {
				firstDone = false;
			} else {
				ret.append(" + ");
			}
			ret.append(element);
		}
		return ret.toString();
	}

	public final double valueAt(final int base) {
		double ret = 0;
		final List<Summand> elements = getSummands();
		for (final Summand element : elements) {
			ret += element.valueAt(base);
		}
		return ret;
	}

	public OrderedSum dropLast() {
		final List<Summand> elements = getSummands();
		return fromList(allButLast(elements));
	}
}
