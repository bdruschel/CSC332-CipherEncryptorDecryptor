package csc332project;

import static csc332project.Main.search;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Brandon
 * 
 * The "global" class is used to contain variables that are intended to be used
 * globally -- throughout the entire package -- if need be.
 * 
 * It also contains the methods for encrypting and decrypting all six ciphers.
 * 
 */

public class global {
    // Array representing the alphabet. Used to iterate through the alphabet
    // (i.e. when accounting for a Caesar shift).
    private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    
    // Array representing the alphabet, except it's backwards. Used only for
    // Atbash encryption/decryption.
    private char[] reverse = "ZYXWVUTSRQPONMLKJIHGFEDCBA".toCharArray();
    
    // Array which lists frequently used letters in the English language from
    // most to least frequent. Useful for frequency analysis.
    private char[] freqLetters = "ETAOINSRHLDCUMFPGWYBVKXJQZ".toCharArray();
    
    // 2D array representing a Polybius Square (5x5).
    private String[][] polybiusSquare = new String[5][5];
    
     // 2D array representing a Vigenere Square (26x26). AKA Tabula Recta
    private String[][] vigenereSquare = new String[26][26];
    File vSquare = new File("C:\\Users\\Brandon\\OneDrive\\School\\Semester 7\\CSC 332\\Programming Project\\Workspace\\csc332project\\src\\csc332project\\vSquare.txt");
       
    // HashMap used for tracking the amount of times each letter in the alphabet
    // is used in a ciphertext. Useful for frequency analysis.
    private HashMap<Character,Integer> letterCount = new HashMap<>();
    
    public global() {
        for(int i = 0; i < alphabet.length; i++){
            letterCount.put(alphabet[i], 0);
        }
    }
    
    public global(HashMap h){
        this.letterCount = h;
    }
    
    public HashMap getLetterCount(){
        return letterCount;
    }
    
    public char[] getFreqLetters(){
        return freqLetters;
    }
    
    public String[][] getPolybiusSquare(){
        return polybiusSquare;
    }
    
