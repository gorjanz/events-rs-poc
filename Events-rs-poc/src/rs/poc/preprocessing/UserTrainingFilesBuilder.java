package rs.poc.preprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import rs.poc.utils.Constants;

public class UserTrainingFilesBuilder {

	public static void main(String[] args) throws Exception {
		BufferedReader trainingFileReader = new BufferedReader(new FileReader(Constants.TRAIN_DATA));
		FileWriter userTrainingFilesWriter = null;
		
		String line;
		trainingFileReader.readLine();
		String userId;
		String previousId;
		
		line=trainingFileReader.readLine();
		userId = line.split(",")[0];
		userTrainingFilesWriter = new FileWriter(Constants.USER_TRAINING_FILES_DIRECTORY + userId + ".txt");
		userTrainingFilesWriter.append(line + "\n");
		previousId = userId;
		
		while((line=trainingFileReader.readLine())!=null){
			userId = line.split(",")[0];
			if(userId.equals(previousId)){
				userTrainingFilesWriter.append(line + "\n");
			} else {
				userTrainingFilesWriter.flush();
				userTrainingFilesWriter.close();
				userTrainingFilesWriter = new FileWriter(Constants.USER_TRAINING_FILES_DIRECTORY + userId + ".txt");
				userTrainingFilesWriter.append(line + "\n");
			}
			previousId = userId;
		}
		
		userTrainingFilesWriter.flush();
		trainingFileReader.close();
		userTrainingFilesWriter.close();
	}
	
}
