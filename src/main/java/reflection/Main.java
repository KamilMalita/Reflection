package reflection;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String... args) {
        String result = Reflection.getInfoForClass("java.lang.String", false);
        try {
            FileWriter fileWriter = new FileWriter("result.txt");
            fileWriter.write(result);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
