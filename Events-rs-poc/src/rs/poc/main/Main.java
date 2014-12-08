package rs.poc.main;

import java.io.IOException;
import java.text.ParseException;

import rs.poc.utils.EventParser;
import rs.poc.utils.UserParser;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {

		//System.out.println(UserParser.getFriendIdsForUser("2008043603"));
		System.out.println(EventParser.loadEvents());
	}
}
