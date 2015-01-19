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
 * Class used to filter the relevant attendees records from the train/test files
 * @author Gorjan
 *
 */
public class AttendeesFileCleaner {
	
	public static void main(String[] args) throws Exception {
		//Reader in = new FileReader(Constants.FILES_SOURCE_DIRECTORY + "cleanEvents.csv");
		Reader in = new FileReader(Constants.TRAIN_EVENTS_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();
		
		//File cleanFriends = new File(Constants.FILES_SOURCE_DIRECTORY + "cleanAttendees.csv");
		File cleanFriends = new File(Constants.TRAIN_ATTENDEES_DATA);
		FileWriter fw = new FileWriter(cleanFriends);
		
		HashSet<String> testEventIds = new HashSet<String>();
		HashSet<String> passedAttendeesIds = new HashSet<String>();
		
		CSVRecord testEventRecord = null;
		iterator.next();
		
		String currentTestEventId;
		
		while(iterator.hasNext()){
			testEventRecord = iterator.next();
			currentTestEventId = testEventRecord.get(0);
			if(!testEventIds.contains(currentTestEventId)){
				testEventIds.add(currentTestEventId);
			}
		}
		
		System.out.println(testEventIds.size());
		
		Reader attendeesReader = new FileReader(Constants.EVENT_ATTENDEES_DATA);
		BufferedReader br = new BufferedReader(attendeesReader);
		
		int counter = 0;
		String line = null;
		String attendeesRecordId = null;
		while((line = br.readLine())!=null){
			attendeesRecordId = line.split(",")[0];
			if(testEventIds.contains(attendeesRecordId) &&
					!passedAttendeesIds.contains(attendeesRecordId)){
				counter++;
				fw.append(line + "\n");
				System.out.println(counter);
				passedAttendeesIds.add(attendeesRecordId);
			}
		}
		
		br.close();
		fw.flush();
		fw.close();
		
	}

}
