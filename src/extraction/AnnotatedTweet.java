package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Tweet;
import twitter4j.URLEntity;

/**
 * 
 * @author Sajin
 *
 */
public class AnnotatedTweet {

	private long id;
	private String user;
	private String text;
	private String date;
	private GeoLocation geoLocation;
	private URLEntity[] urls; 
	private HashMap<String, ArrayList<String>> entities;
	private HashtagEntity[] hashtags;
	private ArrayList<String> hashtagsNew;

	public AnnotatedTweet() {}

	public AnnotatedTweet(Tweet tweet) {
		setId(tweet.getId());
		setUser(tweet.getFromUser());
		setText(tweet.getText());
	}

	public void setUrls(URLEntity[] URLS){
		urls=URLS;
	}
	public URLEntity[] getUrls(){
		return urls;
	}

	public void setHashtags(HashtagEntity[] hashtags) {
		this.hashtags = hashtags;
	}
	public HashtagEntity[] getHashtags() {
		return hashtags;
	}

	public ArrayList<String> getHashtagsNew() {
		return hashtagsNew;
	}

	public void setHashtagsNew(ArrayList<String> hashtagsNew) {
		this.hashtagsNew = hashtagsNew;
	}

	public void setEntities(HashMap<String, ArrayList<String>> entities){
		this.entities=entities;
	}
	public HashMap<String, ArrayList<String>> getEntities(){
		return entities;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}

	public void setUser(String fromUser) {
		this.user = fromUser;
	}
	public String getUser() {
		return user;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
}
