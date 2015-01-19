package rs.poc.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import rs.poc.utils.Constants;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

public class ResultsGenerator {

	public static void main(String[] args) throws Exception {
		
		ArrayList<String> testUserIds = ResultsGenerator.getUserIds(Constants.TEST_DATA);
		
		HashMap<String, Instances> testInstances = new HashMap<String, Instances>();
		
		File result = new File(Constants.FILES_SOURCE_DIRECTORY + "initialResults.txt");
		FileWriter fw = new FileWriter(result);
		
		BufferedReader train = null;
		train = new BufferedReader(new FileReader(Constants.TRAIN_ARFF_DATA));
		Instances trainInst = new Instances(train);
		 //System.out.println("training" + inst.numInstances());
		 
		trainInst.setClassIndex(trainInst.numAttributes() - 1);
		train.close();
		
		BufferedReader test = null;
		for(String userId: testUserIds){
			test = new BufferedReader(new FileReader(Constants.USER_TEST_FILES_DIRECTORY + userId + "FW.arff"));
			Instances tInst = null;
			try{
				tInst = new Instances(test);
				 //System.out.println("test" + inst.numInstances());
				 if(tInst.numInstances() == 0){
					 tInst = null;
				 }
			} catch (Exception e){
				tInst = null;
			}
			testInstances.put(userId, tInst);
			test.close();
		}
		
		//TODO
		// create a hashmap for every instance get the id of the event
		String[] eventIds;
		
		IBk knn = new IBk(3);
		knn.buildClassifier(trainInst);
				
		for(String userId: testInstances.keySet()){
			System.out.println(userId);
			
			Instances testInst = testInstances.get(userId);
			
			if(testInst == null || testInst.numInstances() == 0){
				fw.append(userId + "," + "missing" + "\n");
				continue;
			}
			
			testInst.setClassIndex(testInst.numAttributes()-1);
			test = new BufferedReader(new FileReader(Constants.USER_TEST_FILES_DIRECTORY + userId + "IDs.txt"));
			eventIds = test.readLine().split(",");
			
			if(eventIds.length == 0){
				fw.append(userId + "," + "missing" + "\n");
				continue;
			}
			
			fw.append(userId + ",");
				
			StringBuilder sb = new StringBuilder();
			
			for (int i=0; i<testInst.numInstances(); i++) {
				Instance j = testInst.get(i);
				Double prediction = knn.classifyInstance(j);
				boolean interested = prediction.toString().equals("1.0") ? true : false;
				//Double confidence = knn.distributionForInstance(i)[0];
				
				if(interested){
					//TODO
					//get id for instance
					sb.append(eventIds[i] + ",");	
				}
				
			}
			String s = sb.toString();
			
			if(s.length() == 0){
				fw.append(userId + "," + "missing" + "\n");
				continue;
			}
			
			fw.append(s.substring(0, s.length()-1) + "\n");
			System.out.println("User with id: #" + userId + " done");
		}
		fw.flush();
		fw.close();

	}

	public static ArrayList<String> getUserIds(String file) throws Exception {
		BufferedReader trainingFileReader = new BufferedReader(new FileReader(file));
		ArrayList<String> userIds = new ArrayList<String>();
		
		String line;
		trainingFileReader.readLine();
		line=trainingFileReader.readLine();
		String userId = line.split(",")[0];
		String previousId = userId;
		userIds.add(userId);
		
		while((line=trainingFileReader.readLine())!=null){
			userId = line.split(",")[0];
			if(!userId.equals(previousId)){
				userIds.add(userId);
			}
			previousId = userId;
		}
		trainingFileReader.close();
		return userIds;
	}
	
}
