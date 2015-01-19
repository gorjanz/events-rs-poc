package rs.poc.models;


public class MainFeatureVectorModel {

	/** the id of the event */
	private String eventId;
	
	/** the id of the user */
	private String userId;
	
	/** is the creator of the event a friend of the user (derived) */
	private boolean isCreatorAFriend;
	
	/** is the user in k miles from the event (lat, lon) (derived) */
	private boolean location;
	
	/** how many friends of the user has responded yes */
	private double yesFriends;
	
	/** how many friends of the user has responded maybe */
	private double maybeFriends;
	
	/** how many friends of the user has responded no */
	private double noFriends;
	
	/** how much time from the invite till the start of the event */
	private long timeFromInviteTillStart;
	
	/** words describing the event */
	private double[] contentWords;
	
	/** if true, user is interested in attending the event, otherwise not interested */
	private boolean classAtribute;

	public MainFeatureVectorModel(){}
	
	public MainFeatureVectorModel(String eventId, String userId, boolean isCreatorAFriend,
			boolean location, double yesFriends, double maybeFriends,
			double noFriends, long timeFromInviteTillStart,
			double[] contentWords) {
		super();
		this.eventId = eventId;
		this.userId = userId;
		this.isCreatorAFriend = isCreatorAFriend;
		this.location = location;
		this.yesFriends = yesFriends;
		this.maybeFriends = maybeFriends;
		this.noFriends = noFriends;
		this.timeFromInviteTillStart = timeFromInviteTillStart;
		this.contentWords = contentWords;
	}
	
	public boolean isClassAtribute() {
		return classAtribute;
	}

	public void setClassAtribute(boolean classAtribute) {
		this.classAtribute = classAtribute;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(eventId + ",");
		sb.append(userId + ",");
		sb.append(isCreatorAFriend + ",");
		sb.append(location + ",");
		sb.append(String.format("%.6f",yesFriends) + ",");
		sb.append(String.format("%.6f",maybeFriends) + ",");
		sb.append(String.format("%.6f",noFriends) + ",");
		sb.append(timeFromInviteTillStart + ",");
		for (int i = 0; i < contentWords.length; i++) {
			sb.append(contentWords[i] + ",");
		}
		sb.append(classAtribute + "\n");
		return sb.toString();
	}
	
}
