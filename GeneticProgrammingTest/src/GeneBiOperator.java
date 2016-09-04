import com.cookedbird.genetic.Gene;

public class GeneBiOperator extends Gene<Double> {
	private int op;

	public GeneBiOperator(int op, Gene<Double> a, Gene<Double> b) {
		this.getGeneTree().add(a);
		this.getGeneTree().add(b);

		this.op = op;
	}

	public Gene<Double> getA() {
		return this.getGeneTree().get(0);
	}

	public Gene<Double> getB() {
		return this.getGeneTree().get(1);
	}

	@Override
	public Double getValue() {
		switch (op) {
		case 0: // -
			return getA().getValue() - getB().getValue();
		case 1: // *
			return getA().getValue() * getB().getValue();
		case 2: // /
			if (getB().getValue() == 0)
				return new Double(1);

			return getA().getValue() / getB().getValue();
		case 3: // +
		default:
			return getA().getValue() + getB().getValue();
		}
	}

	@Override
	public Gene<Double> copy() {
		return new GeneBiOperator(op, getA().copy(),
				getB().copy());
	}

	@Override
	public String toString() {
		switch (op) {
		case 0: // -
			return String.format("(%1$s - %2$s)", getA().toString(), getB().toString());
		case 1: // *
			return String.format("(%1$s * %2$s)", getA().toString(), getB().toString());
		case 2: // /
			return String.format("(%1$s / %2$s)", getA().toString(), getB().toString());
		case 3: // +
		default:
			return String.format("(%1$s + %2$s)", getA().toString(), getB().toString());
		}
	}

	@Override
	public Gene<Double> autoResolve() {
		if (getA().isConstant() && getB().isConstant()) {
			switch (op) {
			case 0: // -
				return new GeneConstant(getA().getValue() - getB().getValue());
			case 1: // *
				return new GeneConstant(getA().getValue() * getB().getValue());
			case 2: // /
				if (getB().getValue() == 0) {
					return new GeneConstant(1.0);
				}
				return new GeneConstant(getA().getValue() / getB().getValue());
			case 3: // +
			default:
				return new GeneConstant(getA().getValue() + getB().getValue());
			}
		}
		
		return this;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}
