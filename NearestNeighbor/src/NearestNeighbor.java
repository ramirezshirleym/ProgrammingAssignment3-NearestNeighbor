/* Programming Fundamentals
 * NAME: Shirley Ramirez
 * PROGRAMMING FUNDAMENTALS 3
 */

import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class NearestNeighbor {

	public static void main(String[] args) throws IOException {
		System.out.println("Programming Fundamentals");
		System.out.println("NAME: Shirley Ramirez");
		System.out.println("PROGRAMMING ASSIGNMENT 3");
		System.out.println();
		int k = 5;
		Scanner userInputScan = new Scanner(System.in);
		String trainingFile;
		String testingFile;

		// Read in file names from user
		System.out.print("Enter the name of the training file: ");
		trainingFile = userInputScan.nextLine();

		System.out.print("Enter the name of the testing file: ");
		testingFile = userInputScan.nextLine();
		userInputScan.close();

		System.out.println("EX#: TRUE LABEL, PREDICTED LABEL");

		// Parse files
		Flower[] trainingArray = parseFileData(trainingFile);
		Flower[] testingArray = parseFileData(testingFile);

		// Normalize data
		Flower[] normTraining = normalizeValues(trainingArray);
		Flower[] normTesting = normalizeValues(testingArray);

		// Predict label for each test flower and calculate overall prediction score
		int score = kNearestNeighbor(k, normTraining, normTesting);

		double accuracy = score / new Double(normTesting.length);

		System.out.println("ACCURACY: " + accuracy);

	}

	// --------------------------------------------------------------------------------------------------
	// CREATE FLOWER ARRAY FROM INPUT DATA
	// --------------------------------------------------------------------------------------------------
	public static Flower[] parseFileData(String fileName) throws FileNotFoundException {
		// Instantiate data file and scanner object to read it
		Scanner fileScan = new Scanner(new File(fileName));
		String csvLine;

		String[] stringValues = new String[5];
		Flower[] dataArray = new Flower[75];
		int row = 0;

		// Read and process each line of the data file
		while (fileScan.hasNext()) {

			// Read in each line of csv file
			csvLine = fileScan.nextLine();

			// Split string around comma delimiter
			stringValues = csvLine.split(",");
			float sepalLength = Float.parseFloat(stringValues[0]);
			float sepalWidth = Float.parseFloat(stringValues[1]);
			float petalLength = Float.parseFloat(stringValues[2]);
			float petalWidth = Float.parseFloat(stringValues[3]);
			String label = stringValues[4];

			dataArray[row] = new Flower(sepalLength, sepalWidth, petalLength, petalWidth, label);

			row++;
		}

		fileScan.close();

		return dataArray;
	}

	// --------------------------------------------------------------------------------------------------
	// NORMALIZE INPUT DATA
	// --------------------------------------------------------------------------------------------------
	public static Flower[] normalizeValues(Flower[] originalValues) {
		int row;
		// Create array for each attribute
		int arrayLength = originalValues.length;
		Flower[] normDataset = new Flower[arrayLength];
		float[] arraySL = new float[arrayLength];
		float[] arraySW = new float[arrayLength];
		float[] arrayPL = new float[arrayLength];
		float[] arrayPW = new float[arrayLength];

		// Extract each flower's sepal length in arraySL
		for (int index = 0; index < arrayLength; index++) {
			arraySL[index] = originalValues[index].getSL();
		}
		Arrays.sort(arraySL);
		float minSL = arraySL[0];
		float maxSL = arraySL[arrayLength - 1];
		float denomSL = maxSL - minSL;

		// Extract each flower's sepal width in arraySW
		for (int index = 0; index < arrayLength; index++) {
			arraySW[index] = originalValues[index].getSW();
		}
		Arrays.sort(arraySW);
		float minSW = arraySW[0];
		float maxSW = arraySW[arrayLength - 1];
		float denomSW = maxSW - minSW;

		// Extract each flower's petal length in arrayPL
		for (int index = 0; index < arrayLength; index++) {
			arrayPL[index] = originalValues[index].getPL();
		}
		Arrays.sort(arrayPL);
		float minPL = arrayPL[0];
		float maxPL = arrayPL[arrayLength - 1];
		float denomPL = maxPL - minPL;

		// Extract each flower's petal width in arrayPW
		for (int index = 0; index < arrayLength; index++) {
			arrayPW[index] = originalValues[index].getPW();
		}
		Arrays.sort(arrayPW);
		float minPW = arrayPW[0];
		float maxPW = arrayPW[arrayLength - 1];
		float denomPW = maxPW - minPW;

		// Replace original values with normalized values
		for (row = 0; row < arrayLength; row++) {
			float normSL = (originalValues[row].getSL() - minSL) / denomSL;
			float normSW = (originalValues[row].getSW() - minSW) / denomSW;
			float normPL = (originalValues[row].getPL() - minPL) / denomPL;
			float normPW = (originalValues[row].getPW() - minPW) / denomPW;

			normDataset[row] = new Flower(normSL, normSW, normPL, normPW, originalValues[row].getLabel());
		}

		return normDataset;
	}

	// --------------------------------------------------------------------------------------------------
	// PERFORM K-NEAREST NEIGHBOR ALGORITHM
	// --------------------------------------------------------------------------------------------------
	public static int kNearestNeighbor(int k, Flower[] normTraining, Flower[] normTesting) {
		int score = 0;

		for (int i = 0; i < normTesting.length; i++) {
			int numTraining = normTraining.length;
			Flower currTestFlower = normTesting[i];
			DistanceLabel[] distanceLabels = new DistanceLabel[numTraining];

			// Calculate the distance between current test flower and each training flower
			for (int j = 0; j < numTraining; j++) {
				Flower currTrainFlower = normTraining[j];
				double distance = Flower.distBetween(currTestFlower, currTrainFlower);
				distanceLabels[j] = new DistanceLabel(distance, currTrainFlower.getLabel());
			}

			// Sort array by distance (see DistanceLabel.java line 34)
			Arrays.sort(distanceLabels);

			// Get the k closest training labels
			DistanceLabel[] topK = Arrays.copyOfRange(distanceLabels, 0, k);

			String prediction = predictLabel(topK);

			System.out.println((i + 1) + " " + currTestFlower.getLabel() + " " + prediction);

			if (currTestFlower.getLabel().equals(prediction)) {
				score++;
			}

		}

		return score;
	}

	// --------------------------------------------------------------------------------------------------
	// FIND MODE LABEL COUNT FROM TOP K DISTANCE LABELS FOR PREDICTION LABEL
	// --------------------------------------------------------------------------------------------------
	public static String predictLabel(DistanceLabel[] distanceLabels) {
		int setosa = 0;
		int versicolor = 0;
		int virginica = 0;

		// Get the count of each label from top k
		for (int index = 0; index < distanceLabels.length; index++) {
			String currentLabel = distanceLabels[index].getLabel();

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

		return prediction;
	}
}