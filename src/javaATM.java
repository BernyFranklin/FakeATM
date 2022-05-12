/*
 * fakeATM Version#1.0
 * Frank Bernal
 * CIS 084 Java Programming
 * Input
 * Output
 * 12 May 2022
 */

 import java.util.Scanner;
 import java.io.RandomAccessFile;
 import java.io.FileNotFoundException;
 import java.io.EOFException;

public class javaATM {
    // Select either the binary search or sequential search working with the customer data file
    static final boolean BINARY_SEARCH = false;   // False = sequential

    // Location of the ATM data file on the disk
    static final String ATM_FILENAME = "/Users/frankbernal/Documents/GitHub/FakeATM/src/ATM_accounts.bin";

    // Add Scanner used by input method
    static Scanner stdin;

    // Start main
    public static void main(String[] args) {
        // Variables
        long customerIndex;
        char anotherRequest = ' ';
        stdin = new Scanner(System.in);   // Scanner initialized

        // Infinite loop for embedded systems
        while (true) {
            // Welcome banner
            System.out.print ("\n\n===========================\n");
            System.out.print ("== Welcome to Phoby Bank ==\n");
            System.out.print ("===========================\n");

            customerIndex = searchForCustomer();
        }   // End of infinite loop
    }   // End of main

    // Start searchForCustomer()
    //      return:
    //      index into the file for the selected customer, or
    //      -1 = Unable to open customer file
    //      -2 = Customer number not found in the file
    //      -3 = PIN that is inout does not match PIN in file
    //      -4 = Entire customer file was dispayed to stdout
    static long searchForCustomer() {
        // Variables
        int acctNo;
        int pin =0;
        long customerIndex;   // -2 = customer not found, -3 = PIN doesn't match

        // Customer record (acctNo, PIN, chk, sav)
        ATM_record customer = new ATM_record();

        // Prompt for user input
        System.out.print ("Enter a customer account number,");
        System.out.print ("0 displays the customer data file");
        System.out.print ("or Ctrl-C to end the program\n? ");   // 0 = display file
        // User input
        acctNo = getInt();

    }   // End of searchForCustomer

    // Start getInt()
    //      Reads an integer from standard input (keyboard)
    //      Returns the first character that was input
    //      Displays an error message and tries again if a non-integer is input
    static int getInt() {
        // Variable
        int result = 0;
        boolean tryAgain;

        // Start loop
        do {
            // Assume good
            tryAgain = false;
            try {
                result = stdin.nextInt();
            }   // End of try
            catch (Exception e) {
                System.out.print ("*** Illegal entry. Enter an integer. Try again\n? ");
                stdin.next();   // Clear input buffer
                tryAgain = true;
            }   // End of catch

        } while(tryAgain);

        return result;
    }   // End getInt()
    
}   // End of javaATM
