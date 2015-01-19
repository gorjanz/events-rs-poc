package rs.poc.preprocessing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import rs.poc.models.MainFeatureVectorModel;
import rs.poc.utils.Constants;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;

public class TrainingFileBuilder {
	
	// TODO
	// load test users
	// choose a user
	// find all events in the training set that this user has responded to (attending, maybe, no)
	// find all friends of that user
	// for all events in the set
	// 	check if the creator of the event is in the friends set
	//  take the (lat,lon) of the event, and check if it matches the users location
	//	find the event in the attending data
	//	check how many of the users in the friends set said yes/maybe/no to this event
	//	calculate the time from the invite to the start-time of the event
	//	set the class attribute to interested if yes or maybe, not interested if no
	
	public static void main(String[] args) throws Exception {
		
		Reader in = new FileReader(Constants.TRAIN_DATA);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterator<CSVRecord> iterator = records.iterator();
		
		File cleanTrain = new File(Constants.TRAIN_FILES_SOURCE_DIRECTORY + "trainMFVWithoutLocation.csv");
		FileWriter fw = new FileWriter(cleanTrain);
		
		ArrayList<MainFeatureVectorModel> trainingInstances = new ArrayList<MainFeatureVectorModel>();
		iterator.next();
		
		MainFeatureVectorModel model = null;
		String testUserId, testEventId;
		boolean creatorFriend, skipThisRecord, location;
		double yesFriends,noFriends,maybeFriends;
		long timeFromInviteTillStart = 0;
		ArrayList<Double> eventDescWord = null;
		
		int done = 0;
		int all = 0;
		while(iterator.hasNext()){
			System.out.println(done + "/" + all);
			all++;
			CSVRecord record = iterator.next();
			skipThisRecord = false;
			yesFriends = 0;
			noFriends = 0;
			maybeFriends = 0;
			creatorFriend = false;
			location = false;
			timeFromInviteTillStart = 0;
			
			model = new MainFeatureVectorModel();
			
			//get the user id
			testUserId = record.get(0);
			
			//get the event id
			testEventId = record.get(1);
			
			System.out.println("For event: " + testEventId);
			
			// find user record
			Reader usersReader = new FileReader(Constants.TRAIN_USERS_DATA);
			Iterable<CSVRecord> userRecords = CSVFormat.DEFAULT.parse(usersReader);
			Iterator<CSVRecord> userIterator = userRecords.iterator();
			
			boolean userFound = false;
			userIterator.next();
			CSVRecord userRecord = null;
			while(!userFound && userIterator.hasNext()){
				userRecord = userIterator.next();
				if(userRecord.get(0).equals(testUserId)){
					userFound = true;
					// TODO: get user info
				}
				
				if(!userIterator.hasNext() && !userFound){
					// throw new RuntimeException("No record for user with id: " + testUserId);
					skipThisRecord = true;
				}
			}
			
			if(skipThisRecord){
				System.out.println("continue");
				continue;
			}
			
			System.out.println("user info found" + testUserId);
			
			// find event record
			Reader eventsReader = new FileReader(Constants.TRAIN_EVENTS_DATA);
			Iterable<CSVRecord> eventsRecords = CSVFormat.DEFAULT.parse(eventsReader);
			Iterator<CSVRecord> eventsIterator = eventsRecords.iterator();
			
			boolean eventFound = false;
			eventsIterator.next();
			
			CSVRecord eventRecord = null;
			while(!eventFound && eventsIterator.hasNext()){
				eventRecord = eventsIterator.next();
				if(eventRecord.get(0).equals(testEventId)){
					eventFound = true;
					// TODO: get event info
					eventDescWord = new ArrayList<Double>();
					for (int i = 9; i < 110; i++) {
						eventDescWord.add(Double.parseDouble(eventRecord.get(i)));
					}
					
				}
				
				if(!eventsIterator.hasNext() && !eventFound){
					//throw new RuntimeException("No record for event with id: " + testEventId);
					skipThisRecord = true;
				}
				
			}
			
			if(skipThisRecord){
				System.out.println("continue");
				continue;
			}
			
			System.out.println("event info found");
			
			// find friends record for test user
			Reader friendsReader = new FileReader(Constants.TRAIN_FRIENDS_DATA);
			Iterable<CSVRecord> friendsRecords = CSVFormat.DEFAULT.parse(friendsReader);
			Iterator<CSVRecord> friendsIterator = friendsRecords.iterator();
			
			// have all the IDs of the user friends
			ArrayList<String> userFriendIds = new ArrayList<String>();
			
			boolean friendsRecordFound = false;
			friendsIterator.next();
			
			CSVRecord friendsRecord = null;
			while(!friendsRecordFound && friendsIterator.hasNext()){
				friendsRecord = friendsIterator.next();
				if(friendsRecord.get(0).equals(testUserId)){
					friendsRecordFound = true;
					// TODO: get friends IDs
					String[] parts = friendsRecord.get(1).split(" ");
					for (int i = 0; i < parts.length; i++) {
						userFriendIds.add(parts[i]);
					}
				}
				
				if(!friendsIterator.hasNext() && !friendsRecordFound){
					// set all friends derived info to false/0
					
					//throw new RuntimeException("no user friends record for user with id: " + testUserId);
					skipThisRecord = true;
				}
			}
			
			if(skipThisRecord){
				System.out.println("continue");
				continue;
			}
			
			System.out.println("friend info found" + userFriendIds.size());
			
			// is the creator of the event friend of the test user
			for (int i = 0; i < userFriendIds.size(); i++) {
				if (userFriendIds.get(i).equals(eventRecord.get(1))){
					creatorFriend = true;
				}
			}
			// find event attendees info for event record
			Reader attendeesReader = new FileReader(Constants.TRAIN_ATTENDEES_DATA);
			Iterable<CSVRecord> attendeesRecords = CSVFormat.DEFAULT.parse(attendeesReader);
			Iterator<CSVRecord> attendeesIterator = attendeesRecords.iterator();
			
			attendeesIterator.next();
			CSVRecord attendeesRecord = null;
			boolean attendeesFound = false;
			while(!attendeesFound && attendeesIterator.hasNext()){
				attendeesRecord = attendeesIterator.next();
				if(attendeesRecord.get(0).equals(testEventId)){
					attendeesFound = true;
					
					System.out.println("attendees Found");
					// TODO find yes-friends, no-friends, maybe-friends
					String[] yesFriendsIds = attendeesRecord.get(1).split(" ");
					String[] maybeFriendsIds = attendeesRecord.get(2).split(" ");
					String[] noFriendsIds = attendeesRecord.get(4).split(" ");
					
					System.out.println("yes friends: " + yesFriendsIds.length);
					System.out.println("maybe friends: " + maybeFriendsIds.length);
					System.out.println("no friends: " + noFriendsIds.length);
					
					int num= 0;
					// calculate how many friends said yes
					for (int i = 0; i < yesFriendsIds.length; i++) {
						for (int j = 0; j < userFriendIds.size(); j++) {
							if (userFriendIds.get(j).equals(yesFriendsIds[i])){
								System.out.println("found a yes friend");
								num++;
							}
						}
					}
					
					yesFriends = num / (double) userFriendIds.size();
					num = 0;

					// calculate how many friends said maybe
					for (int i = 0; i < maybeFriendsIds.length; i++) {
						for (int j = 0; j < userFriendIds.size(); j++) {
							if (userFriendIds.get(j).equals(maybeFriendsIds[i])){
								System.out.println("found a maybe friend");
								num++;
							}
						}
					}
					maybeFriends = num / (double) userFriendIds.size();
					num = 0;
					
					// calculate how many friends said no
					for (int i = 0; i < noFriendsIds.length; i++) {
						for (int j = 0; j < userFriendIds.size(); j++) {
							if (userFriendIds.get(j).equals(noFriendsIds[i])){
								System.out.println("found a no friend");
								num++;
							}
						}
					}
					noFriends = num / (double) userFriendIds.size();
				}
				
				if(!attendeesFound && !attendeesIterator.hasNext()){
					// set all attendees derived info to false/0
					
					skipThisRecord = true;
					//throw new RuntimeException("No event attendees record found for event with id: " + testEventId);
				}
			}

			if(skipThisRecord){
				System.out.println("continue");
				continue;
			}
			
			System.out.println("attendees info found" + yesFriends + "/"+ maybeFriends + "/" + noFriends);
			
			//TODO calculate location
			String city = eventRecord.get(3);
			String state = eventRecord.get(4);
			String zip = eventRecord.get(5);
			String address = city + ", " + state + ", " + zip;
			
			GeoApiContext context = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY);
			GeocodingApiRequest req = GeocodingApi.newRequest(context).address(address);

			double eventResultLatitude=0, eventResultLongitude=0,
					userResultLatitude=0, userResultLongitude=0;
			boolean eventLocationResultFound=false, userLocationResultFound=false;
			GeocodingResult[] result = null;
			try {
				if(!address.equals("")){
				    result = req.await();
				    // Handle successful request.
				    eventResultLatitude = result[0].geometry.location.lat;
				    eventResultLongitude = result[0].geometry.location.lng;
				    eventLocationResultFound = true;
				}
			    
			    String userLocation = userRecord.get(5);
			    if(!userLocation.equals("")){
			    	req = GeocodingApi.newRequest(context).address(userLocation);
			    	result = req.await();
			    	userResultLatitude = result[0].geometry.location.lat;
				    userResultLongitude = result[0].geometry.location.lng;
				    userLocationResultFound = true;
			    }
			    
			    if(userLocationResultFound && eventLocationResultFound){
			    	if(distance(userResultLatitude, userResultLongitude,
			    			eventResultLatitude, eventResultLongitude) < Constants.EVENT_RADIUS){
			    		location = true;
			    	}
			    }
			    
			} catch (Exception e) {
			    // Handle error
				
				// set location derived info to false/0
				//throw new RuntimeException("Cannot get location info for (user,event) pair: " + 
											//"(" + testUserId + "," + testEventId + ")");
				
			}
			
			//TODO calculate time from invite to event start
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						Constants.INVITED_AT_DATE_FORMAT);
				Date inviteDate = dateFormat.parse(record.get(3).split("\\.")[0]);
				
