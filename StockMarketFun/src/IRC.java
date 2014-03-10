import org.jibble.pircbot.PircBot;

public class IRC
    extends PircBot
    implements Runnable
{
    public void onMessage(
        String channel,
        String sender,
        String login,
        String hostname,
        String message)
    {
        Parser.parse(message);
    }


    @Override
    public void run()
    {
        setName("TwitchPlaysMoney");
        try
        {
            this.connect(
                "irc.twitch.tv",
                6667,
                "oauth:");
        }
        catch (Exception e)
        {
        }
        this.joinChannel("#twitchplaysmoney");
    }

}
