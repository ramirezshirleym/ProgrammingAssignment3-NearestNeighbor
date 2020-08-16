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
		int k = 3;
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
	// STANDARDIZE INPUT DATA
	// --------------------------------------------------------------------------------------------------
	public static Flower[] normalizeValues(Flower[] originalValues) {
		int row;
		// Normalize data
		int arrayLength = originalValues.length;
		Flower[] normDataset = new Flower[arrayLength];
		double[] arraySL = new double[arrayLength];
		double[] arraySW = new double[arrayLength];
		double[] arrayPL = new double[arrayLength];
		double[] arrayPW = new double[arrayLength];

		// Normalize Sepal Length
		for (int index = 0; index < arrayLength; index++) {
			arraySL[index] = originalValues[index].getSL();
		}

		double meanSL = mean(arraySL);
		double stDevSL = stDev(arraySL, meanSL);

		// Normalize Sepal Width
		for (int index = 0; index < arrayLength; index++) {
			arraySW[index] = originalValues[index].getSW();
		}

		double meanSW = mean(arraySW);
		double stDevSW = stDev(arraySL, meanSW);

		// Normalize Petal Length
		for (int index = 0; index < arrayLength; index++) {
			arrayPL[index] = originalValues[index].getPL();
		}
		double meanPL = mean(arrayPL);
		double stDevPL = stDev(arrayPL, meanPL);

		// Normalize Petal Width
		for (int index = 0; index < arrayLength; index++) {
			arrayPW[index] = originalValues[index].getPW();
		}
		double meanPW = mean(arrayPW);
		double stDevPW = stDev(arraySL, meanPW);

		// Replace trainingArray values with normalized values

		for (row = 0; row < arrayLength; row++) {
			double normSL = (originalValues[row].getSL() - meanSL) / stDevSL;
			double normSW = (originalValues[row].getSW() - meanSW) / stDevSW;
			double normPL = (originalValues[row].getPL() - meanPL) / stDevPL;
			double normPW = (originalValues[row].getPW() - meanPW) / stDevPW;

			normDataset[row] = new Flower(normSL, normSW, normPL, normPW, originalValues[row].getLabel());
		}
		return normDataset;
	}
	// --------------------------------------------------------------------------------------------------
	// FIND MEAN OF INPUT ARRAY
	// --------------------------------------------------------------------------------------------------

	public static double mean(double[] inputArray) {
		// Find mean
		double sum = 0;
		for (int index = 0; index < inputArray.length; index++) {
			sum += inputArray[index];
		}
		double mean = sum / inputArray.length;
		return mean;
	}
	// --------------------------------------------------------------------------------------------------
	// FIND STANDARD DEVIATION OF INPUT ARRAY
	// --------------------------------------------------------------------------------------------------

	public static double stDev(double[] inputArray, double mean) {
		// Find standard deviation
		double variance = 0;
		for (int index = 0; index < inputArray.length; index++) {
			variance += Math.pow((inputArray[index] - mean), 2) ;
		}
		double stDevSL = Math.sqrt(variance / (inputArray.length)) ;
		return stDevSL;
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
