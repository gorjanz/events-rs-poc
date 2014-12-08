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

import rs.poc.models.User;

public class UserParser {

	public static ArrayList<User> loadUsers() throws IOException, ParseException {

		Reader in = new FileReader(Constants.USERS_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();

		ArrayList<User> users = new ArrayList<User>();

		User usr = null;

		iterator.next();
		while (iterator.hasNext()) {
			CSVRecord record = iterator.next();

			usr = new User();
			usr.setUser_id(record.get(0));
			usr.setBirthYear(Integer.parseInt(record.get(2)));
			usr.setGender(record.get(3).equalsIgnoreCase("male") ? true : false);

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					Constants.JOINED_AT_DATE_FORMAT);
			Date parsedDate = dateFormat.parse(record.get(4));
			usr.setJoinedAt(new java.sql.Timestamp(parsedDate.getTime()));
			usr.setLocation(record.get(5));
			users.add(usr);
			System.out.println(usr);
		}

		System.out.println("users#: " + users.size());

		return users;
	}

	public static ArrayList<String> getFriendIdsForUser(String userId)
			throws IOException, ParseException {

		Reader in = new FileReader(Constants.USER_FRIENDS_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();

		ArrayList<String> friends = new ArrayList<String>();

		iterator.next();
		while (iterator.hasNext()) {
			CSVRecord record = iterator.next();
			if(record.get(0).equals(userId)){
				String[] parts = record.get(1).split(" ");
				for (int i = 0; i < parts.length; i++) {
					friends.add(parts[i]);
				}
				break;
			}
		}
		
		return friends;
	}

}
