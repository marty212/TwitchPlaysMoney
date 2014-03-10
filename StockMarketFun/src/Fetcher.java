import java.util.Date;
import java.util.Vector;
import org.json.JSONObject;
import java.util.Scanner;
import java.net.URL;

public class Fetcher
{
    protected static Vector<Fetcher.stored> storage = new Vector<>();

    protected static class stored{
        protected double price;
        protected String name;
        protected Date   date;
        public stored(double p, String str)
        {
            price = p;
            name = str;
            date  = new Date();
        }
        public boolean equals(stored other)
        {
            return price == other.price && name.equals(other.name);
        }
    }

    public static double lru(String name)
    {
        for(stored s : storage)
        {
            if(s.name.equals(name) && (s.date.getTime() - (new Date()).getTime()) >= -60*1000)
            {
                storage.remove(s);
                storage.add(s);
                return s.price;
            }
            else if(s.name.equals(name))
            {
                storage.remove(s);
                return -1;
            }
        }
        return -1;
    }
    public static void addStorage(stored s)
    {
        storage.add(s);
        if(storage.size() > 20)
        {
            storage.remove(0);
        }
    }
    public static double fetchCoin(String name)
    {
        try
        {
            double ret = lru(name + "coin");
            if(ret != -1)
            {
                return ret;
            }
            URL url =
                new URL("http://coinmarketcap.northpole.ro/api/" + name
                    + ".json");
            String out =
                new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")
                    .next();
            JSONObject a = new JSONObject(out);
            if (a.has("price"))
            {
                addStorage(new stored(a.getDouble("price"), name + "coin"));
                return a.getDouble("price");
            }
        }
        catch (Exception e)
        {
            // throw out exception
        }
        return -1;
    }


    public static double fetchCurrencyPrice(String name)
    {
        try
        {
            double ret = lru(name + "currency");
            if(ret != -1)
            {
                return ret;
            }
            URL url =
                new URL(
                    "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20('"
                        + name
                        + "')&format=json&diagnostics=false&env=http://datatables.org/alltables.env");
            String out =
                new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")
                    .next();
            JSONObject a = new JSONObject(out);
            a = a.getJSONObject("query");
            a = a.getJSONObject("results");
            a = a.getJSONObject("rate");
            if (a.has("Rate"))
            {
                addStorage(new stored(a.getDouble("Rate"), name + "currency"));
                return a.getDouble("Rate");
            }
        }
        catch (Exception e)
        {
            // throw out exception
            e.printStackTrace();
        }
        return -1;
    }


    public static double fetchPriceStock(String ticker)
    {
        try
        {
            double ret = lru(ticker + "stocks");
            if(ret != -1)
            {
                return ret;
            }
            URL url =
                new URL(
                    "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20('"
                        + ticker
                        + "')&format=json&diagnostics=false&env=http://datatables.org/alltables.env");
            String out =
                new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")
                    .next();
            JSONObject a = new JSONObject(out);
            a = a.getJSONObject("query");
            a = a.getJSONObject("results");
            a = a.getJSONObject("quote");
            if (a.has("AskRealtime"))
            {
                addStorage(new stored(a.getDouble("AskRealtime"), ticker + "stocks"));
                return a.getDouble("AskRealtime");
            }
        }
        catch (Exception e)
        {
            // throw out exception
            e.printStackTrace();
        }
        return -1;
    }


    public static void main(String args[])
    {
        System.out.println(fetchCoin("btc"));
    }
}
