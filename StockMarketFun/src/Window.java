import java.awt.TextArea;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JFrame;


public class Window
    extends JFrame
{
    protected TextArea assets, trans;

    public Window()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,700);
        setResizable(false);
        trans = new TextArea("a");
        assets = new TextArea("b");
        setLayout(new FlowLayout());
        assets.setPreferredSize(new Dimension(950, 600));
        trans.setPreferredSize(new Dimension(950, 50));
        add(assets);
        add(trans);
        setVisible(true);
    }

    public void Update(String str1, String str2)
    {
        assets.setText(str1);
        trans.setText(str2);
    }
}
