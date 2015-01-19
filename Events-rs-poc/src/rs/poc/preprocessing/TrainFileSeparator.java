package rs.poc.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import rs.poc.utils.Constants;

/**
 * Class used to create a training file for each user from the test set,
 * find all derived feature vectors for that user, and create a new
 * file by appending the .arff header
 * @author Gorjan
 *
 */
public class TrainFileSeparator {

	//TODO
	//for each user
	//	open a new file named $userId.arff
	//	take the train file $userId.txt
	//	append the derived vector arff header
	//	for each instance in the training file
	//  calculate the derived vector
	//  write it to the arff training file
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader trainingFileReader = new BufferedReader(new FileReader(Constants.TRAIN_DATA));
		ArrayList<String> userIds = new ArrayList<String>();
		
		FileWriter fw = null;
		
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
		
		for (String userRecord: userIds) {
			Reader in = new FileReader(Constants.USER_TRAINING_FILES_DIRECTORY + userRecord + ".txt");
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
			Iterator<CSVRecord> iterator = records.iterator();
			
			File separate = new File(Constants.USER_TRAINING_FILES_DIRECTORY + userRecord + "FW.arff");
			fw = new FileWriter(separate);

			BufferedReader headerInfoReader = new BufferedReader(new FileReader(Constants.FILES_SOURCE_DIRECTORY + "arff-header.txt"));
			while((line=headerInfoReader.readLine())!=null){
				fw.append(line + "\n");
			}
			headerInfoReader.close();
			
			BufferedReader calcVectors = new BufferedReader(new FileReader(Constants.FILES_SOURCE_DIRECTORY + "trainMFVWithoutLocation.csv"));
			
			CSVRecord record;
			String eventId;
			while(iterator.hasNext()){
				record = iterator.next();
				eventId = record.get(1);

				while((line=calcVectors.readLine())!=null){
					if(eventId.equals(line.split(",")[0])){
						int index = line.indexOf(",");
						fw.append(line.substring(index+1) + "\n");
					}
				}
			}
			
			System.out.println("user with id: #" + userRecord + " done");
			fw.flush();
			fw.close();
			calcVectors.close();						
		}		
	

		trainingFileReader.close();
	}
	

}
