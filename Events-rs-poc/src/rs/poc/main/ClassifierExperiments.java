package rs.poc.main;

import java.io.BufferedReader;
import java.io.FileReader;

import rs.poc.utils.Constants;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

public class ClassifierExperiments {

	public static void main(String[] args) throws Exception {
		
		BufferedReader trainFileReader = new BufferedReader(new FileReader(Constants.TRAIN_ARFF_DATA));
		Instances trainData = new Instances(trainFileReader);
		trainData.setClassIndex(trainData.numAttributes() - 1);
		trainFileReader.close();
		
		BufferedReader testFileReader = new BufferedReader(new FileReader(Constants.TEST_ARFF_DATA));
		Instances testData = new Instances(testFileReader);
		testData.setClassIndex(testData.numAttributes() - 1);
		
		IBk knn = new IBk(3);
		//NaiveBayes knn = new NaiveBayes();
		knn.buildClassifier(trainData);
		
		int numInstances = testData.numInstances();
		for (int i = 0; i < numInstances; i++) {
			Double prediction = knn.classifyInstance(testData.instance(0));
			System.out.println("for instance #" + i + " the prediction is" + 
					(prediction.toString().equals("1.0") ? "true" : "false"));
			System.out.println("with confidence: " + knn.distributionForInstance(testData.get(i))[0]);
			
		}

	}
	
}
