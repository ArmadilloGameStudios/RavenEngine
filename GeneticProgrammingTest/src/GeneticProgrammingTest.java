import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cookedbird.genetic.Enviroment;

public class GeneticProgrammingTest {
	public static void main(String[] args) {
		GeneGenoratorTest geneGen = new GeneGenoratorTest();

		List<TestOrganism> pop = new ArrayList<TestOrganism>();

		for (int i = 0; i < 100; i++) {
			pop.add(new TestOrganism(geneGen.genGene(10)));
		}

		Enviroment<TestOrganism, Double> e = new Enviroment<TestOrganism, Double>();
		e.setFitnessFunction(new FitnessFunctionTest());
		e.setGeneGenorator(geneGen);
		e.setPopulation(pop);

		TestOrganism organism = null;

		try {
			organism = e.Run();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (organism != null) {
			List<Double> inputs = new ArrayList<Double>();
			Inputs.setInputs(inputs);

			while (true) {
				Scanner input = new Scanner(System.in);

				inputs.clear();
				inputs.add(input.nextDouble());
				inputs.add(input.nextDouble());
				System.out.println(String.format("[%1$s, %2$s]: %3$s",
						inputs.get(0), inputs.get(1), organism.getValue()));
			}
		}
	}
}
