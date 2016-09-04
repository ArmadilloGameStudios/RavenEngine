package com.cookedbird.genetic;

public abstract class FitnessFunction<O extends Organism<O, ?>> {

	public abstract OrganismFitness<O> Test(O organism);
}
