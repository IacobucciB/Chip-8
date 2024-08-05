import java.io.File;

public class RomReader {

    public void readRom(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("File not foud");
            return;
        }
        System.out.println(path);

    }
}
