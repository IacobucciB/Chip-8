public class Chip {

    private final int COL = 64;
    private final int ROW = 32;
    private int[][] display = new int[COL][ROW];
    private byte[] memory = new byte[4096];
    private byte[] V = new byte[16];
    private int programCounter = 0x200;
    private int indexRegister;
    private int[] stack = new int[16];
    private int stackPointer = 0;
    private boolean drawFlag = false;

    private static final byte[] FONT_SET = new byte[] {
            // 0x00 - 0x0F
            (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xF0, // 0
            (byte) 0x20, (byte) 0x60, (byte) 0x20, (byte) 0x20, (byte) 0x70, // 1
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // 2
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 3
            (byte) 0x90, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0x10, // 4
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 5
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 6
            (byte) 0xF0, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x40, // 7
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 8
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 9
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90, // A
            (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0, // B
            (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0, // C
            (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0, // D
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // E
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80 // F
    };

    public Chip(int[] readBytes) {
        // Load font data into memory
        for (int i = 0; i < FONT_SET.length; i++) {
            memory[i] = FONT_SET[i];
        }

        // Load the rest of the ROM
        for (int i = 0; i < readBytes.length; i++) {
            memory[0x200 + i] = (byte) readBytes[i];
        }
    }

    // 00E0 - Clears the screen
     public void clearScreen() {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                display[i][j] = 0;
            }
        }
    }

    // 1NNN - Jumps to Address NNN
    private void jumpAdd(int address) {
        programCounter = address & 0xFFF;
    }

    // 00EE - Returns from a subroutine
    private void returnSub() {
        if (stackPointer > 0) {
            programCounter = stack[--stackPointer];
        }
    }

    // 2NNN - Calls subroutine at NNN
    private void callSub(int address) {
        if (stackPointer < stack.length) {
            stack[stackPointer++] = programCounter;
            programCounter = address & 0xFFF;
        }
    }

    // 3XNN - Skip Inst. if equal NN
    private void beqNN(int x, int NN) {
        if (V[x] == (byte) NN) {
            programCounter += 2;
        }
    }

    // 4XNN - Skip Inst. if not equal NN
    private void bneNN(int x, int NN) {
        if (V[x] != (byte) NN) {
            programCounter += 2;
        }
    }

    // 5XY0 - Skip Inst. if Vx = Vy
    private void beqVxVy(int x, int y) {
        if (V[x] == V[y]) {
            programCounter = programCounter + 2;
        }
    }

    // 9XY0 - Skip Inst. if Vx != Vy
    private void bnqVxVy(int x, int y) {
        if (V[x] != V[y]) {

        }
    }

    // 6XNN - Set Vx to NN
    private void setVxNN(int x, int value) {
        V[x] = (byte) (value & 0xFF);
    }

    // 7XNN - Add NN to Vx
    private void addNNVx(int x, int value) {
        V[x] = (byte) ((V[x] + value) & 0xFF);
    }

    // ANNN - Set Index to NNN
    private void setIndexRegister(int address) {
        indexRegister = address & 0xFFF;
    }

    // 0xDXYN - Draw sprites
    public void drawSprites(int x, int y, int N) {
        int vx = V[x] & 0xFF;
        int vy = V[y] & 0xFF;
        V[0xF] = 0;
        for (int i = 0; i < N; i++) {
            int sprite = memory[indexRegister + i];
            for (int j = 0; j < 8; j++) {
                int pixel = (sprite >> (7 - j)) & 1;
                if (pixel == 1) {
                    int displayX = (vx + j) % COL;
                    int displayY = (vy + i) % ROW;
                    if (display[displayX][displayY] == 1) {
                        V[0xF] = 1; // Set collision flag
                    }
                    display[displayX][displayY] ^= 1; // XOR pixel
                    drawFlag = true;
                }
            }
        }
    }

    public int[][] getDisplay() {
        return display;
    }

    public void init() {
        clearScreen();
        System.out.println("Chip 8 Started");
    }

    public void run() {

        if (programCounter < 0x200 || programCounter >= memory.length - 1) {
            System.err.println("Program counter out of bounds: " + programCounter);
        }

        // Fetch the opcode (2 bytes)
        int opcode = (memory[programCounter] << 8) | (memory[programCounter + 1] & 0xFF);
        programCounter += 2; // Move to the next instruction

        // Decode and execute the opcode
        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode & 0x00FF) {
                    case 0x00E0: // 00E0 - Clear screen (not implemented)
                        clearScreen();
                        break;
                    case 0x00EE: // 00EE - Return from subroutine
                        returnSub();
                        break;
                }
                break;
            case 0x1000: // 1NNN - Jump to address NNN
                jumpAdd(opcode & 0x0FFF);
                break;
            case 0x2000: // 2NNN - Call subroutine at NNN
                callSub(opcode & 0x0FFF);
                break;
            case 0x3000: // 3XNN - Skip next instruction if Vx == NN
                beqNN((opcode & 0x0F00) >> 8, opcode & 0x00FF);
                break;
            case 0x4000: // 4XNN - Skip next instruction if Vx != NN
                bneNN((opcode & 0x0F00) >> 8, opcode & 0x00FF);
                break;
            case 0x5000: // 5XY0 - Skip next instruction if Vx == Vy
                if ((opcode & 0x000F) == 0x0000) {
                    beqVxVy((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4);
                }
                break;
            case 0x6000: // 6XNN - Set Vx to NN
                setVxNN((opcode & 0x0F00) >> 8, opcode & 0x00FF);
                break;
            case 0x7000: // 7XNN - Add NN to Vx
                addNNVx((opcode & 0x0F00) >> 8, opcode & 0x00FF);
                break;
            case 0x9000: // 9XY0 - Skip next instruction if Vx != Vy
                if ((opcode & 0x000F) == 0x0000) {
                    bnqVxVy((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4);
                }
                break;
            case 0xA000: // ANNN - Set index register to NNN
                setIndexRegister(opcode & 0x0FFF);
                break;
            case 0xD000: // DXYN - Display/draw sprites
                drawSprites((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4, opcode & 0x000F);
                break;
            // Other cases can be added here
        }

    }

    public boolean draw() {
        return drawFlag;
    }

    public void finished() {
        drawFlag = false;
    }

    public void printDisplay() {
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                System.out.print(display[x][y] == 0 ? "." : "#");
            }
            System.out.println();
        }
    }
}
