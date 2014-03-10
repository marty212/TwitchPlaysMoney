import twitter4j.conf.ConfigurationBuilder;
import twitter4j.TwitterException;
import java.util.List;
import java.util.ArrayList;
import twitter4j.*;

public class TwitterSupport
{
    protected static ArrayList<String> stat;
    protected static TwitterFactory tf;

    public static void init()
    {
        stat = new ArrayList<>();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("").setOAuthConsumerSecret("")
        .setOAuthAccessToken("-")
        .setOAuthAccessTokenSecret("");
        tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            User user = twitter.verifyCredentials();
            List<Status> statuses = twitter.getMentionsTimeline();
            for (Status status : statuses) {
                stat.add(status.getCreatedAt().getTime() + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
        }
    }

    public static void update()
    {
        Twitter twitter = tf.getInstance();
        try {
            User user = twitter.verifyCredentials();
            List<Status> statuses = twitter.getMentionsTimeline();
            for (Status status : statuses) {
                if(stat.remove(status.getCreatedAt().getTime() + status.getText()))
                {
                    stat.add(status.getCreatedAt().getTime() + status.getText());
                }
                else
                {
                    String str = status.getText();
                    stat.add(status.getCreatedAt().getTime() + str);
                    str = str.substring(str.indexOf(' ') + 1);
                    Parser.parse(str);
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
        }
        while(stat.size() > 20)
        {
            stat.remove(0);
        }
    }
}
