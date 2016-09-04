package com.cookedbird.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Enviroment<O extends Organism<O, T>, T> {
	// static
	private static Random r = new Random();

	// params
	private int generations = 1000;
	private int minPopCount = 50;
	private int maxPopCount = 500;
	private int depth = 5;
	private int maxGeneCount = 100;
	private List<O> population;
	private FitnessFunction<O> fitnessFunction;
	private GeneGenerator<T> geneGen;

	// data
	private OrganismFitness<O> best = null;
	private int genFound = 0;
	private int completion = 0;

	public void setPopulation(List<O> population) {
		this.population = population;
	}

	public void setFitnessFunction(FitnessFunction<O> fitFunc) {
		this.fitnessFunction = fitFunc;
	}

	public void setGeneGenorator(GeneGenerator<T> geneGen) {
		this.geneGen = geneGen;
	}

	public O Run() throws Exception {
		// TODO check params filled
		if (fitnessFunction == null)
			throw new Exception();
		if (geneGen == null)
			throw new Exception();
		if (population == null || population.size() == 0)
			throw new Exception();

		List<OrganismFitness<O>> ranks = new ArrayList<OrganismFitness<O>>();

		for (int g = 0; g < generations; g++) {
			if ((g * 100) / generations != completion) {
				completion = (g * 100) / generations;

				System.out.println(completion + "%");
				printBest();
			}

			int size = population.size();

			// test pop
			for (int i = 0; i < size; i++) {
				ranks.add(fitnessFunction.Test(population.get(i)));
			}

			Collections.sort(ranks);

			OrganismFitness<O> top = ranks.get(0);

			// save data
			if (best == null
					|| best.getFitness() < top.getFitness()
					|| (best.getFitness() == top.getFitness() && top
							.getOrganism().getGeneCount() < best.getOrganism()
							.getGeneCount())) {
				best = new OrganismFitness<O>(top.getFitness(), top
						.getOrganism().copy());
				genFound = g;
			}

			// cull the herd
			population.clear();

			for (int i = 0; i < size / 8; i++) {
				population.add(ranks.get(0).getOrganism());
				ranks.remove(0);
			}

			// maintain diversity
			for (int i = 0; i < size / 8; i++) {
				int index = r.nextInt(ranks.size());

				population.add(ranks.get(index).getOrganism());
				ranks.remove(index);
			}

			size = population.size();
			ranks.clear();

			// next generation
			List<O> nextPopulation = new ArrayList<O>();

			for (int i = 0; i < size; i++) {
				double method = r.nextDouble();

				if (method < .08) { // Mutate
					nextPopulation
							.add(population.get(i).Mutate(geneGen, depth));
				} else if (method < .90) { // Mate
					int count = r.nextInt(10) + 1;
					for (int j = 0; j < count; j++)
						nextPopulation.add(population.get(i).Mate(
								population.get(r.nextInt(size))));
				} else if (method < .98) { // Maintain
					nextPopulation.add(population.get(i));
				} // Otherwise abandon the organism
			}

			// check pop size
			while (nextPopulation.size() > this.maxPopCount) {
				nextPopulation.remove(r.nextInt(nextPopulation.size()));
			}

			// check organism size
			for (int i = 0; i < nextPopulation.size(); i++) {
				nextPopulation.get(i).limitGeneCount(maxGeneCount, geneGen);
			}

			population = nextPopulation;
		}

		best.getOrganism().getGenetics().autoResolve();
		System.out.println("done!");
		printBest();
		
		return best.getOrganism();
	}

	public void printBest() {
		System.out.println("Fitness: " + best.getFitness());
	}

	public static Random getRandom() {
		return r;
	}
}
