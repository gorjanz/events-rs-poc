package rs.poc.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import rs.poc.models.Event;

public class EventParser {

	public static ArrayList<Event> loadEvents() throws IOException, ParseException{
		Reader in = new FileReader(Constants.EVENTS_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();

		ArrayList<Event> events = new ArrayList<Event>();
		iterator.next();
		
		Event evt = null;
		
		while(iterator.hasNext()){
			CSVRecord record = iterator.next();
			evt = new Event();
			
			evt.setId(record.get(0));
			evt.setCreatorId(record.get(1));
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					Constants.JOINED_AT_DATE_FORMAT);
			Date parsedDate = dateFormat.parse(record.get(2));
			evt.setStartTime(new java.sql.Timestamp(parsedDate.getTime()));
			
			evt.setCity(record.get(3));
			evt.setState(record.get(4));
			evt.setZip(record.get(5));
			evt.setCountry(record.get(6));
			
			if(!record.get(7).isEmpty()){
				evt.setLat(Double.parseDouble(record.get(7)));
			} else {
				evt.setLat(-1.0);
			}
						
			if(!record.get(8).isEmpty()){
				evt.setLon(Double.parseDouble(record.get(8)));
			} else {
				evt.setLon(-1.0);
			}


			for (int i = 9; i < 110; i++) {
				String wordOcc = record.get(i);
				if(!wordOcc.isEmpty()){
					evt.addWordData(Double.parseDouble(wordOcc));
				} else {
					evt.addWordData(0);
				}
			}
			System.out.println(evt);
		}
		
		return events;
	}
	
}
