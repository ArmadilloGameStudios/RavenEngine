import java.util.ArrayList;
import java.util.List;

import com.cookedbird.genetic.Enviroment;
import com.cookedbird.genetic.FitnessFunction;
import com.cookedbird.genetic.OrganismFitness;

public class FitnessFunctionTest extends FitnessFunction<TestOrganism> {

	@Override
	public OrganismFitness<TestOrganism> Test(TestOrganism organism) {
		double fitness = 0;

		List<Double> inputs = new ArrayList<Double>();
		Inputs.setInputs(inputs);

		for (int i = 0; i < 50; i++) {
		    double x = Enviroment.getRandom().nextDouble();
		    double y = Enviroment.getRandom().nextDouble();

			inputs.clear();
			inputs.add(x);
			inputs.add(y);
			
			if (x + y < 1.0)
				fitness += Math.abs(100.0 - organism.getValue());
			else
				fitness += Math.abs(0.0 - organism.getValue());
		}
		
		fitness *= -1;
		
		return new OrganismFitness<TestOrganism>(fitness, organism);
	}
}
