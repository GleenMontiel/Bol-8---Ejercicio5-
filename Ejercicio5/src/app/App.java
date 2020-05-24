package app;
import javax.swing.JFrame;
public class App {
    public static void main(String[] args) throws Exception {
        FrmPrincipal frame = new FrmPrincipal();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(250, 345);

        frame.setVisible(true);
    }
}