    // Reads a txt file in which the first line is the alphabet, and each line
    // afterwards is the previous line with the alphabet shifted once to the
    // left, repeat 26 times to form a Vigenère Square.
    public void readVigenereSquare() throws FileNotFoundException, IOException{
        int j = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(vSquare))) {
            for(String line; (line = br.readLine()) != null; ) {
                for(int i = 0; i < line.length(); i++){
                    vigenereSquare[i][j] = String.valueOf(line.charAt(i));
                }
                j++;
            }
        }
    }
    
    // Print the vigenereSquare[][] Array -- just for testing.
    public void printVigenereSquare(){
        String[][] square = vigenereSquare;
        for(int i = 0; i < square.length; i++){
            for(int j = 0; j < square[i].length; j++){
                System.out.print(square[i][j] + "  ");
            }
            System.out.println("");
        }    
    }
    
    // Print the contents of the letterCount HashMap
    public void printLetterCount(){
        System.out.println("- - - - - - - -\nPRINTING LETTER COUNT:");
        for(Character c: letterCount.keySet()){
            String key = c.toString();
            String value = letterCount.get(c).toString();
            System.out.print("[" + key + " " + value + "] ");  
        }
        System.out.println("\n- - - - - - - -");
    }
    
    // Return the index of a character within the alphabet (i.e. 'A' is 0 and 'Z' is 25)
    // Used for ciphers that involve letter shifting. 
    public int alphabetIndex(char c){
        for(int i = 0; i < alphabet.length; i++){
            if(alphabet[i] == c){
                return i;
            }
        }
        return -1;
    }
        
    // Return the index of a character within the reversed alphabet. Used for
    // Atbash decryption.
    public int reverseIndex(char c){
        for(int i = 0; i < reverse.length; i++){
            if(reverse[i] == c){
                return i;
            }
        }
        return -1;
    }
    
    // Return the letter with the highest count in the letterCount HashMap.
    public char mostCommonLetter(){
        int max = 0;
        char let = 'z';
        for(Character c: letterCount.keySet()){
            if(max == 0 || letterCount.get(c) > max){
                max = letterCount.get(c);
                let = c;
            }
        }
        return let;
    }
    
    // Given two char inputs, return an int representing the shift required
    // to get from the first letter input to the other. (i.e. A->C = 2, and
    // C->A = -2.
    public int findShift(char common, char freq){
        int indexC = alphabetIndex(common);
        //System.out.println("Index of " + common + ": " + indexC); // Debug
        int indexF = alphabetIndex(freq);
        //System.out.println("Index of " + freq + ": " + indexF); // Debug
        if(indexC < indexF)
            return indexF - indexC;
        else
            return (indexC - indexF) * -1;
    }
    
    // Take each word within the given cipher String and search within the
    // dictionary. If no word within the sentence is found in the dictionary,
    // it is deemed an invalid decryption.
    // Used only for Caesar decryption.
    public boolean analyzeDictionary(String cipher, ArrayList<String> dictionary){
        String[] sentence = cipher.split("\\W+");
        for(int i = 0; i < sentence.length; i++){
            int result = search(sentence[i].toLowerCase());
            if(result != -1){
                return true;
            }
        }
        return false;
    }
    
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
    // Given the key String, fill the Polybius Square 2D array.
    public void makePolySquare(String key){
        for(int i = 0; i < polybiusSquare.length; i++){
            for(int j = 0; j < polybiusSquare[i].length; j++){
                if(key.substring(0,1).contains("(")){
                    polybiusSquare[i][j] = key.substring(1,3);
                    key = key.substring(4);
                }
                else{
                    polybiusSquare[i][j] = key.substring(0,1);
                    key = key.substring(1);
                }
            }
        }
    }
    
    // Create a string of the Vigenere keyword that matches the length of
    // the plaintext by repeatedly writing the keyword.
    // i.e. key = "BAMF", plaintext = "BRANDON IS COOL"
    // result: BRANDON IS COOL
    //         BAMFBAM FB AMFB
    public String keywordString(String key, String plaintext){
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for(int i = 0; i < plaintext.length(); i++){
            if(plaintext.charAt(i) != ' '){
                sb.append(key.charAt(j));
                if(j < key.length()-1)
                    j++;
                else
                    j = 0;
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public int getVRowCol(char c){
        for(int i = 0; i < alphabet.length; i++){
            if(alphabet[i] == Character.toUpperCase(c))
                return i;
        }
        return -1;
    }
        
    public String caesarDecrypt(String cipher, char common, ArrayList<String> dictionary){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < freqLetters.length; i++){
            char f = freqLetters[i];
           // System.out.println("Trying decryption: " + common + "->" + f);
            int shift = findShift(common, f);
            //System.out.println("Caesar shift: " + shift);
            for(int j = 0; j < cipher.length(); j++){
                int newIndex = alphabetIndex(cipher.charAt(j)) + shift;
                if(newIndex < 0){
                    newIndex = alphabet.length + newIndex;
                }
                if(newIndex > alphabet.length-1){
                    newIndex = newIndex - alphabet.length;
                }
                if(cipher.charAt(j) != ' '){
                    sb.append(alphabet[newIndex]);
                }
                else
                    sb.append(" ");
            }
            //System.out.println("Resulting decipher: " + sb.toString());
            if(analyzeDictionary(sb.toString(), dictionary)){
                Scanner in = new Scanner(System.in); //Read user input
                System.out.println("Dictionary word found.");
                System.out.println("Text: " + sb.toString());
                System.out.print("Continue? (y/n): ");
                String input = in.nextLine();
                if(input.equals("y")){
                    sb = new StringBuilder();
                    continue;
                }
                else if (input.equals("n")){
                    return sb.toString();
                }
            }
            else{
                //System.out.println("Decryption invalid, attempting next most common letter...");
                sb = new StringBuilder();
            }
        }
        System.out.println("DECRYPTION FAILURE: Returning null...");
        return null;
    }
    
    public String caesarDecryptWithKey(String cipher, int key){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cipher.length(); i++){
            int newIndex = alphabetIndex(cipher.charAt(i)) + key;
            if(newIndex < 0){
                newIndex = alphabet.length + newIndex;
            }
            if(newIndex > alphabet.length-1){
                newIndex = newIndex - alphabet.length;
            }
            if(cipher.charAt(i) != ' '){
                sb.append(alphabet[newIndex]);
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String caesarEncrypt(String plaintext, int key){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < plaintext.length(); i++){
            int newIndex = alphabetIndex(plaintext.charAt(i)) + key;
            if(newIndex < 0){
                newIndex = alphabet.length + newIndex;
            }
            if(newIndex > alphabet.length-1){
                newIndex = newIndex - alphabet.length;
            }
            if(plaintext.charAt(i) != ' '){
                sb.append(alphabet[newIndex]);
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String atbashEncrypt(String plaintext){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < plaintext.length(); i++){
            if(plaintext.charAt(i) != ' '){
                int index = alphabetIndex(plaintext.charAt(i));
                sb.append(reverse[index]);
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String atbashDecrypt(String cipher){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cipher.length(); i++){
            if(cipher.charAt(i) != ' '){
                int index = reverseIndex(cipher.charAt(i));
                sb.append(alphabet[index]);
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String polybiusEncrypt(String key, String plaintext){
        StringBuilder sb = new StringBuilder();
        String plain = plaintext.toLowerCase();
        makePolySquare(key);
        for(int x = 0; x < plain.length(); x++){
            for(int i = 0; i < polybiusSquare.length; i++){
                for(int j = 0; j < polybiusSquare[i].length; j++){
                    if(polybiusSquare[i][j].contains(String.valueOf(plain.charAt(x)))){
                        sb.append(i+1);
                        sb.append(j+1);
                    }
                }
            }
            if(x < plain.length()-1){
                if(plain.substring(x,x+1).equals(" "))
                    sb.append(" ");
            }
            else{
                if(plain.substring(x).equals(" "))
                    sb.append(" ");
            }
        }
        return sb.toString();
    }
    
    public String polybiusDecrypt(String key, String cipher){
        StringBuilder sb = new StringBuilder();
        makePolySquare(key);
        while(cipher.length() > 0){
            if(cipher.substring(0,1).equals(" ")){
                sb.append(" ");
                cipher = cipher.substring(1);
            }
            else{
                int x = Integer.parseInt(cipher.substring(0,1));
                int y = Integer.parseInt(cipher.substring(1,2));
                if(polybiusSquare[x-1][y-1].length() > 1){
                    sb.append(polybiusSquare[x-1][y-1].charAt(0));
                    //sb.append("[" + polybiusSquare[x-1][y-1].charAt(0) + "/" + polybiusSquare[x-1][y-1].charAt(1) + "]");
                    cipher = cipher.substring(2);
                }
                else{
                    sb.append(polybiusSquare[x-1][y-1]);
                    cipher = cipher.substring(2);
                }
            }
        }
        return sb.toString();
    }
    
    public String vigenereEncrypt(String key, String plaintext){
        StringBuilder sb = new StringBuilder();
        String keyword = keywordString(key, plaintext);
        for(int i = 0; i < plaintext.length(); i++){
            if(plaintext.charAt(i) != ' '){
                int x = getVRowCol(plaintext.charAt(i));
                int y = getVRowCol(keyword.charAt(i));
                sb.append(vigenereSquare[y][x]);
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String vigenereDecrypt(String key, String cipher){
        StringBuilder sb = new StringBuilder();
        String keyword = keywordString(key, cipher);
        for(int i = 0; i < cipher.length(); i++){
            if(cipher.charAt(i) != ' '){
                int x = getVRowCol(keyword.charAt(i));
                for(int j = 0; j < vigenereSquare.length; j++){
                    //System.out.println(vigenereSquare[j][x] + " = " + Character.toUpperCase(cipher.charAt(i)) + "?"); // Debug
                    if(vigenereSquare[j][x].equals(Character.toString(Character.toUpperCase(cipher.charAt(i)))))
                        sb.append(alphabet[j]);
                }
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String albertiEncrypt(String plaintext, int iShift, int pIncrement, int period){
        StringBuilder sb = new StringBuilder();
        int shift = iShift;
        int j = 0;
        for(int i = 0; i < plaintext.length(); i++){
            if(j < period){
                int newIndex = alphabetIndex(plaintext.charAt(i)) + shift;
                if(newIndex < 0)
                    newIndex = alphabet.length + newIndex;
                
                if(newIndex > alphabet.length-1)
                    newIndex = newIndex - alphabet.length;
                
                if(plaintext.charAt(i) != ' '){
                    sb.append(alphabet[newIndex]);
                    //System.out.println("Shift " + shift + ": " + plaintext.charAt(i) + "->" + alphabet[newIndex] + " (j=" + j + ")"); // Debug
                    j++;
                }
                // This encryption usually forgoes spaces in favor of security
            }
            else{
                shift += pIncrement;
                j = 0;
                int newIndex = alphabetIndex(plaintext.charAt(i)) + shift;
                if(newIndex < 0){
                    newIndex = alphabet.length + newIndex;
                }
                if(newIndex > alphabet.length-1){
                    newIndex = newIndex - alphabet.length;
                }
                if(plaintext.charAt(i) != ' '){
                    sb.append(alphabet[newIndex]);
                    //System.out.println("Shift " + shift + ": " + plaintext.charAt(i) + "->" + alphabet[newIndex] + " (j=" + j + ")"); // Debug
                    j++;
                }
                // This encryption usually forgoes spaces in favor of security
            }
        }
        return sb.toString();
    }
    
    // The decryption process is nearly identical to encryption, except the 'disk' 
    // spins counter-clockwise. In other words, the shift is simply negated.
    public String albertiDecrypt(String ciphertext, int iShift, int pIncrement, int period){
        StringBuilder sb = new StringBuilder();
        int shift = iShift * -1;
        int j = 0;
        for(int i = 0; i < ciphertext.length(); i++){
            if(j < period){
                int newIndex = alphabetIndex(ciphertext.charAt(i)) + shift;
                if(newIndex < 0)
                    newIndex = alphabet.length + newIndex;
                if(newIndex > alphabet.length-1)
                    newIndex = newIndex - alphabet.length;
                if(ciphertext.charAt(i) != ' '){
                    sb.append(alphabet[newIndex]);
                    j++;
                }
            }
            else{
                shift -= pIncrement;
                j = 0;
                int newIndex = alphabetIndex(ciphertext.charAt(i)) + shift;
                if(newIndex < 0)
                    newIndex = alphabet.length + newIndex;
                if(newIndex > alphabet.length-1)
                    newIndex = newIndex - alphabet.length;
                if(ciphertext.charAt(i) != ' '){
                    sb.append(alphabet[newIndex]);
                    j++;
                }
            }
        }
        return sb.toString();
    }
    
    // Trithemius is a precursor the Vigenère cipher, thus its encryption
    // and decryption use a similar process, though Trithemius is simpler.
    // The boolean determines: TRUE = forward shift (0, +1, +2, ...)
    // FALSE = backward shift (0, -1, -2, ...)
    public String trithemiusEncrypt(String plaintext, boolean b){
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for(int i = 0; i < plaintext.length(); i++){
            if(plaintext.charAt(i) != ' '){
                int x = getVRowCol(plaintext.charAt(i));
                sb.append(vigenereSquare[j][x]);
                if(b){
                    if(j < 25)
                        j++;
                    else
                        j = 0;
                }
                else{
                    if(j > 0)
                        j--;
                    else
                        j = 25;
                }
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
    public String trithemiusDecrypt(String ciphertext, boolean b){
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for(int i = 0; i < ciphertext.length(); i++){
            if(ciphertext.charAt(i) != ' '){
                int x = getVRowCol(ciphertext.charAt(i));
                sb.append(vigenereSquare[j][x]);
                if(b){
                    if(j > 0)
                        j--;
                    else
                        j = 25;
                }
                else{
                    if(j < 25)
                        j++;
                    else
                        j = 0;
                }
            }
            else
                sb.append(" ");
        }
        return sb.toString();
    }
    
}