# Website security

- Cross site scripting (XSS): inject JavaScript into your page in an attempt to trick the users into sending them personal data such as usernames and passwords.
- SQL injection attacks: allows attacker to run malicious SQL code on your database.
- Social engineering attack: tricks someone into revealing their username and password, often by posing as an IT employee of the company.

Cryptography is the process of taking readable data and changing it in some way so that it is no longer readable as the original text. For passwords, one-way hashing is used so that password can be hashed by it cannot be recovered from hash value. Reversible encryption is the one which can be reversed. It is used to transfer secure data over the Internet. Reversible encryption encrypts data against a key. The key can be used to decrypt the data at a later date when it needs to be read by a user.

md5: older one-way 128-bit algorithm. It is vulnerable to collisions means the same hash can be produced for two different passwords.
sha-1: 160-bit one-way hash algorithm.
sha-2: 224 to 512 bits hash sizes.
AES-128: reversible encryption with 128-bit key. Generally considered suitable for most encryption needs.
AES-256: 256-bit version of AES.

- Dictionary attacks: An attacker simply tries different passwords until they find one that works. This is done using an automated program and an electronic dictionary.
- Rainbow table attacks: It uses pre-computed lookup table that contains hashes for the words. This allows attacker who has access to the hashed passwords to crack them much more efficiently and quickly.

**Utility class for hashing and salting passwords**

```Java
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Base64;

public class PasswordUtil {
  public static String hashPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.reset();
    md.update(password.getBytes());
    byte[] mdArray = md.digest();
    StringBuilder sb = new StringBuilder(mdArray.length * 2);
    for(byte b: mdArray) {
      int v = b & 0xff;
      if(v < 16) {
        sb.append('0');
      }
      sb.append(Integer.toHexString(v));
    }
    return sb.toString();
  }

  public static getSalt() {
    Random r = new SecureRandom();
    byte[] saltBytes = new byte[32];
    r.nextBytes(saltBytes);
    return Base64.getEncoder().encodeToString(saltBytes);
  }

  public static String hashAndSaltPassword(String password)  throws NoSuchAlgorithmException {
    String salt = getSalt();
    return hashPassword(password + salt);
  }

  public static void main(String[] args) {
    try {
      System.out.println("Hash for 'sesame': " + hashPassword("sesame"));
      System.out.println("Random salt : " + getSalt());
      System.out.println("Salted hash for 'sesame': " + hashAndSaltPassword("sesame"));
    } catch (NoSuchAlgorithmException e) {
      System.out.println(e);
    }
  }
}
```

Java code to enforce password strength

```java
public static void checkPasswordStrength(String password) throws Exception {
  if(password == null || password.trim().isEmpty()) {
    throw new Exception("Password cannot be empty.");
  } else if (pass.length < 8) {
    throw new Exception("Password is too short. Must be at least 8 characters long.");
  }
}
