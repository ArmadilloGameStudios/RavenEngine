import com.cookedbird.genetic.Gene;
import com.cookedbird.genetic.Organism;


public class TestOrganism extends Organism<TestOrganism, Double> {
	public TestOrganism(Gene<Double> genetics) {
		super(genetics);
	}

	@Override
	public TestOrganism copy(Gene<Double> genetics) {
		return new TestOrganism(genetics);
	}
	
	@Override
	public TestOrganism copy() {
		return new TestOrganism(this.getGenetics().copy());
	}
}
