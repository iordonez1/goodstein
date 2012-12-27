package com.priorsoftware.goodstein;

public class TestGoodstein {

	public static void main(final String[] args) {
		final int n = Integer.valueOf(args[0]);
		final Goodstein g = new Goodstein(n);
		
		for (int i = 0; i < 100; i++) {
			System.out.println(g);
			g.next();
		}
	}

}
