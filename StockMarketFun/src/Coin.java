import java.util.Date;

public class Coin
    extends Asset
{

    public Coin(String str, double i, Double usdVal)
    {
        name = str;
        value = i;
        USDValue = usdVal;
        age = new Date();
    }


    @Override
    public void combine(Asset other)
    {
        if (other instanceof Coin)
        {
            Coin s = (Coin)other;
            if (s.name.equals(this.name))
            {
                this.value += s.value;
                s.value = 0;
            }
        }
    }

}
