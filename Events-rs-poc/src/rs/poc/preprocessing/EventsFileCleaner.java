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
 * Class used to filter the relevant event records from the train/test files
 * @author Gorjan
 *
 */
public class EventsFileCleaner {
	
	public static void main(String[] args) throws Exception {
		//Reader in = new FileReader(Constants.TEST_DATA);
		Reader in = new FileReader(Constants.TRAIN_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();
		
		//File cleanEvents = new File(Constants.FILES_SOURCE_DIRECTORY + "cleanEvents.csv");
		File cleanEvents = new File(Constants.TRAIN_EVENTS_DATA);
		FileWriter fw = new FileWriter(cleanEvents);
		
		String testEventId = null;
		
		String eventId = "";
		String eventLine = null;
		
		BufferedReader br = null;
		
		iterator.next();
		
		HashSet<String> trainIds = new HashSet<String>();
		while(iterator.hasNext()){
			testEventId = iterator.next().get(1);
			if(!trainIds.contains(testEventId)){
				trainIds.add(testEventId);
			}
		}
		
		int j = 0;
		try{
			Reader eventsIn = new FileReader(Constants.EVENTS_DATA);
			br = new BufferedReader(eventsIn);
			br.readLine();

			while(!trainIds.isEmpty() && (eventLine = br.readLine())!=null){
				eventId = eventLine.split(",")[0];
				if(!trainIds.contains(eventId)){
					continue;
				} else {
					trainIds.remove(eventId);
					fw.append(eventLine+"\n");
					System.out.println(++j + "|"+ eventId);
				}
			}
				
		} catch(Exception e){
			System.out.println("Cannot parse info for event with id: " + testEventId);
		}
	
		fw.flush();
		fw.close();
		br.close();
		
	}

}