				dateFormat = new SimpleDateFormat(
						Constants.EVENT_START_DATE_FORMAT);
				Date eventStartDate = dateFormat.parse(eventRecord.get(2));
				timeFromInviteTillStart = Math.abs(inviteDate.getTime() - eventStartDate.getTime());
				System.out.println("time info set" + timeFromInviteTillStart);
			} catch (Exception e) {
				// if unable to calculate the time due to missing data, set the time to one week
				timeFromInviteTillStart = 604800000;
			}
			
			//TODO and get the words occurrence from event record
			double[] contentWords = new double[101];
			for (int i = 9; i < 110; i++) {
				contentWords[i-9] = Double.parseDouble(eventRecord.get(i));
			}
			
			//TODO create MainFeatureVectoreModel object from calculated features
			model = new MainFeatureVectorModel(testEventId,
					testUserId,
					creatorFriend,
					location,
					yesFriends,
					maybeFriends,
					noFriends,
					timeFromInviteTillStart,
					contentWords);
			
			int interested = Integer.parseInt(record.get(4));
			if(interested == 1){
				model.setClassAtribute(true);
			} else {
				model.setClassAtribute(false);
			}
		
			fw.append(model.toString());
			trainingInstances.add(model);
			done++;
		}
		
		fw.flush();
		fw.close();
	}
	
	public static double distance(double userLat, double userLng, double eventLat, double eventLng){
		return Math.abs(Math.sqrt(Math.pow((userLat - eventLat), 2) + Math.pow((userLng - eventLng),2)));
	}
}
