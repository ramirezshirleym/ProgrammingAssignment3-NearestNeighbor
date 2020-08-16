/* Programming Fundamentals
 * NAME: Shirley Ramirez
 * PROGRAMMING FUNDAMENTALS 3
 */

import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;


public class NearestNeighbor {

	public static void main(String[] args) throws IOException {
		System.out.println("Programming Fundamentals");
		System.out.println("NAME: Shirley Ramirez");
		System.out.println("PROGRAMMING ASSIGNMENT 3");
		System.out.println();
		int k = 5;
		Scanner userInputScan = new Scanner(System.in); // reader user input
		String trainingFile;
		String testingFile;

		// Read in file names from user
//		System.out.print("Enter the name of the training file: ");
//		trainingFile = userInputScan.nextLine();
//
//		System.out.print("Enter the name of the testing file: ");
//		testingFile = userInputScan.nextLine();
		trainingFile = "iris-training-data.csv";
		testingFile = "iris-testing-data.csv";
		userInputScan.close();

		// Instantiate training data file and scanner object to read it
		Scanner trainingScan = new Scanner(new File(trainingFile));

		// Instantiate testing data file and scanner object to read it
		Scanner testingScan = new Scanner(new File(testingFile));

		String csvLine;
		String[] stringValues = new String[5];
		Flower[] trainingArray = new Flower[75];
		int row = 0;
		System.out.println();
		System.out.println("EX#: TRUE LABEL, PREDICTED LABEL");
		// Read and process each line of the training file
		while (trainingScan.hasNext()) {

			// Read in line of csv file
			csvLine = trainingScan.nextLine();

			// Split string around comma delimiter & enter each value into String array
			stringValues = csvLine.split(",");
			float sepalLength = Float.parseFloat(stringValues[0]);
			float sepalWidth = Float.parseFloat(stringValues[1]);
			float petalLength = Float.parseFloat(stringValues[2]);
			float petalWidth = Float.parseFloat(stringValues[3]);
			String label = stringValues[4];

			trainingArray[row] = new Flower(sepalLength, sepalWidth, petalLength, petalWidth, label);

			row++;
		}

		trainingScan.close();

		// Predict label for each testing row
		row = 0;
		int score = 0;
		

		while (testingScan.hasNext()) {
			DistanceLabel[] distanceLabels = new DistanceLabel[75];
			// Read in line of csv file
			csvLine = testingScan.nextLine();

			// Split string around comma delimiter & enter each value into String array
			stringValues = csvLine.split(",");
			float sepalLength = Float.parseFloat(stringValues[0]);
			float sepalWidth = Float.parseFloat(stringValues[1]);
			float petalLength = Float.parseFloat(stringValues[2]);
			float petalWidth = Float.parseFloat(stringValues[3]);
			String testLabel = stringValues[4];

			// Build Flower
			Flower testFlower = new Flower(sepalLength, sepalWidth, petalLength, petalWidth, testLabel);

			// Calculate the distance between each test flower and each training flower
			for (int index = 0; index < 75; index++) {
				double distance = Flower.distBetween(testFlower, trainingArray[index]);
				distanceLabels[index] = new DistanceLabel(distance, trainingArray[index].getLabel(), index);
					
			}

			// Sort array by distance (see DistanceLabel.java line 34)
			Arrays.sort(distanceLabels);
			
			// Get the five closest training labels
			DistanceLabel[] topK = Arrays.copyOfRange(distanceLabels, 0, k);
			int setosa = 0;
			int versicolor = 0;
			int virginica = 0;

			// Get the count of each label from top k
			for (int index = 0; index < k; index++) {
				String currentLabel = topK[index].getLabel();

				switch (currentLabel) {
				case "Iris-setosa":
					setosa++;
					break;
				case "Iris-versicolor":
					versicolor++;
					break;
				case "Iris-virginica":
					virginica++;
					break;
				default:
					System.out.println("Unknown label: " + currentLabel);
				}
			}

			// Get mode for prediction
			String prediction;
			if (setosa > versicolor && setosa > virginica) {
				prediction = "Iris-setosa";
			} else if (versicolor > setosa && versicolor > virginica) {
				prediction = "Iris-versicolor";
			} else if (virginica > setosa && virginica > versicolor) {
				prediction = "Iris-virginica";
			} else {
				prediction = "There is no mode";
			}

			System.out.println((row+1) + " " + testLabel + " " + prediction);
			row++;

			if (testLabel.equals(prediction)) {
				score++;
			}

		}

		double accuracy = score / 75.0;

		System.out.println("ACCURACY: " + accuracy);

	}

}
