import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private final int tileSize = 16;

    private final int COL = 64;
    private final int ROW = 32;

    private final int WIDTH = tileSize * COL;
    private final int HEIGHT = tileSize * ROW;

       private int[][] matrix = new int[COL][ROW];
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }
    
    public void update(int[][] matrix) {
        this.matrix = matrix;
   }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMatrix(g);
        g.dispose();
    }

    private void paintMatrix(Graphics g) {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                if (matrix[i][j] == 1) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
            }
        }
    }

}
