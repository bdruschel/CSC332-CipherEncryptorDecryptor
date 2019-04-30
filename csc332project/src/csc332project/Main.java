package csc332project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Brandon
 * 
 * CLASSICAL CIPHER DECRYPTOR & ENCRYPTOR
 * 
 * This program allows the user to encrypt and decrypt six classical ciphers.
 * The following ciphers are supported:
 *  - SUBSTITUTION CIPHERS
 *      ~ Caesar (Encrypt w/ key, Decrypt with Frequency Analysis or given key)
 *      ~ Atbash (Encrypt and Decrypt)
 *      ~ Polybius Square (Encrypt and Decrypt w/ key)
 *  - POLYALPHABETIC CIPHERS
 *      ~ Vigenère (Encrypt and Decrypt w/ key)
 *      ~ Alberti (Encrypt and Decrypt w/ initial shift, period, and periodic increment)
 *      ~ Trithemius (Encrypt and Decrypt)
 * 
 */

public class Main {
        
    public static global g = new global();
    public static ArrayList<String> dictionary = new ArrayList<>();
    
    static void trackLetterFreq(String c){
        // Traverse through the following alphabet array:
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for(int i = 0; i < alphabet.length; i++){
            int count = 0;
            for(int j = 0; j < c.length(); j++){
                // Compare each character in the string to the letter. If equal,
                // increment count by 1.
                if(c.charAt(j) == alphabet[i]){
                    count++;
                }
            }
            g.getLetterCount().put(alphabet[i], count);
        }
    }
    
