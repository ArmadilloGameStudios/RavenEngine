package com.cookedbird.genetic;

import java.util.ArrayList;
import java.util.List;

public abstract class Gene<T> {
	private List<Gene<T>> geneTree = new ArrayList<Gene<T>>();

	public abstract T getValue();
	public abstract Gene<T> copy();
	public abstract Gene<T> autoResolve();
	public abstract boolean isConstant();

	public List<Gene<T>> getGeneList() {
		List<Gene<T>> genetics = new ArrayList<Gene<T>>();

		for (int i = 0; i < geneTree.size(); i++) {
			genetics.add(geneTree.get(i));
			genetics.addAll(geneTree.get(i).getGeneList());
		}

		return genetics;
	}
	
	public int getGeneCount() {
		int count = this.geneTree.size();
		
		for (int i = 0; i < this.geneTree.size(); i++) {
			count += this.geneTree.get(i).getGeneCount();
		}
		
		return count;
	}
	
	public boolean containsGene(Gene<T> gene) {
		return geneTree.contains(gene);
	}
	
	public boolean replaceGene(Gene<T> oldGene, Gene<T> newGene) {
		for (int i = 0; i < geneTree.size(); i++) {
			if (geneTree.get(i) == oldGene) {
				geneTree.set(i, newGene);
				return true;
			}
		}
		
		for (int i = 0; i < geneTree.size(); i++) {
			if (geneTree.get(i).replaceGene(oldGene, newGene)) {
				return true;
			}
		}
		
		return false;
	}

	public List<Gene<T>> getGeneTree() {
		return geneTree;
	}
}
