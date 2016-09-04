package com.cookedbird.genetic;


public class OrganismFitness<O extends Organism<O, ?>> implements
		Comparable<OrganismFitness<O>> {
	private double rank;
	private O organism;

	public OrganismFitness(double rank, O organism) {
		this.rank = rank;
		this.organism = organism;
	}

	public double getFitness() {
		return rank;
	}

	public O getOrganism() {
		return organism;
	}

	@Override
	public int compareTo(OrganismFitness<O> fRank) {
		if (this.getFitness() > fRank.getFitness()) {
			return -1;
		} else if (this.getFitness() < fRank.getFitness()) {
			return 1;
		}

		return 0;
	}
}
