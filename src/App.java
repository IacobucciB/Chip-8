import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class App {

    public static void showGUI() {

        JFrame frame = new JFrame("CHIP 8");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Chip 8 Emulator written in Java");
        frame.getContentPane().add(label);

        frame.setSize(1024,512);
        frame.setVisible(true);

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Chip 8 Emulator");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGUI();
            }
        });
    }
}
