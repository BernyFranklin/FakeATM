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
        int accountNo;
        int pin =0;
        long customerIndex;   // -2 = customer not found, -3 = PIN doesn't match

        // Customer record (acctNo, PIN, chk, sav)
        ATM_record customer = new ATM_record();

        // Prompt for user input
        System.out.print ("Enter a customer account number,");
        System.out.print ("0 displays the customer data file");
        System.out.print ("or Ctrl-C to end the program\n? ");   // 0 = display file
        // User input
        accountNo = getInt();

        // Conditional statements for user input

        // Display the file
        if (accountNo == 0) {
            displayFile();
            return -4;
        }   // End of == 0

        if (accountNo >= 0) {
            System.out.print ("Enter PIN? ");
            pin = getInt();
        }   // End of good acount number

        // Start binary search
        if (BINARY_SEARCH) {
            // The first thing to do is determine the number of customers in file
            // Open the customer data file at the end of the file
            RandomAccessFile ATM_file;
        }   // End of binary search
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
    
    // Start displayFile()
    //      Displays all of the customer records in the file
    //      Return: 0 = success, -1 = error displaying file
    static int displayFile() {
        // Customer record (acctNo, PIN, chk, sav)
        ATM_record customer  = new ATM_record();
        RandomAccessFile ATM_file;

        // Start try
        try {
            // Open the customer file
            ATM_file = new RandomAccessFile(ATM_FILENAME, "r");

            // Display header info
            System.out.printf ("%-7s    %-4s  %-8s   %-8s", 
                                "Acct# ", "PIN", "Checking", "Savings");
            
            // Read the first customer record
            customer.setAcctNo(ATM_file.readInt());
            customer.setPIN(ATM_file.readInt());
            customer.setChecking(ATM_file.readDouble());
            customer.setSavings(ATM_file.readDouble());

            // Read until end of file is reached
            while (true) {
                System.out.println (customer);
                // Read next customer
                customer.setAcctNo(ATM_file.readInt());
                customer.setPIN(ATM_file.readInt());
                customer.setChecking(ATM_file.readDouble());
                customer.setSavings(ATM_file.readDouble());

            }   // End of file

        }   // End of try
        catch (EOFException e) {
            // Reached EOF without finding cutomer ?
            ATM_file.close();
            return 0;   // Success
        }   // End of EOFException
        catch (Exception e) {
            System.out.print ("Unable to open ATM_accounts ");
            return -1;
        }   // End of Exception
    }   // End of displayFile

}   // End of javaATM
