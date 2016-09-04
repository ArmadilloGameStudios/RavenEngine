import com.cookedbird.genetic.Enviroment;
import com.cookedbird.genetic.Gene;
import com.cookedbird.genetic.GeneGenerator;

public class GeneGenoratorTest extends GeneGenerator<Double> {
	public Gene<Double> genGene(int depth) {
		int type = Enviroment.getRandom().nextInt(3);
		if (depth == 0) {
			type = 2;
		}

		switch (type) {
		case 0:
			return new GeneInput(this.genGene(depth - 1));
		case 1:
			int op = Enviroment.getRandom().nextInt(4);

			return new GeneBiOperator(op, this.genGene(depth - 1),
					this.genGene(depth - 1));
		case 2:
		default:
			double value = Enviroment.getRandom().nextDouble();
			double multiply = Enviroment.getRandom().nextDouble();

			if (multiply < .1) {

			} else if (multiply < .6) {
				value *= 10;
			} else if (multiply < .85) {
				value *= 100;
			} else if (multiply < .95) {
				value *= 1000;
			} else if (multiply < .98) {
				value *= 10000;
			} else {
				value *= 100000;
			}

			return new GeneConstant(value);
		}
	}
}
