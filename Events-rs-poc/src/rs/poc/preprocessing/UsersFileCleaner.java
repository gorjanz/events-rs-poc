package rs.poc.preprocessing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import rs.poc.utils.Constants;

/**
 * Class used to filter the relevant users records from the train/test files
 * @author Gorjan
 *
 */
public class UsersFileCleaner {
	
	public static void main(String[] args) throws Exception {
		//Reader in = new FileReader(Constants.TEST_DATA);
		Reader in = new FileReader(Constants.TRAIN_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();
		
		//File cleanUsers = new File(Constants.FILES_SOURCE_DIRECTORY + "cleanUsers.csv");
		File cleanUsers = new File(Constants.TRAIN_USERS_DATA);
		FileWriter fw = new FileWriter(cleanUsers);
		
		CSVRecord testRecord = null;
		CSVRecord userRecord = null;
		boolean found;
		StringBuilder sb = null;
		String testUserId = null;
		
		String previousUserId = "";
		
		iterator.next();
		int usersFound = 0;
		
		while(iterator.hasNext()){
			testRecord = iterator.next();
			testUserId = testRecord.get(0);
			found = false;
			
			if(testUserId.equals(previousUserId)){
				continue;
			}
			
			Reader usersIn = new FileReader(Constants.USERS_DATA);
			Iterable<CSVRecord> userRecords = CSVFormat.DEFAULT.parse(usersIn);
			Iterator<CSVRecord> usersIterator = userRecords.iterator();
			
			usersIterator.next();
			while(usersIterator.hasNext() && !found){
				userRecord = usersIterator.next();
				if(testUserId.equals(userRecord.get(0))){
					found = true;
					usersFound++;
					System.out.println(usersFound);

					sb = new StringBuilder();
					sb.append(userRecord.get(0)+",");
					sb.append(userRecord.get(1)+",");
					sb.append(userRecord.get(2)+",");
					sb.append(userRecord.get(3)+",");
					sb.append(userRecord.get(4)+",");
					sb.append(userRecord.get(5)+",");
					sb.append(userRecord.get(6));
					fw.append(sb.toString()+"\n");
				}
			}
			
			previousUserId = testUserId;
		}
		fw.flush();
		fw.close();

	}
	
}