    // Traverse through the dictionary file 'words.txt', storing each word into an array
    static void readDictionary(File file) throws FileNotFoundException, IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                dictionary.add(line);
            }
        }
    }
    
    // Search for a given word in the dictionary using the Binary Search algorithm.
    // Return the index of the word if found, else return -1.
    static int search(String x){ 
        String[] arr = new String[dictionary.size()];
        arr = dictionary.toArray(arr);
        int l = 0;
        int r = arr.length; 
        while (l <= r) { 
            int m = l + (r - l) / 2;
            int res = x.compareTo(arr[m]);
            // System.out.println("Comparing to...\t" + arr[m] + " = " + res); // Debug
            if (res == 0) // Check if x is present at mid 
                return m;
            if (res > 0) // If x greater, ignore left half 
                l = m + 1;
            else // If x is smaller, ignore right half
                r = m - 1; 
        }
        return -1; 
    } 
    
    // Simple method for testing the search method.
    static void testSearch(){
        String word = "onwards";
        System.out.println("Searching dictionary for '" + word + "'");
        int result = search(word); 
        if (result == -1) 
            System.out.println("Word not found!"); 
        else
            System.out.println("Word found at index " + result + "!"); 
    }
    
    // Print the contents of the 2D Array representing the Polybius Square
    static void printPolySquare(){
        String[][] square = g.getPolybiusSquare();
        System.out.println("    1   2   3   4   5");
        System.out.println("    - - - - - - - - -");
        for(int i = 0; i < square.length; i++){
            System.out.print(i+1 + " | ");
            for(int j = 0; j < square[i].length; j++){
                if(square[i][j].length() == 1)
                    System.out.print(square[i][j] + "   ");
                else
                    System.out.print(square[i][j] + "  ");
            }
            System.out.println("");
        }
    }
  

    public static void main(String[] args) throws IOException {        
        File f = new File("C:\\Users\\Brandon\\OneDrive\\School\\Semester 7\\CSC 332\\Programming Project\\Workspace\\csc332project\\src\\csc332project\\words.txt");
        readDictionary(f);
        g.readVigenereSquare();
        //g.printVigenereSquare(); // Debug
        
        Scanner in = new Scanner(System.in); //Read user input
         String keyword = "";
         int key = -1;
         
         System.out.println("##############################################\n"
                 +          "##  CLASSICAL CIPHER DECRYPTOR & ENCRYPTOR  ##\n"
                 +          "##  By Brandon Druschel                     ##\n"
                 +          "##############################################");
         
        // TEXT-BASED INTERFACE
        for(;;){
            System.out.println("\n---------------\n\n"
                    +          "OPTIONS:\n"
                    +          "'e' - Encrypt a plaintext\n"
                    +          "'d' - Decrypt a ciphertext\n"
                    +          "'q' - Quit program\n");
            System.out.print("Enter a command:\n > ");
            String input = in.nextLine();
            
            if(input.equals("q")){ // Quit
                System.out.println("Goodbye.");
                break;
            }

            
            else if(input.equals("e")){
                for(;;){
                    System.out.print("Enter your plaintext message ('!back' to return to main menu): ");
                    String plaintext = in.nextLine();
                    if(plaintext.equals("!back")){
                        break;
                    }
                    System.out.println("Which cipher would you like to encrypt with?\n"
                        +          "'c' - Caesar\n"
                        +          "'at' - Atbash\n"
                        +          "'p' - Polybius\n"
                        +          "'al' - Alberti\n"
                        +          "'t' - Trithemius\n"
                        +          "'v' - Vigenère \n");
                    System.out.print("Enter a command, or 'back' to leave encryption menu:\n > ");
                    input = in.nextLine().toLowerCase();
                    
                    if(input.equals("c")){
                        for(;;){
                            System.out.println("NOTE: Negative number = shift left, positive = shift right");
                            System.out.print("Enter key (shift): ");
                            input = in.nextLine();
                            try{
                                key = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }
                            String ciphertext = g.caesarEncrypt(plaintext, key);
                            System.out.println("");
                            System.out.println("Plaintext:  " + plaintext);
                            System.out.println("Ciphertext: " + ciphertext);
                            System.out.println("");
                            System.out.print("Use a different key? (y/n): ");
                            input = in.nextLine().toLowerCase();
                            if(input.equals("y")){
                                continue;
                            }
                            if(input.equals("n")){
                                System.out.println("Going back...\n");
                                break;
                            }
                        }
                    }
                    else if(input.equals("at")){
                        System.out.println("Encrypting message...");
                        String ciphertext = g.atbashEncrypt(plaintext);
                        System.out.println("Plaintext:  " + plaintext);
                        System.out.println("Ciphertext: " + ciphertext + "\n");
                    }
                    else if(input.equals("p")){
                        // Sample Key: haykvdtformbs(ij)znwguxqlecp
                        System.out.println("NOTE: The key must be 25 characters to fit a 5x5 grid. Because of this,\n" +
                                            "two letters must be combined into one. (i.e. \"(ij)\" in the key will treat\n" +
                                            "i and j as one letter)");
                        for(;;){
                            System.out.print("Enter key (Polybius Square): ");
                            keyword = in.nextLine();
                            if(keyword.length() < 28){
                                System.out.println("ERROR: Invalid key length... are you missing a letter or combined letter?");
                                continue;
                            }
                            else{
                                System.out.println("Forming Polybius Square: ");
                                g.makePolySquare(keyword);
                                printPolySquare();
                                System.out.println("\nEncrypting message...");
                                String ciphertext = g.polybiusEncrypt(keyword, plaintext);
                                System.out.println("Plaintext:  " + plaintext);
                                System.out.println("Ciphertext: " + ciphertext + "\n");
                                System.out.print("Use a different key? (y/n): ");
                                input = in.nextLine().toLowerCase();
                                if(input.equals("y")){
                                    continue;
                                }
                                if(input.equals("n")){
                                    System.out.println("Going back...\n");
                                    break;
                                }
                            }
                        }
                    }
                    else if(input.equals("al")){
                        for(;;){
                            System.out.print("Enter Initial Shift: ");
                            input = in.nextLine();
                            int iShift = -1;
                            int pIncrement = -1;
                            int pLength = -1;
                            try{
                                iShift = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }                        
                            System.out.print("Enter Periodic Increment: ");
                            input = in.nextLine();
                            try{
                                pIncrement = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }
                            System.out.print("Enter Period Length: ");
                            input = in.nextLine();
                            try{
                                pLength = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }
                            System.out.println("Encrypting...\n");
                            String ciphertext = g.albertiEncrypt(plaintext, iShift, pIncrement, pLength);
                            System.out.println("Plaintext:  " + plaintext);
                            System.out.println("Ciphertext: " + ciphertext + "\n");
                            System.out.print("Try different values? (y/n): ");
                            input = in.nextLine().toLowerCase();
                            if(input.equals("y")){
                                continue;
                            }
                            if(input.equals("n")){
                                System.out.println("Going back...\n");
                                break;
                            }
                        }
                    }
                    else if(input.equals("t")){
                        for(;;){
                            System.out.println("OPTIONS:\n"
                                +          "'asc' - Ascending Shift\n"
                                +          "'desc' - Descending Shift\n");
                            System.out.print("Enter a command: ");
                            input = in.nextLine();
                            if(input.equals("asc")){
                                System.out.println("Encrypting with ascending shift...\n");
                                String ciphertext = g.trithemiusEncrypt(plaintext, true);
                                System.out.println("Plaintext:  " + plaintext);
                                System.out.println("Ciphertext: " + ciphertext + "\n");
                                System.out.print("Try different shift? (y/n): ");
                                input = in.nextLine().toLowerCase();
                                if(input.equals("y")){
                                    continue;
                                }
                                if(input.equals("n")){
                                    System.out.println("Going back...\n");
                                    break;
                                }
                            }
                            else if(input.equals("desc")){
                                System.out.println("Encrypting with descending shift...\n");
                                String ciphertext = g.trithemiusEncrypt(plaintext, false);
                                System.out.println("Plaintext:  " + plaintext);
                                System.out.println("Ciphertext: " + ciphertext + "\n");
                                System.out.print("Try different shift? (y/n): ");
                                input = in.nextLine().toLowerCase();
                                if(input.equals("y")){
                                    continue;
                                }
                                if(input.equals("n")){
                                    System.out.println("Going back...\n");
                                    break;
                                }
                            }
                            else{
                                System.out.println("Invalid command: " + "'" + input + "'");
                                continue;
                            }
                        }
                    }
                    else if(input.equals("v")){
                        for(;;){
                            System.out.print("Enter Keyphrase: ");
                            keyword = in.nextLine();
                            System.out.println("Encrypting...");
                            String ciphertext = g.vigenereEncrypt(keyword, plaintext);
                            System.out.println("Plaintext:  " + plaintext);
                            System.out.println("Ciphertext: " + ciphertext + "\n");
                            System.out.print("Try different keyphrase? (y/n): ");
                            input = in.nextLine().toLowerCase();
                            if(input.equals("y")){
                                continue;
                            }
                            if(input.equals("n")){
                                System.out.println("Going back...\n");
                                break;
                            }
                        }
                    }
                    else if(input.equals("back"))
                        break;
                    else
                        System.out.println("Invalid command: " + "'" + input + "'");
                }
            }
            
            
            else if(input.equals("d")){
                for(;;){
                    System.out.print("Enter the ciphertext ('!back' to return to main menu): ");
                    String ciphertext = in.nextLine();
                    if(ciphertext.equals("!back")){
                        break;
                    }
                    System.out.println("Which cipher would you like to decrypt with?\n"
                        +          "'c' - Caesar\n"
                        +          "'at' - Atbash\n"
                        +          "'p' - Polybius\n"
                        +          "'al' - Alberti\n"
                        +          "'t' - Trithemius\n"
                        +          "'v' - Vigenère \n");
                    System.out.print("Enter a command, or 'back' to leave decryption menu:\n > ");
                    input = in.nextLine().toLowerCase();
                    
                    if(input.equals("c")){
                        for(;;){
                            System.out.println("OPTIONS:\n"
                                    +          "'key' - decrypt using a given key\n"
                                    +          "'keyless' - decrypt using frequency analysis\n");
                            System.out.print("Enter decryption method (or 'back' to return to decryption menu): ");
                            input = in.nextLine();
                            
                            if(input.equals("key")){
                                for(;;){
                                    System.out.print("Enter key (shift): ");
                                    input = in.nextLine();
                                    try{
                                        key = Integer.parseInt(input);
                                    } catch(NumberFormatException | NullPointerException nfe) {
                                        System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                        continue;
                                    }
                                    System.out.println("KEY: " + key);
                                    String plaintext = g.caesarDecryptWithKey(ciphertext, key);
                                    System.out.println("Ciphertext: " + ciphertext);
                                    System.out.println("Plaintext:  " + plaintext + "\n");
                                     System.out.print("Use a different key? (y/n): ");
                                    input = in.nextLine().toLowerCase();
                                    if(input.equals("y")){
                                        continue;
                                    }
                                    if(input.equals("n")){
                                        System.out.println("Going back...\n");
                                        break;
                                    }
                                }
                            }
                            
                            else if(input.equals("keyless")){
                                System.out.println("Analyzing cipher...");
                                trackLetterFreq(ciphertext);
                                char common = g.mostCommonLetter();
                                System.out.println("Most common letter: " + common);
                                String plaintext = g.caesarDecrypt(ciphertext, common, dictionary);
                                System.out.println("Ciphertext: " + ciphertext);
                                System.out.println("Plaintext:  " + plaintext + "\n");
                            }
                            else if(input.equals("back"))
                                break;
                            else
                                System.out.println("Invalid command: " + "'" + input + "'");
                        }
                    }
                    else if(input.equals("at")){
                        System.out.println("Decrypting...");
                        String plaintext = g.atbashDecrypt(ciphertext);
                        System.out.println("Ciphertext: " + ciphertext);
                        System.out.println("Plaintext:  " + plaintext + "\n");
                    }
                    else if(input.equals("p")){
                        // Sample Key: haykvdtformbs(ij)znwguxqlecp
                        System.out.println("NOTE: The key must be 25 characters to fit a 5x5 grid. Because of this,\n" +
                                            "two letters must be combined into one. (i.e. \"(ij)\" in the key will treat\n" +
                                            "i and j as one letter)");
                        for(;;){
                            System.out.print("Enter key (Polybius Square): ");
                            keyword = in.nextLine();
                            if(keyword.length() < 28){
                                System.out.println("ERROR: Invalid key length... are you missing a letter or combined letter?");
                                continue;
                            }
                            else{
                                System.out.println("Forming Polybius Square: ");
                                g.makePolySquare(keyword);
                                printPolySquare();
                                System.out.println("Decrypting...");
                                String plaintext = g.polybiusDecrypt(keyword, ciphertext);
                                System.out.println("Ciphertext: " + ciphertext);
                                System.out.println("Plaintext:  " + plaintext + "\n");
                                System.out.print("Use a different key? (y/n): ");
                                input = in.nextLine().toLowerCase();
                                if(input.equals("y")){
                                    continue;
                                }
                                if(input.equals("n")){
                                    System.out.println("Going back...\n");
                                    break;
                                }
                            }
                        }
                    }
                    else if(input.equals("al")){
                        for(;;){
                            System.out.print("Enter Initial Shift: ");
                            input = in.nextLine();
                            int iShift = -1;
                            int pIncrement = -1;
                            int pLength = -1;
                            try{
                                iShift = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }                        
                            System.out.print("Enter Periodic Increment: ");
                            input = in.nextLine();
                            try{
                                pIncrement = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }
                            System.out.print("Enter Period Length: ");
                            input = in.nextLine();
                            try{
                                pLength = Integer.parseInt(input);
                            } catch(NumberFormatException | NullPointerException nfe) {
                                System.out.println("ERROR: " + "'" + input + "'" + " is not a valid number!");
                                continue;
                            }
                            System.out.println("Decrypting...\n");
                            String plaintext = g.albertiDecrypt(ciphertext, iShift, pIncrement, pLength);
                            System.out.println("Ciphertext:  " + ciphertext);
                            System.out.println("Plaintext:   " + plaintext + "\n");
                            System.out.print("Try different values? (y/n): ");
                            input = in.nextLine().toLowerCase();
                            if(input.equals("y")){
                                continue;
                            }
                            if(input.equals("n")){
                                System.out.println("Going back...\n");
                                break;
                            }
                        }
                    }
                    else if(input.equals("t")){
                        for(;;){
                            System.out.println("OPTIONS:\n"
                                +          "'asc' - Ascending Shift\n"
                                +          "'desc' - Descending Shift\n");
                            System.out.print("Enter a command: ");
                            input = in.nextLine();
                            if(input.equals("asc")){
                                System.out.println("Decrypting from ascending shift...\n");
                                String plaintext = g.trithemiusDecrypt(ciphertext, true);
                                System.out.println("Ciphertext: " + ciphertext);
                                System.out.println("Plaintext:  " + plaintext + "\n");
                                System.out.print("Try different shift? (y/n): ");
                                input = in.nextLine().toLowerCase();
                                if(input.equals("y")){
                                    continue;
                                }
                                if(input.equals("n")){
                                    System.out.println("Going back...\n");
                                    break;
                                }
                            }
                            else if(input.equals("desc")){
                                System.out.println("Decrypting from descending shift...\n");
                                String plaintext = g.trithemiusDecrypt(ciphertext, false);
                                System.out.println("Ciphertext: " + ciphertext);
                                System.out.println("Plaintext:  " + plaintext + "\n");
                                System.out.print("Try different shift? (y/n): ");
                                input = in.nextLine().toLowerCase();
                                if(input.equals("y")){
                                    continue;
                                }
                                if(input.equals("n")){
                                    System.out.println("Going back...\n");
                                    break;
                                }
                            }
                            else{
                                System.out.println("Invalid command: " + "'" + input + "'");
                                continue;
                            }
                        }
                    }
                    else if(input.equals("v")){
                        for(;;){
                            System.out.print("Enter Keyphrase: ");
                            keyword = in.nextLine();
                            System.out.println("Decrypting...");
                            String plaintext = g.vigenereDecrypt(keyword, ciphertext);
                            System.out.println("Ciphertext: " + ciphertext);
                            System.out.println("Plaintext:  " + plaintext + "\n");
                            System.out.print("Try different keyphrase? (y/n): ");
                            input = in.nextLine().toLowerCase();
                            if(input.equals("y")){
                                continue;
                            }
                            if(input.equals("n")){
                                System.out.println("Going back...\n");
                                break;
                            }
                        }
                    }
                    else if(input.equals("back"))
                        break;
                    else
                        System.out.println("Invalid command: " + "'" + input + "'");
                }
            }
            else
                System.out.println("Invalid command -- " + input);
        }

    /**
     * THE FOLLOWING CODE WAS USED FOR TESTING EACH METHOD AND SERVES NO OTHER PURPOSE
     */

//    // Frequency Analysis Test
//        String cipher = "ZHOFRPH WR JUDYLWB IDOOV";
//        System.out.println("Analyzing ciphertext: " + cipher);
//        trackLetterFreq(cipher);
//        g.printLetterCount();
//        char common = g.mostCommonLetter();
//        System.out.println("Most common letter: " + common);
        
//    // Caesar Test
        // Test Decryption 
//        System.out.println("Attempting Caesar Decryption...");
//        String decryption = g.caesarDecrypt(cipher, common, dictionary);
//        System.out.println(decryption);
//        System.out.println("");
//        
//        // Test Encryption 
//        System.out.println("Testing Caesar encryption...");
//        String plaintext = "PERSONALLY I PREFER THE AIR";
//        int key = 6; // Shift right 6
//        String encryption = g.caesarEncrypt(plaintext, key);
//        System.out.println(encryption);
//        
//        System.out.println("Analyzing encrypted text...");
//        trackLetterFreq(encryption);
//        g.printLetterCount();
//        
//        common = g.mostCommonLetter();
//        System.out.println("Most common letter: " + common);
//        
//        System.out.println("Attempting Caesar Decryption...");
//        decryption = g.caesarDecrypt(encryption, common, dictionary);
//        System.out.println(decryption);
//        
//    // Atbash test
//        String atbashP = "PAPER JAM DIPPER SAYS AUUGHWXQHGADSADUH";
//        System.out.println("Encrypting message using Atbash: " + atbashP);
//        
//        // Test Encryption
//        String atbashE = g.atbashEncrypt(atbashP);
//        System.out.println("Result: " + atbashE);
//        
//        // Test Decryption
//        System.out.println("- - - - - -\nDecrypting the encryption...");
//        String atbashD = g.atbashDecrypt(atbashE);
//        System.out.println(atbashD);
        
    /**
     * NOTE:
     * THE POLYBIUS SQUARE IS CONSTRUCTED LEFT TO RIGHT ACROSS EACH ROW WHEN
     * READING FROM THE KEY. OTHERWISE, COORDINATES WILL BE REVERSED WHEN
     * DECRYPTING!
     * 
     * THE KEY MUST BE 25 CHARACTERS LONG TO FIT A 5x5 GRID. BECAUSE OF THIS,
     * TWO LETTERS MUST BE COMBINED INTO ONE. (i.e. "(ij)" in the key will treat
     * i and j as one letter)
     */
    
    // Polybius Square test
//        String key = "haykvdtformbs(ij)znwguxqlecp";
//        String plaintext = "hello world";
//
//        // Test makeSquare()
//        g.makePolySquare(key);
//        String[][] square = g.getPolybiusSquare();
//        printPolySquare();
//
//        // Test Encryption
//        System.out.println(g.polybiusEncrypt(key, plaintext));
//
//        // Test Decryption
//        String ciphertext = "34443322 12 22533322";
//        System.out.println(g.polybiusDecrypt(key, ciphertext));
       
    // Vigenere Cipher Test
//        String key = "BAMF";
//        String plaintext = "brandon is cool";
//        
//        // Test Encryption
//        String ciphertext = g.vigenereEncrypt(key, plaintext);
//        System.out.println("Keyword:    " + g.keywordString(key, plaintext));
//        System.out.println("Plaintext:  " + plaintext);
//        System.out.println("Ciphertext: " + ciphertext);
        
          // Test Decryption
//        key = "SCHMENDRICK";
//        ciphertext = "S UPYTYH DIP GAVO QETHI MCBK OHK XEXJB VRW YOUWCHIA VRSV OQ LRDIA";
//        System.out.println("Keyword:    " + g.keywordString(key, ciphertext));
//        System.out.println("Ciphertext: " + ciphertext);
//        System.out.println("Decryption: " + g.vigenereDecrypt(key, ciphertext));


    /**
     * NOTE:
     * THIS ALBERTI CIPHER USES THE ENGLISH ALPHABET FOR BOTH OF ITS "RINGS".
     */
    
    // Alberti Cipher Test
//        String plaintext = "HELLO WORLD";
//        String ciphertext = "IFMORZTWQK";
//
//        int iShift = 1; // Initial Shift
//        int pIncrement = 2; // Periodic Increment
//        int period = 3;
        
        // Test Encryption
//        System.out.println("Plaintext:  " + plaintext);
//        System.out.println("Ciphertext: " + g.albertiEncrypt(plaintext, iShift, pIncrement, period));
        
        // Test Decryption
//        System.out.println("Ciphertext: " + ciphertext);
//        System.out.println("Plaintext:  " + g.albertiDecrypt(ciphertext, iShift, pIncrement, period));

    // Trithemius Cipher Test
//        String plaintext =  "Hello World";
//        String ciphertextA = "TSKWLJSPCB";  // TRITHEMIUS
//        String ciphertextD = "HDJIK RIKDU"; // HELLO WORLD
        
        // Test Encryption
//        System.out.println("Plaintext:               " + plaintext);
//        System.out.println("Ciphertext (Ascending):  " + g.trithemiusEncrypt(plaintext, true));
//        System.out.println("Ciphertext (Descending): " + g.trithemiusEncrypt(plaintext, false));
        
        // Test Decryption
//        System.out.println("Ciphertext (Ascending): " + ciphertextA);
//        System.out.println("Ciphertext (Decending): " + ciphertextD);
//        System.out.println("");
//        System.out.println("Plaintext (Ascending):  " + g.trithemiusDecrypt(ciphertextA, true));
//        System.out.println("Plaintext (Descending): " + g.trithemiusDecrypt(ciphertextD, false));

    // Combined Cipher Test
//        String plaintext =  "HELLO WORLD";
//        String vKey = "SUPDAWG";
//        String caesarEncrypt = g.caesarEncrypt(plaintext, 3);
//        String vigEncrypt = g.vigenereEncrypt(vKey, plaintext);
//                
//        System.out.println("Plaintext: " + plaintext);
//        System.out.println("Caesar:    " + caesarEncrypt);
//        System.out.println("Vigenere:  " + vigEncrypt);
//        
//        String combinedE = g.vigenereEncrypt(vKey, caesarEncrypt);
//        System.out.println("Combined Encryption:  " + combinedE);
//        
//        String combinedD = g.caesarDecryptWithKey(g.vigenereDecrypt(vKey, combinedE), 3);
//        System.out.println("Combined Decryption:  " + combinedD);
    }
}
