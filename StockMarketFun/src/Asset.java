import java.util.Date;


public abstract class Asset
{
    protected String name;
    protected double value;
    protected Date   age;
    protected double USDValue;

    public String getUSDValue()
    {
        return "$" + String.valueOf(USDValue * value);
    }

    public double getValue()
    {
        return value;
    }

    public abstract void combine(Asset other);
}
