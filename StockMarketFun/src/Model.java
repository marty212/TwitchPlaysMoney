import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Observable;
import java.util.Set;
import java.util.HashMap;


public class Model extends Observable
{
    public final static double INITMONEY = 100000;
    private HashMap<String, Asset> map;

    private class LoopTask extends TimerTask {
        public void run() {
            Set<String> keys = map.keySet();
            for(String s : keys)
            {
                Asset a = map.get(s);
                if(a instanceof Stock)
                {
                    a.USDValue = Fetcher.fetchPriceStock(a.name);
                }
                else if(a instanceof Currency)
                {
                    a.USDValue = Fetcher.fetchCurrencyPrice(a.name + "usd");
                }
                else if(a instanceof Coin)
                {
                    a.USDValue = Fetcher.fetchCoin(a.name);
                }
            }
            SQL.getMessage();
            setChanged();
            notifyObservers();
        }
    }
    private class Loop extends TimerTask {
        public void run() {
            TwitterSupport.update();
        }
    }


    public Model()
    {
        map = new HashMap<>();
        map.put("usd : " + Currency.class, new Currency("usd", INITMONEY));
        long delay = 5*1000; //changed!
        LoopTask task = new LoopTask();
        Timer timer = new Timer("TaskName");
        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date(); // no params = now
        timer.scheduleAtFixedRate(task, executionDate, delay);
        Loop task2 = new Loop();
        Timer timer2 = new Timer("Loop");
        timer2.cancel();
        timer2 = new Timer("Loop");
        Date executionDate2 = new Date(); // no params = now
        timer2.scheduleAtFixedRate(task2, executionDate2, 60 * 4 * 1000);
    }

    public Asset fetchAsset(String name, String type)
    {
        return map.get(name + " : " + type);
    }

    private boolean valid(int amount, double cost, Asset money)
    {
        return money.value - amount * cost >= 0;
    }

    public String buy(String nameOfAsset, int amount, String type)
    {
        Double price = -1.0;
        String className = null;
        Asset asset = null;
        switch(type)
        {
            case "Stock" :
                price = Fetcher.fetchPriceStock(nameOfAsset);
                className = Stock.class.toString();
                asset = new Stock(nameOfAsset, amount, price);
                break;
            case "Currency" :
                price = Fetcher.fetchCurrencyPrice(nameOfAsset + "usd");
                className = Currency.class.toString();
                asset = new Currency(nameOfAsset, amount);
                break;
            case "CryptoCoin" :
                price = Fetcher.fetchCoin(nameOfAsset);
                className = Coin.class.toString();
                asset = new Coin(nameOfAsset, amount, price);
                break;
        }
        if(price != -1 && price != 0)
        {
            Asset usd = fetchAsset("usd", Currency.class.toString());
            if(!valid(amount, price, usd))
            {
                return "Transaction Error: Not enough money for " + nameOfAsset + " at cost of " + price;
            }
            if(map.containsKey(nameOfAsset + " : " + className))
            {
                map.get(nameOfAsset + " : " + className).combine(asset);
            }
            else
            {
                map.put(nameOfAsset + " : " + className, asset);
            }
            usd.value -= price * amount;
            setChanged();
            notifyObservers();
            return null;
        }
        return "Transaction Error: The symbol " + nameOfAsset + " was not found on the market. :(";
    }

    public String sell(String nameOfAsset, int amount, String type)
    {
        Double price = -1.0;
        String className = null;
        switch(type)
        {
            case "Stock" :
                price = Fetcher.fetchPriceStock(nameOfAsset);
                className = Stock.class.toString();
                break;
            case "Currency" :
                price = Fetcher.fetchCurrencyPrice(nameOfAsset + "usd");
                className = Currency.class.toString();
                break;
            case "CryptoCoin" :
                price = Fetcher.fetchCoin(nameOfAsset);
                className = Coin.class.toString();
                break;
        }
        if(price != -1)
        {
            Asset usd = fetchAsset("usd", Currency.class.toString());
            if(map.containsKey(nameOfAsset + " : " + className))
            {
                Asset asset = map.remove(nameOfAsset + " : " + className);
                if(asset.value < amount)
                {
                    return "Transaction Error: Can't sell that much of " + nameOfAsset;
                }
                asset.value -= amount;
                if(asset.value > 0)
                {
                    map.put(nameOfAsset + " : " + className, asset);
                }
            }
            else
            {
                return "Transaction Error: We do not have any of " + nameOfAsset;
            }
            usd.value += price * amount;
            setChanged();
            notifyObservers();
            return null;
        }
        return "Transaction Error: The symbol " + nameOfAsset + " was not found on the market. :(";
    }

    public String toString()
    {
        Set<String> keys = map.keySet();
        StringBuilder ret = new StringBuilder();
        //ret.append("<HTML><P>");
        for(String str : keys)
        {
            ret.append(str.replaceAll("class ", ""));
            ret.append(" : ");
            Asset a = map.get(str);
            ret.append(a.getUSDValue());
            ret.append(" : ");
            ret.append(a.getValue());
            ret.append("\r\n");
        }
        //ret.append("</P></HTML>");
        return ret.toString();
    }

    public static void main(String args[])
    {
        Model mod = new Model();
        mod.buy("aapl", 1, "Stock");
        System.out.println(mod.sell("goog", 1, "Stock"));
        System.out.println(mod.toString());
        mod.sell("aapl", 1, "Stock");
        System.out.println(mod.toString());
    }
}
