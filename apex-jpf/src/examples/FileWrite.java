import java.io.*;

class FileWrite {
        public static void writeToFile(String text) {
                try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                                        "C:/Users/Saeed Iqbal/Desktop/Data/test/new.txt"), true));
                        bw.write(text);
                        bw.newLine();
                        bw.close();
                } catch (Exception e) {
                }
        }

        public static void main(String[] args) throws Exception {
                String st1 = "Hello";
                String st2 = "Where there is will, there is a way";

                writeToFile(st1);
                writeToFile(st2);

                System.out.println("File created successfully!");
        }
}