package com.priorsoftware.goodstein;

public abstract class Summand {
	public abstract long getFactor();

	public abstract OrderedSum getExponent();

	public abstract OrderedSum decrement(long base);

	public boolean isZero() {
		return getFactor() == 0;
	};

	public final String toString() {
		final long factor = getFactor();
		if (factor == 0)
			return "";

		final StringBuffer ret = new StringBuffer();

		final OrderedSum exponent = getExponent();
		
		if (factor > 1 || exponent.isZero())
			ret.append(getFactor());

		if (!exponent.isZero()) {
			ret.append("b");
			
			if (!exponent.isOne()) {
				ret.append("^");

				final boolean compound = exponent.getSummands().size() > 1;
				
				if (compound)
					ret.append("(");
	
				ret.append(exponent);
	
				if (compound)
					ret.append(")");
			}
		}

		return ret.toString();
	}

	public Summand incrementBase(final long oldBase, final long newBase) {
		long factor = getFactor();
		final OrderedSum exponent = getExponent();
		if (exponent.isZero()) {
			if (factor == oldBase)
				return getPrimitive(newBase);
			return this;
		}

		return get(factor, exponent.incrementBase(oldBase, newBase));
	}

	public static Summand getPrimitive(final long k) {
		return new Summand() {

			@Override
			public OrderedSum getExponent() {
				return OrderedSum.ZERO;
			}

			@Override
			public long getFactor() {
				return k;
			}

			@Override
			public OrderedSum decrement(final long base) {
				if (k == 0)
					throw new IllegalStateException();
				if (k == 1)
					return OrderedSum.ZERO;
				return OrderedSum.getSingleton(getPrimitive(k - 1));
			}
		};
	}

	public static Summand get(final long factor, final OrderedSum exponent) {
		if (factor == 0 || exponent.isZero())
			return getPrimitive(factor);

		return new Summand() {
			@Override
			public OrderedSum getExponent() {
				return exponent;
			}

			@Override
			public long getFactor() {
				return factor;
			}

			@Override
			public OrderedSum decrement(final long base) {
				final Summand x = get(base, exponent.decrement(base));
				final OrderedSum r = x.decrement(base);
				final Summand ms = get(factor - 1, exponent);
				return OrderedSum.add(ms, r);
			}
		};
	}

	public double valueAt(final int base) {
		if (isZero())
			return 0;
		final double exponent = getExponent().valueAt(base);
		return getFactor()*Math.pow(base, exponent);
	}
}
