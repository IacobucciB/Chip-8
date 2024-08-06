import javax.swing.JFrame;

public class App implements Runnable {

    private Chip chip;
    private GamePanel gamePanel;

    private final int FPS = 60;
    private final long FRAME_DURATION = 1000 / FPS;

    public App() {
        RomReader romReader = new RomReader();
        int[] readBytes = romReader.readRom("./roms/IBM Logo.ch8");
        chip = new Chip(readBytes);
        chip.init();
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("CHIP 8 Emulator");

        gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Chip 8 Emulator");
        App app = new App();
        Thread thread = new Thread(app);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            chip.run();
            gamePanel.update(chip.getMatrix());
            if (chip.draw()) {
                gamePanel.repaint();
                chip.finished();
            }
            try {
                Thread.sleep(FRAME_DURATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
