import java.util.Date;


public class Currency extends Asset
{

    public Currency(String name, double value)
    {
        this.name = name;
        this.value = value;
        this.age = new Date();
        if(name.equals("usd"))
        {
            USDValue = 1;
        }
        else
        {
            USDValue = Fetcher.fetchCurrencyPrice(name + "usd");
        }
    }

    @Override
    public void combine(Asset other)
    {
        if(other instanceof Currency)
        {
            Currency s = (Currency)other;
            if(s.name.equals(this.name))
            {
                this.value += s.value;
                s.value = 0;
            }
        }
    }
}
