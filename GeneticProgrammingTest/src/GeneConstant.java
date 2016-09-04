import com.cookedbird.genetic.Gene;


public class GeneConstant extends Gene<Double> {
	private double value;
	
	public GeneConstant(double value) {
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return new Double(value);
	}

	@Override
	public Gene<Double> copy() {
		return new GeneConstant(value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public GeneConstant autoResolve() {
		return this;
	}

	@Override
	public boolean isConstant() {
		return true;
	}
}
