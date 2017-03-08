package twitterApp;

import java.io.IOException;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Tools {
	@Deprecated
	public static List<Status> searchWords(Account account,  String tagId,Integer postsNum) {
		try {
			int maxQueriesNum = 0;
			int restQueriesNum = 0;
			Query query = new Query(tagId);
			QueryResult result;

			List<Status> tweets = null;
			if (postsNum <= 100) {
				maxQueriesNum = 0;
				restQueriesNum = postsNum;
			} else if (postsNum > 100 && postsNum < 1500) {
				maxQueriesNum = postsNum / 100; // liczba zapytań do bazy danych
												// po 100
				restQueriesNum = postsNum % 100; // liczba zapytań pozostalych
			} else {
				// tu trzeba zrobić wyjątek
				System.out.println("Niestety Twitter nie pozawala pobrać na raz z bazy danych tylu postów");
			}

			System.out.println("maxQueriesNum: " + maxQueriesNum + "restQueriesNum: " + restQueriesNum);
			query.setCount(restQueriesNum);
			result = account.getTwitter().search(query);
			tweets = result.getTweets();

			query.setCount(100);
			for (int i = 0; i < maxQueriesNum; i++) {
				if (result.hasNext()) {
					System.out.println(i);
					query = result.nextQuery();
					result = account.getTwitter().search(query);
					tweets.addAll(result.getTweets());
				}
			}

			return tweets;

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed");
			return null;

		}
	}

	public static List<Status> getUserTweets(Twitter twitter, String user, int tweetsNum)throws TwitterException,IOException{
		
			int maxQueriesNum = 0;
			int restQueriesNum = 0;
			if (tweetsNum <= 100) {
				maxQueriesNum = 0;
				restQueriesNum = tweetsNum;
			} else if (tweetsNum > 100 && tweetsNum < 1500) {
				maxQueriesNum = tweetsNum / 100; // liczba zapytań do bazy danych
												// po 100
				restQueriesNum = tweetsNum % 100; // liczba zapytań pozostalych
			} else {
				throw new IOException();
			}
			if (restQueriesNum == 0)
				restQueriesNum =1;
			Paging page = new Paging(1,restQueriesNum);
			List<Status> tweets = twitter.getUserTimeline(user, page);
			page.setCount(100);
			for (int i = 0; i < maxQueriesNum; i++) {
				page.setPage(i+1);
				tweets.addAll(twitter.getUserTimeline(user, page));
			}
			return tweets;
			
	}

	public static String listStatusToString(List<Status> tweets) {
		String stat = "";
		int i = 0;
		for (Status st : tweets) {
			i++;
			stat = stat + "-----------------------------------------\n ***" + i + "***\n";
	//		stat = stat + st.getUser().getName() + "------" + st.getText() + "\n";
			stat = stat +st.getText() + "\n";
		}
		return stat;
	}
}