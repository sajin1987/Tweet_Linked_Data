package serviceStart;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class is used for authorization to twitter streaming api.
 * @author Sajin
 *
 */
public class Authorization {

	public static Configuration getauthorization() {
		
		Properties props = new Properties();
		try {
			props.load(new FileReader("config.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ConsumerKey = props.getProperty("ConsumerKey");
		String ConsumerSecret = props.getProperty("ConsumerSecret");
		String access_token = props.getProperty("access_token");
		String access_token_secret = props.getProperty("access_token_secret");
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(ConsumerKey)
		.setOAuthConsumerSecret(ConsumerSecret)
		.setOAuthAccessToken(access_token)
		.setOAuthAccessTokenSecret(access_token_secret);
		
		return cb.build(); 
	}
}
