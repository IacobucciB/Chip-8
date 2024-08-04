public class Chip {
    
    private byte[] memory = new byte[4096];
    private byte[] registers = new byte[16];


    public byte[] getMemory() {
        return memory;
    }

    public void setMemory(byte[] memory) {
        this.memory = memory;
    }

    public byte[] getRegisters() {
        return registers;
    }

    public void setRegisters(byte[] registers) {
        this.registers = registers;
    }
}
