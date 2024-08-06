public class Chip {

    private final int COL = 64;
    private final int ROW = 32;
//    private boolean flag;
    private int[][] matrix = new int[COL][ROW];

    public int[][] getMatrix() {
        return matrix;
    }

    private byte[] memory = new byte[4096];
//    private byte[] registers = new byte[16];

    public Chip(int[] readBytes) {
//        this.flag = false;
    }    

    public void init() {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                matrix[i][j] = 0;
            }
        }
        System.out.println("Chip 8 Started");
    }

    private int x = 0;
    private int y = 0;
    public void run() {

    }
    

    public boolean draw() {
        return true;
    }

    public void finished() {
        matrix[x][y] = 1;
        if (x < COL - 1) {
            x++;
        } else {
            x = 0;
            if (y < ROW - 1) {
                y++;
            } else {
                y = 0;
            }
        }
    }
    
}
