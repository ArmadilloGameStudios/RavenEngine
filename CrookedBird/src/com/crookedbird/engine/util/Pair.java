package com.crookedbird.engine.util;

public class Pair<T extends Number> {
	private T x, y;

	public Pair(T a, T b) {
		this.x = a;
		this.y = b;
	}

	public T getX() {
		return x;
	}

	public T getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		try {
			if (this.getClass() == o.getClass()) {
				Pair<T> p = (Pair<T>) o;
				return x == p.x && y == p.y; // || a == p.b && b == p.a;
			}
			return super.equals(o);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return x.hashCode() + y.hashCode();
	}
}
