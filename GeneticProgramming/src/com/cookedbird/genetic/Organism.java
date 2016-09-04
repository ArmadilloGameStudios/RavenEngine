package com.cookedbird.genetic;

import java.util.ArrayList;
import java.util.List;

public abstract class Organism<O extends Organism<O, T>, T> {
	private Gene<T> genetics;

	public abstract O copy(Gene<T> genetics);
	public abstract O copy();
	
	public Organism(Gene<T> genetics) {
		this.genetics = genetics;
	}

	public boolean replaceGene(Gene<T> oldGene, Gene<T> newGene) {
		if (genetics == oldGene) {
			genetics = newGene;
			return true;
		} else {
			return genetics.replaceGene(oldGene, newGene);
		}
	}

	public O Mate(O partner) {
		O child = partner.copy();

		List<Gene<T>> list = this.getGeneList();
		List<Gene<T>> childList = child.getGeneList();

		int geneIndex = Enviroment.getRandom().nextInt(list.size());
		int childIndex = Enviroment.getRandom().nextInt(childList.size());

		child.replaceGene(childList.get(childIndex), list.get(geneIndex).copy());

		return child;
	}

	public O Mutate(GeneGenerator<T> gg) {
		return Mutate(gg, 0);
	}

	public O Mutate(GeneGenerator<T> gg, int depth) {
		O child = this.copy(genetics);

		List<Gene<T>> list = child.getGeneList();
		int geneIndex = Enviroment.getRandom().nextInt(list.size());

		child.replaceGene(list.get(geneIndex), gg.genGene(depth));

		return child;
	}

	public T getValue() {
		return genetics.getValue();
	}

	public int getGeneCount() {
		return 1 + genetics.getGeneCount();
	}

	public Gene<T> getGenetics() {
		return this.genetics;
	}

	public List<Gene<T>> getGeneList() {
		List<Gene<T>> list = new ArrayList<Gene<T>>();

		list.add(this.genetics);
		list.addAll(this.genetics.getGeneList());

		return list;
	}

	public void setGenetics(Gene<T> gene) {
		genetics = gene;
	}

	public void limitGeneCount(int count, GeneGenerator<T> geneGen) {
		// attempt methods until the count is less than the limit
		while (getGeneCount() > count) {
			int method = Enviroment.getRandom().nextInt(2);

			List<Gene<T>> geneList = this.getGeneList();
			
			Gene<T> geneToAlter = geneList.get(Enviroment.getRandom().nextInt(geneList.size()));
			
			switch (method) {
			case 0:
				geneToAlter.autoResolve();
			case 1:
				replaceGene(geneToAlter, geneGen.genGene(0));
			}
		}
	}
}
