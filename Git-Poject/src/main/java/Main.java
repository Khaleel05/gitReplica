import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


public class Main {
  public static void main(String[] args) throws NoSuchAlgorithmException {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

    // TODO: Uncomment the code below to pass the first stage
    //
     final String command = args[0];

     switch (command) {
       case "init" -> {
         final File root = new File(".git");
         new File(root, "objects").mkdirs();
         new File(root, "refs").mkdirs();
         final File head = new File(root, "HEAD");

         try {
           head.createNewFile();
           Files.write(head.toPath(), "ref: refs/heads/main\n".getBytes());
           System.out.println("Initialized git directory");
         } catch (IOException e) {
           throw new RuntimeException(e);
         }
       }
       //reading blob object
       case "cat-file"->{
         String hash = args[2];
         String dirHash = hash.substring(0,2);
         String fileHash = hash.substring(2);
         File blobFile = new File("./.git/objects/" + dirHash + "/" + fileHash);
         try{
           String blob = new BufferedReader(new InputStreamReader(new InflaterInputStream(new FileInputStream(blobFile)))).readLine();
           String content = blob.substring(blob.indexOf("\0")+1);
           System.out.print(content);
         }catch(IOException e){
           throw new RuntimeException();
         }
       }
       //creating a blob object
       case "hash-object" -> {
         String filename = args[2];
         try{
           Path filePath = Paths.get(filename);
           String fileContent = Files.readString(filePath);
           long fileSize = Files.size(filePath);

           String header = "blob " + fileSize + "\0";
           String combinedData = header + fileContent;

           String hash = SHA1Encrypt.encryptThisString(combinedData);
           String blobPath = String.format(".git/blob/%s/%s",
                   hash.substring(0,2),
                   hash.substring(2));

           File blobFile = new File(blobPath);
           blobFile.getParentFile().mkdirs();

           try (DeflaterOutputStream out = new DeflaterOutputStream(new FileOutputStream(blobFile))){
             //write he combined data (header + file content)
             out.write(combinedData.getBytes());
           }

           System.out.println(hash);

         }catch(IOException e){
           throw new RuntimeException();
         }
       }
       default -> System.out.println("Unknown command: " + command);
     }
  }
}
