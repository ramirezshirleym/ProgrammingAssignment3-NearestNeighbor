//**********************************************************************************
// DistanceLabel.java
// Author: Shirley Ramirez
// Allows association between calculated distance and instance label
// *********************************************************************************

public class DistanceLabel implements Comparable<DistanceLabel> {
	private double distance;
	private String label;

	public DistanceLabel(double dist, String l, int origIndex) {
		this.distance = dist;
		this.label = l;
	}

	// -----------------------------------------------------------------------------
	// Returns the distance between instances
	// -----------------------------------------------------------------------------
	public double getDistance() {
		return this.distance;
	}

	// -----------------------------------------------------------------------------
	// Returns the label for an instance
	// -----------------------------------------------------------------------------
	public String getLabel() {
		return this.label;
	}

	// -----------------------------------------------------------------------------
	// Overrides default compareTo method
	// -----------------------------------------------------------------------------
	@Override
	public int compareTo(DistanceLabel otherResult) {
		return Double.compare(this.distance, otherResult.getDistance());

	}

	// -----------------------------------------------------------------------------
	// Overrides default toString method
	// -----------------------------------------------------------------------------
	@Override
	public String toString() {
		return "distance: " + this.distance + " label: " + this.label + " original index: ";
	}
}
