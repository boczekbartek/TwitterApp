package twitterApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import com.sun.jersey.core.util.Base64;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public final class Account {
	private static RequestToken requestToken;
	private static TwitterFactory tf;
	public static Twitter twitter;
	private static AccessToken accessToken;
	private static ConfigurationBuilder cb;
	private static String consumerKey;
	private static String consumerSecret;

	public Account(String cK, String cS) {
		consumerKey = cK;
		consumerSecret = cS;
	}

	public String generateURL() {
		cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(null).setOAuthAccessTokenSecret(null);

		tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

		try {
			requestToken = twitter.getOAuthRequestToken();

			return requestToken.getAuthorizationURL();
		} catch (TwitterException e) {

			e.printStackTrace();
			return null;
		}

	}

	public boolean authoriseAccount() {
		try {
			File accessTokens = new File("accessToken");
			File accessTokensSecret = new File("accessTokenSecret");
			Scanner sc = new Scanner(accessTokens);
			String at = new String(Base64.decode(sc.nextLine()));

			Scanner sct = new Scanner(accessTokensSecret);
			String ats = new String(Base64.decode(sct.nextLine()));

			cb = new ConfigurationBuilder();
			// the following is set without accesstoken- desktop client
			cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
					.setOAuthAccessToken(at).setOAuthAccessTokenSecret(ats);

			sct.close();
			sc.close();

			tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();

			twitter.verifyCredentials();
			return true;
		} catch (TwitterException e) {
			System.out.println("Nie udalo sie zalogowac");
			return false;
		} catch (FileNotFoundException e) {
			System.out.println("Nie znaleziono pliku z tokenami");
			return false;
		} catch (NullPointerException e) {
			System.out.println("Nie znaleziono pliku z tokenami");
			return false;
		}
	}

	public boolean authoriseAccount(String pin) {
		try {
			if (pin.length() > 0) {
				// tu trzeba zrobić accesToken w innej klasie
				// zawierająćej samo logowanie

				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			} else {
				accessToken = twitter.getOAuthAccessToken(requestToken);
			}
			// cb.setDebugEnabled(true).setOAuthAccessToken(accessToken.getToken())
			// .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
			//
			// tf = new TwitterFactory(cb.build());
			// twitter = tf.getInstance();
			System.out.println("Got access token.");
			System.out.println("Access token: " + accessToken.getToken());
			System.out.println("Access token secret: " + accessToken.getTokenSecret());

			try {
				byte data[] = Base64.encode(accessToken.getToken().getBytes());
				Path file = Paths.get("accessToken");
				Files.write(file, data);

				byte data2[] = Base64.encode(accessToken.getTokenSecret().getBytes());
				file = Paths.get("accessTokenSecret");
				Files.write(file, data2);
			} catch (FileNotFoundException | UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.getStackTrace();
			}

			return true;
		} catch (TwitterException e) {
			if (401 == e.getStatusCode()) {
				System.out.println("Unable to get the access token.");
			} else {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}

	public void saveTimelineToFile(List<String> timeline, String user) {
		try {
			Path file = Paths.get(user + "_timeline.txt");
			Files.write(file, timeline, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.out.println("Nie udalo sie zapisac timeline'a do pliku");
		}
	}

	public Twitter getTwitter() {
		return twitter;
	}

}