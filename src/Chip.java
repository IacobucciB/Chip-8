public class Chip {
    
    private final int COL = 64;
    private final int ROW = 32;

    private int[][] matrix = new int[COL][ROW];

//    private byte[] memory = new byte[4096];
//    private byte[] registers = new byte[16];

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Chip() {
        this.flag = false;
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
