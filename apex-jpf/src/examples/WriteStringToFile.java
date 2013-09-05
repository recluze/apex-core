import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @web http://java-buddy.blogspot.com/
 */
public class WriteStringToFile {
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileWriter fileWriter = null;
        try {
            String content = "Hello! Java-Buddy :)" +
            		" saeed IQbal" +
            		"saeed kkjjkj";
            File newTextFile = new File("C:/Users/Saeed Iqbal/Desktop/Data/test/test.txt");
            fileWriter = new FileWriter(newTextFile);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteStringToFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(WriteStringToFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}