
public class Parser
{
    public static Model mod;

    public static void parse(String str)
    {
        try {
        String working[] = str.split("\\s");
        if(working.length < 3 || mod == null)
        {
            return;
        }
        String ret = "";
        if(working[0].toLowerCase().contains("buy"))
        {
            if(working[1].toLowerCase().contains("stock"))
            {
                ret = mod.buy(working[2], 1, "Stock");
            }
            else if(working[1].toLowerCase().contains("curr"))
            {
                ret = mod.buy(working[2], 1, "Currency");
            }
            else if(working[1].toLowerCase().contains("coin"))
            {
                ret = mod.buy(working[2], 1, "CryptoCoin");
            }
        }
        else if(working[0].toLowerCase().contains("sell"))
        {
            if(working[1].toLowerCase().contains("stock"))
            {
                ret = mod.sell(working[2], 1, "Stock");
            }
            else if(working[1].toLowerCase().contains("curr"))
            {
                ret = mod.sell(working[2], 1, "Currency");
            }
            else if(working[1].toLowerCase().contains("coin"))
            {
                ret = mod.sell(working[2], 1, "CryptoCoin");
            }
        }
        if(ret == null)
        {
            ret = "Transaction Successful : " + working[0] + " " + working[2] + " " + working[1] + " 1";
        }
        else if(ret.isEmpty())
        {
            return;
        }
        SQL.addString(ret);
        }
        catch(Exception e)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e1)
            {}
            parse(str);
        }
    }
}
