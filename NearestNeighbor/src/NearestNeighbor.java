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

		System.out.println("EX#: TRUE LABEL, PREDICTED LABEL");

		Flower[] trainingArray = parseFileData(trainingFile);
		Flower[] testingArray = parseFileData(testingFile);

		// Normalize training set
		Flower[] normTraining = normalizeValues(trainingArray);
		Flower[] normTesting = normalizeValues(testingArray);

		// Predict label for each testing row
		int score = 0;

		for (int i = 0; i < normTesting.length; i++) {
			int numTraining = normTraining.length;
			Flower currTestFlower = normTesting[i];
			DistanceLabel[] distanceLabels = new DistanceLabel[numTraining];

			// Calculate the distance between current test flower and each training flower
			for (int j = 0; j < numTraining; j++) {
				Flower currTrainFlower = normTraining[j];
				double distance = Flower.distBetween(currTestFlower, currTrainFlower);
				distanceLabels[j] = new DistanceLabel(distance, currTrainFlower.getLabel(), j);
			}

			// Sort array by distance (see DistanceLabel.java line 34)
			Arrays.sort(distanceLabels);

			// Get the k closest training labels
			DistanceLabel[] topK = Arrays.copyOfRange(distanceLabels, 0, k);

			String prediction = predict(topK);

			System.out.println((i + 1) + " " + currTestFlower.getLabel() + " " + prediction);

			if (currTestFlower.getLabel().equals(prediction)) {
				score++;
			}

		}

		double accuracy = score / 75.0;

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

		// Read and process each line of the training file
		while (fileScan.hasNext()) {

			// Read in line of csv file
			csvLine = fileScan.nextLine();

			// Split string around comma delimiter & enter each value into String array
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
		// Normalize data
		int arrayLength = originalValues.length;
		Flower[] normDataset = new Flower[arrayLength];
		float[] arraySL = new float[arrayLength];
		float[] arraySW = new float[arrayLength];
		float[] arrayPL = new float[arrayLength];
		float[] arrayPW = new float[arrayLength];

		// Normalize Sepal Length
		for (int index = 0; index < arrayLength; index++) {
			arraySL[index] = originalValues[index].getSL();
		}
		Arrays.sort(arraySL);
		float minSL = arraySL[0];
		float maxSL = arraySL[arrayLength - 1];
		float denomSL = maxSL - minSL;

		// Normalize Sepal Width
		for (int index = 0; index < arrayLength; index++) {
			arraySW[index] = originalValues[index].getSW();
		}
		Arrays.sort(arraySW);
		float minSW = arraySW[0];
		float maxSW = arraySW[arrayLength - 1];
		float denomSW = maxSW - minSW;

		// Normalize Petal Length
		for (int index = 0; index < arrayLength; index++) {
			arrayPL[index] = originalValues[index].getPL();
		}
		Arrays.sort(arrayPL);
		float minPL = arrayPL[0];
		float maxPL = arrayPL[arrayLength - 1];
		float denomPL = maxPL - minPL;

		// Normalize Petal Width
		for (int index = 0; index < arrayLength; index++) {
			arrayPW[index] = originalValues[index].getPW();
		}
		Arrays.sort(arrayPW);
		float minPW = arrayPW[0];
		float maxPW = arrayPW[arrayLength - 1];
		float denomPW = maxPW - minPW;

		// Replace trainingArray values with normalized values

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
	// FIND MODE LABEL COUNT FROM TOP K DISTANCE LABELS FOR PREDICTION LABEL
	// --------------------------------------------------------------------------------------------------
	public static String predict(DistanceLabel[] distanceLabels) {
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
