import com.cookedbird.genetic.Gene;


public class GeneInput extends Gene<Double> {
	public GeneInput(Gene<Double> gene) {
		getGeneTree().add(gene);
	}
	
	public Gene<Double> getGene() {	
		// System.out.println("Get " + getGeneTree());
		
		return this.getGeneTree().get(0);
	}
	
	@Override
	public Double getValue() {
		int index = Math.abs(getGene().getValue().intValue()) % Inputs.getInputs().size();
		
		return Inputs.getInputs().get(index);
	}

	@Override
	public Gene<Double> copy() {
		return new GeneInput(getGene().copy());
	}

	@Override
	public String toString() {
		return String.format("([I] %% %1$s)", getGene().toString());
	}

	@Override
	public GeneInput autoResolve() {
	    Gene<Double> gene = getGene().autoResolve();
		
	    getGeneTree().clear();
		getGeneTree().add(gene);
	    
		return this;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}
