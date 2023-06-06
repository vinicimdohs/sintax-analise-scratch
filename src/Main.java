import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting...");

        String fileName = "test.pas";

        replaceTabWithSpaces(fileName);

        Sintatico sintatico = new Sintatico(fileName);

        sintatico.analyse();
    }

    public static void replaceTabWithSpaces(String fileName) {
        Path filePath = Paths.get(fileName);
        int spacesPerTab = 4;
        StringBuilder spacesBuilder = new StringBuilder();

        for (int i = 0; i < spacesPerTab; i++) {
            spacesBuilder.append(" ");
        }
        String spaces = spacesBuilder.toString();

        String fileContent;
        try {
            fileContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            fileContent = fileContent.replace("\t", spaces);
            Files.write(filePath, fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

}