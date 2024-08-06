import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RomReader {

    public int[] readRom(String path) {
        int[] readByte = null;
        try {
            File rom = new File(path);
            FileInputStream fis = new FileInputStream(rom);
            int romLength = (int) rom.length();
            readByte = new int[romLength];
            for (int i = 0; i < rom.length(); i++) {
                readByte[i] = fis.read();
            }
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File not found beacause " + e);
        } catch (IOException e) {
            System.err.println("ERROR: Could not read file beacuse " + e);
        }
        return readByte;
    }
}
