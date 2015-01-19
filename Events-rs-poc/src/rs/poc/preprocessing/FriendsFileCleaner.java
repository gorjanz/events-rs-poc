package rs.poc.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import rs.poc.utils.Constants;

/**
 * Class used to filter the relevant friends records from the train/test files
 * @author Gorjan
 *
 */
public class FriendsFileCleaner {
	
	public static void main(String[] args) throws Exception {
		//Reader in = new FileReader(Constants.FILES_SOURCE_DIRECTORY + "cleanUsers.csv");
		Reader in = new FileReader(Constants.TRAIN_USERS_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();
		
		File cleanFriends = new File(Constants.TRAIN_FRIENDS_DATA);
		FileWriter fw = new FileWriter(cleanFriends);
		
		HashSet<String> testUsersIds = new HashSet<String>();
		HashSet<String> passedFriendsIds = new HashSet<String>();
		
		CSVRecord testUserRecord = null;
		iterator.next();
		
		String currentTestUsedId;
		
		while(iterator.hasNext()){
			testUserRecord = iterator.next();
			currentTestUsedId = testUserRecord.get(0);
			if(!testUsersIds.contains(currentTestUsedId)){
				testUsersIds.add(currentTestUsedId);
			}
		}
		
		System.out.println(testUsersIds.size());
		
		Reader friendsReader = new FileReader(Constants.USER_FRIENDS_DATA);
		BufferedReader br = new BufferedReader(friendsReader);
		
		int counter = 0;
		String line = null;
		String friendRecordId = null;
		while((line = br.readLine())!=null){
			friendRecordId = line.split(",")[0];
			if(testUsersIds.contains(friendRecordId) &&
					!passedFriendsIds.contains(friendRecordId)){
				counter++;
				fw.append(line + "\n");
				System.out.println(counter);
				passedFriendsIds.add(friendRecordId);
			}
		}
		
		br.close();
		fw.flush();
		fw.close();
		
	}

}
