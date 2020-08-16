//**********************************************************************************
// Flower.java
// Author: Shirley Ramirez
// Allows organization for flower data
// *********************************************************************************

public class Flower {
	// Attributes for Flower class objects
	private float sepalLength;
	private float sepalWidth;
	private float petalLength;
	private float petalWidth;
	String label;

	// -----------------------------------------------------------------------------
	// Constructor for Flower objects
	// -----------------------------------------------------------------------------
	public Flower(float sl, float sw, float pl, float pw, String l) {
		sepalLength = sl;
		sepalWidth = sw;
		petalLength = pl;
		petalWidth = pw;
		label = l;
	}

	// -----------------------------------------------------------------------------
	// Returns distance between test Flower instance and training Flower instance
	// -----------------------------------------------------------------------------
	public static double distBetween(Flower test, Flower training) {
		double deltaSL = Math.pow((test.sepalLength - training.sepalLength), 2.0);
		double deltaSW = Math.pow((test.sepalWidth - training.sepalWidth), 2.0);
		double deltaPL = Math.pow((test.petalLength - training.petalLength), 2.0);
		double deltaPW = Math.pow((test.petalWidth - training.petalWidth), 2.0);

		double dist = Math.sqrt(deltaSL + deltaSW + deltaPL + deltaPW);

		return dist;
	}

	// -----------------------------------------------------------------------------
	// Returns the sepal length for an instance
	// -----------------------------------------------------------------------------
	public float getSL() {
		return this.sepalLength;
	}

	// -----------------------------------------------------------------------------
	// Returns the sepal width for an instance
	// -----------------------------------------------------------------------------
	public float getSW() {
		return this.sepalWidth;
	}

	// -----------------------------------------------------------------------------
	// Returns the petal length for an instance
	// -----------------------------------------------------------------------------
	public float getPL() {
		return this.petalLength;
	}

	// -----------------------------------------------------------------------------
	// Returns the petal width for an instance
	// -----------------------------------------------------------------------------
	public float getPW() {
		return this.petalWidth;
	}

	// -----------------------------------------------------------------------------
	// Returns the label for an instance
	// -----------------------------------------------------------------------------
	public String getLabel() {
		return this.label;
	}

	// -----------------------------------------------------------------------------
	// Overrides default toString method
	// -----------------------------------------------------------------------------
	@Override
	public String toString() {

		return " sl: " + this.sepalLength + " sw: " + this.sepalWidth + " pl: " + this.petalLength + " pw: "
				+ this.petalWidth + " label: " + label;
	}

}