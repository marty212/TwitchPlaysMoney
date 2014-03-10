import java.util.Date;


public class Stock
    extends Asset
{

    public Stock(String str, double i, Double usdVal)
    {
        name = str;
        value = i;
        USDValue = usdVal;
        age = new Date();
    }

    @Override
    public void combine(Asset other)
    {
        if(other instanceof Stock)
        {
            Stock s = (Stock)other;
            if(s.name.equals(this.name))
            {
                this.value += s.value;
                s.value = 0;
            }
        }
    }

}
