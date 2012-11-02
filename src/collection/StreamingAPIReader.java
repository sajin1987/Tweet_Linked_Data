package collection;

import java.util.ArrayList;
import java.util.HashMap;
import rdfising.TweetRDFModel;
import serviceStart.Authorization;
import serviceStart.ServiceVariables;
import extraction.AnnotatedTweet;
import extraction.NamedEntityExtractor;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;

/**
 * This class collects tweets from Twitter Streaming API and sets different metadata of tweets into java objects
 * 
 * @author Sajin
 */

public final class StreamingAPIReader {

	public static void readAPI(final ServiceVariables variables) throws TwitterException {

		StatusListener listener = new StatusListener() {

			public void onStatus(Status status) {

				String nerClassifier = variables.getNerClassifier();

				String user = status.getUser().getName();
				String text = status.getText();
				long id = status.getId();
				GeoLocation location = status.getGeoLocation();
				HashtagEntity[] hashtags = status.getHashtagEntities();
				URLEntity[] URLS = status.getURLEntities();

				NamedEntityExtractor tags = new NamedEntityExtractor();
				HashMap<String, ArrayList<String>> entities = tags.getEntities(text, nerClassifier);

				AnnotatedTweet tweet = new AnnotatedTweet();
				tweet.setUser(user);
				tweet.setText(text);
				tweet.setGeoLocation(location);
				tweet.setHashtags(hashtags);
				tweet.setUrls(URLS);
				tweet.setId(id);
				tweet.setEntities(entities);

				TweetRDFModel.tweetToTriples(tweet, variables);
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				// System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			/**
			 * 
			 */
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				//  System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			/**
			 * 
			 * @param userId
			 * @param upToStatusId
			 */
			public void onScrubGeo(long userId, long upToStatusId) {
				//  System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			/**
			 * 
			 */
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		TwitterStream twitterStream = new TwitterStreamFactory(Authorization.getauthorization()).getInstance();
		twitterStream.addListener(listener);
		twitterStream.sample();
	}
}


