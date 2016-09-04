package com.cookedbird.genetic;

public abstract class GeneGenerator<T> {
	public abstract Gene<T> genGene(int depth);
}
