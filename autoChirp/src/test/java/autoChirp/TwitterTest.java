package autoChirp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TwitterTest {
	
	private static  String dbPath = "src/test/resources/";
	private static String dbFileName = "autoChirp.db";
	private static String dbCreationFileName = "src/test/resources/createDatabaseFile.sql";
	
	@BeforeClass
	public static void dbConnection(){
		try {
			DBConnector.connect(dbPath + dbFileName);
			DBConnector.createOutputTables(dbCreationFileName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void twitterTest(){
		
		//TODO : read Keys from Congfig-Datei (gitigore)
		int user_id = DBConnector.insertNewUser("", "", "");
		Map<String, List<String>> tweetsByDate = new HashMap<String, List<String>>();
		List<String> tweets = Arrays.asList("tweet1", "tweet2", "tweet3");
		tweetsByDate.put("2017-01-15 12:00:00", tweets);
		tweets = Arrays.asList("next_tweet1", "next_tweet2");
		tweetsByDate.put("2017-01-16 12:00:00", tweets);
		List<Integer> user_ids = new ArrayList<Integer>();
		user_ids.add(user_id);
		DBConnector.insertTweets("test_url", tweetsByDate, user_ids, "test_title");
		//getAllGropIDs to update their status
		try{
			Statement stmt = DBConnector.connection.createStatement();
			String sql = "SELECT group_id FROM groups";
			ResultSet group_ids = stmt.executeQuery(sql);
			while(group_ids.next()){
				DBConnector.updateGroupStatus(group_ids.getInt(1), true);
			}	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		//get all new and enabled tweets
		Map<Integer,Map<String,List<String>>> allNewTweets = DBConnector.getAllNewTweets();
		Assert.assertEquals((Integer)user_id, allNewTweets.keySet().iterator().next());
		Assert.assertEquals(3, allNewTweets.get(user_id).get("2017-01-15 12:00:00").size());
		Assert.assertEquals("next_tweet2", allNewTweets.get(user_id).get("2017-01-16 12:00:00").get(1));
		for (Integer user : user_ids) {
			//TODO: Create TwitterConnection
		}
		
	}
	

}