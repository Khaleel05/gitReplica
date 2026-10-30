import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Encrypt {
    public static String encryptThisString(String input){
        try{
            //getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            //digest() method is called
            //to calculate message digest of input string
            //returned as array of byte
            byte[] MessageDigest = md.digest(input.getBytes());

            //convert byte array into signum representation
            BigInteger no = new BigInteger(1, MessageDigest);

            //convert message digest into hex value
            String hashText = no.toString(16);

            // Add preceding 0s to make it 40 digits long
            while (hashText.length() < 40) {
                hashText = "0" + hashText;
            }

            //return hashText
            return hashText;
        }
        //for specifying wrong message digest algorithm.
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
}
