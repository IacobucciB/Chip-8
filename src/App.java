import javax.swing.JFrame;

public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("Chip 8 Emulator");
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("CHIP 8 Emulator");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.startGameThread();
    }
}