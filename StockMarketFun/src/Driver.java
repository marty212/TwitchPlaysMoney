import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;


public class Driver
{
    private ArrayList<String> lastErrors;

    private class Obs implements Observer
    {

        @Override
        public void update(Observable o, Object arg)
        {
            window.Update(mod.toString(), "");
        }

    }

    Model mod;
    Window window;
    public Driver()
    {
        SQL.init();
        SQL.addString("Starting program");
        TwitterSupport.init();
        mod = new Model();
        window = new Window();
        Parser.mod = mod;
        Thread thread = new Thread(new IRC());
        thread.start();
        mod.addObserver(new Obs());
        window.Update(mod.toString(), "");
    }

    public static void main(String args[])
    {
        Driver drive = new Driver();
    }
}
