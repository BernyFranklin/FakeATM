/*
 * javaATM.java Version#1.0
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
            System.out.print ("== Welcome to Phony Bank ==\n");
            System.out.print ("===========================\n");

            customerIndex = searchForCustomer();

            // Start loop
            do {
                // Can't open customer file
                if (customerIndex == -1) {
                    return;   // Exit program
                }   // End of can't open
                // No account number found
                else if (customerIndex == -2) {
                    System.out.print ("Account Number not found\n");
                    continue;
                }   // End of no account number found
                // Incorrect pin
                else if (customerIndex == -3) {
                    System.out.print ("Incorrect PIN\n");
                    continue;
                }   // End of incorrect pin
                // File displayed
                else if (customerIndex == -4) {
                    continue;
                }   // End of file displayed

                // Call selectAccount()
                // 'C' = checking 'S' = savings 'X' = quit
                char checkingOrSavings = selectAccount(customerIndex);

                
                // Done with customer
                if (checkingOrSavings == 'X')
                    break;
                else if (checkingOrSavings == 'C')
                    System.out.printf ("\nChecking Selected\n");
                else if (checkingOrSavings == 'S')
                    System.out.printf ("\nSavings Selected\n");
                
                char transaction = selectTransaction();

                switch (transaction) {
                    case 'B':  // Balance
                        System.out.printf ("Balance: $%.2f\n", getBalance(customerIndex, checkingOrSavings));
                        break;
                    case 'D':   // Deposit
                        deposit(customerIndex, checkingOrSavings);
                        break;
                    case 'W':   // Withdraw
                        withdraw(customerIndex, checkingOrSavings);
                        break;
                    case 'X':
                        break;

                }   // End of switch case

                // Do you want another request for the same customer?
                do {
                    System.out.print ("Do you want another transaction for the same customer (Y/N)?");
                    anotherRequest = Character.toUpperCase(getChar());
                } while (anotherRequest != 'Y' && anotherRequest != 'N');

            } while (anotherRequest == 'Y');

            // 5 extra liones to provide space on the screen to separate between customes
            System.out.print ("\n\n\n\n\n");

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
        System.out.print ("Enter a customer account number,\n");
        System.out.print ("[0] displays the customer data file\n");
        System.out.print ("Enter here: ");   // 0 = display file
        // User input
        accountNo = getInt();

        // Conditional statements for user input

        // Display the file
        if (accountNo == 0) {
            displayFile();
            return -4;
        }   // End of == 0

        if (accountNo >= 0) {
            System.out.print ("Enter PIN: ");
            pin = getInt();
        }   // End of good acount number

        // Start binary search
        if (BINARY_SEARCH) {
            // The first thing to do is determine the number of customers in file
            // Open the customer data file at the end of the file
            RandomAccessFile ATM_file;

            // Try to open file
            try {
                ATM_file = new RandomAccessFile(ATM_FILENAME, "r");

                // Number of customers = file size / customer record size
                long nbytes = ATM_file.length();   // Length in bytes
                long customerCount = nbytes / ATM_record.size;

                // Prepare for binary search
                long imin = 0;                  // Start of current search
                long imax = customerCount -1;   // End index of current search
                long imid;                      // midpoint for roughly equal parts

                // Assume customer not found until it happens
                customerIndex = -2;
                // Start loop
                while (imax >= imin) {
                    imid = (imin + imax) / 2;   // Middle
                    // Seek to selected customer
                    long customerPositionInFile = imid * ATM_record.size;
                    ATM_file.seek(customerPositionInFile);
                    // Read customer record into customer object
                    customer.setAcctNo(ATM_file.readInt());
                    customer.setPIN(ATM_file.readInt());
                    customer.setChecking(ATM_file.readDouble());
                    customer.setSavings(ATM_file.readDouble());

                    // Found customer in file
                    if (accountNo == customer.getAcctNo()) {
                        // Pin matches
                        if (pin == customer.getPIN())
                            customerIndex = imid;
                        // Pin doesn't match
                        else
                            customerIndex = -3;
                        break;   // Customer has been found
        
                    }   // End of found customer

                    // Divide search
                    else if (accountNo < customer.getAcctNo())
                        imax = imid -1;    // Lower half
                    else 
                        imin = imid + 1;   // Upper half

                }   // End of loop
            }   // End of try
            catch (Exception e) {
                System.out.print ("Unable to open ATM_accounts ");
                return -1;
            }   // End of Exception e

        }   // End of binary search

        // Start sequential search
        else {
            // Open the customer data file
            RandomAccessFile ATM_file;

            // Try to open file
            try {
                ATM_file = new RandomAccessFile(ATM_FILENAME, "r");
                // Read first customer record
                customer.setAcctNo(ATM_file.readInt());
                customer.setPIN(ATM_file.readInt());
                customer.setChecking(ATM_file.readDouble());
                customer.setSavings(ATM_file.readDouble());

                // Loop through rest of file
                for (int i = 0; true; i++) {
                    // Customer found
                    if (accountNo == customer.getAcctNo()) {
                        // Pin matches
                        if (pin == customer.getPIN()) customerIndex = i;
                        // Pin doesn't match
                        else customerIndex = -3;
                        // Exit loop
                        break;
                    }   // End of found

                    // Read next record
                    customer.setAcctNo(ATM_file.readInt());
                    customer.setPIN(ATM_file.readInt());
                    customer.setChecking(ATM_file.readDouble());
                    customer.setSavings(ATM_file.readDouble());

                }   // End of for loop
                ATM_file.close();
            }   // End of try
            catch (EOFException e) {
                // Reached end of file without finding customer
                customerIndex= -2;
            }   // End of EOFException
            catch (Exception e) {
                System.out.print ("Unable to open ATM_accounts ");
                return -1;
            }   // End of Exception e

        }   // End of sequential search

        // Customer has been found. Display balances
        if (customerIndex >= 0) {
            System.out.printf ("\n Checking $%.2f  Savings $%.2f\n\n", 
                                customer.getChecking(), customer.getSavings());
        }   // End of customer found

        return customerIndex;
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
    
    // Start getDouble()
    //      Reads a real number from standard input (keyboard)
    //      Returns the first char that was input
    //      Displays an error message and tries again if error is detected
    static double getDouble() {
        double result = 0.0;
        boolean tryAgain;

        do {
            tryAgain = false;
             try {
                 result = stdin.nextDouble();
             }
             catch (Exception e) {
                 System.out.print ("*** Illegal entry. Enter a number. Try again\n?");
                 tryAgain = true;
             }
        } while (tryAgain);
        return result;

    }   // End of get double

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
            System.out.printf ("%-7s    %-4s  %-8s   %-8s\n", 
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
            return 0;   // Success
        }   // End of EOFException
        catch (Exception e) {
            System.out.print ("Unable to open ATM_accounts ");
            return -1;
        }   // End of Exception
    }   // End of displayFile

    // Start selectAccount()
    //      Return: 'C' or 'S' for checking or savings
    static char selectAccount(long customerIndex) {
        System.out.print ("Select account");
        System.out.print ("  C = checking");
        System.out.print ("  S = savings");
        System.out.print ("  X = cancel");

        char accountType = ' ';   // C  = checking S = savings

        // Start loop until valid data
        do {
            System.out.print (": ");
            accountType = Character.toUpperCase(getChar());

            if (accountType == 'C' || accountType == 'S' || accountType =='X')
                break;   // Input legal
            System.out.print ("Illegal selection, try again\n");
        } while (accountType != 'C' && accountType != 'S' && accountType != 'X');

        return accountType;
    }   // End of selectAccount

    // Start getChar()
    //      Reads line of text from standard input (keyboard)
    //      Returns the 1st char that was input
    //      Rest of line gets discarded
    static char getChar() {
        return (stdin.next().toUpperCase().charAt(0));
    }   // End of getChar

    // Start selectTransaction
    //      Returns 'B' 'W' 'D' or 'X'
    static char selectTransaction() {
        System.out.println ("Select transaction:");
        System.out.println ("  B = Balance");
        System.out.println ("  D = Deposit");
        System.out.println ("  W = Withdraw");
        System.out.println ("  X = Cancel");

        char transactionType = ' ';
        do {
            System.out.print (": ");
            transactionType = getChar();
            if (transactionType == 'B' || transactionType =='D' || transactionType == 'W' || transactionType == 'X')
                break;
            System.out.println ("  Illegal selection. Try again.");
        } while (transactionType != 'B' && transactionType != 'D' && transactionType != 'W' && transactionType != 'X');

        return transactionType;
    }   // End selectTransaction

    // Start getBalance()
    //      Where:
    //              customerIndex = customer number in file
    //              accountType = 'C' checking or 'S' savings
    //      Return: balance of either customer's checking or savings account
    static double getBalance(long customerIndex, char accountType) {
        // Customer record (acctNo, PIN, chk, sav)
        ATM_record customer = new ATM_record();
        double balance = -1;
        RandomAccessFile ATM_file;

        // Try to open file
        try {
            ATM_file = new RandomAccessFile(ATM_FILENAME, "r");
            // Seek to the selected file
            ATM_file.seek(customerIndex * ATM_record.size);
            // Read customer record
            customer.setAcctNo(ATM_file.readInt());
            customer.setPIN(ATM_file.readInt());
            customer.setChecking(ATM_file.readDouble());
            customer.setSavings(ATM_file.readDouble());

            // Return Balance of selected account type
            if (accountType =='C')
                balance = customer.getChecking();
            else if (accountType == 'S')
                balance = customer.getSavings();
            // Close file
            ATM_file.close();

            return balance;
        }   // End of try
        catch (EOFException e ) {
            return -1;
        }   // End of EOFException
        catch (Exception e) {
            System.out.print ("Unable to open ATM_accounts");
            return -2;
        }   // End of Exception 

    } // End of getBalance


    // Start deposit()
    //      Where:
    //              customerIndex = customer within the file
    //              accountType = 'C' checking or 'S' savings
    //      The function requests the amount to deposit, then
    //          reads the customer record  and adds the deposit
    //          and updates the customer data file
    static int deposit(long customerIndex, char accountType) {
        // Customer record (acctNo, PIN, chk, sav)
        ATM_record customer = new ATM_record();
        RandomAccessFile ATM_file;
        double depositAmount;
        double newBalance = 0.0;
        System.out.print ("Enter the amount to deposit: ");

        depositAmount = getDouble();

        if (depositAmount <= 0)
            System.out.print ("Deposit must be greater than zero\n\n");
        else {
            // Try to open the file
            try {
                ATM_file = new RandomAccessFile(ATM_FILENAME, "rw");
                // Seek to the requested record
                ATM_file.seek(customerIndex * ATM_record.size);

                // Read the record
                customer.setAcctNo(ATM_file.readInt());
                customer.setPIN(ATM_file.readInt());
                customer.setChecking(ATM_file.readDouble());
                customer.setSavings(ATM_file.readDouble());

                // Update the balance for the selected account
                if (accountType == 'C') {
                    newBalance = customer.getChecking() + depositAmount;
                    customer.setChecking(newBalance);
                }   // End of checking
                else if (accountType == 'S') {
                    newBalance = customer.getSavings() + depositAmount;
                    customer.setSavings(newBalance);
                }   // End of savings

                // Seek back to requested record and update data file
                ATM_file.seek(customerIndex * ATM_record.size);
                ATM_file.writeInt(customer.getAcctNo());
                ATM_file.writeInt(customer.getPIN());
                ATM_file.writeDouble(customer.getChecking());
                ATM_file.writeDouble(customer.getSavings());
                ATM_file.close();

                // Display updated balance
                System.out.printf ("Your balance is $%.2f\n", newBalance);
            }   // End of try
            catch (EOFException e) {
                return -1;
            }   // End of EOFException
            catch (Exception e) {
                System.out.print ("Unable to open ATM_accounts ");
                return -2;
            }   // End of Exception

        }   // End of valid deposit

        return 0;
    }   // End of deposit

    // Start withdraw()
    //      Where:
    //              customerIndex = customer within the file
    //              acountType = 'C' checking or 'S' Savings
    //      The function requests the amount to withdraw, then
    //          validates that the amount is an even multiple of $20.00
    //      Maximum withdraw is $500.00
    //      Reads the customer record and validates sufficient funds
    //      Deducts the withdrawal and updates the customer data file
    static int withdraw(long customerIndex, char accountType) {
        // Customer record (acctNo, PIN, chk, sav)
        ATM_record customer = new ATM_record();
        RandomAccessFile ATM_file;
        double withdrawAmount;
        double newBalance = 0.0;

        System.out.print ("Enter the amount of the withdrawal in increments of $20 up to $500: ");

        withdrawAmount = getDouble();

        // Compute amount of withdrawal in pennies used to check for increments of $20.00
        int intWithdrawX100 = (int)(withdrawAmount * 100);   // COnvert to pennies

        if (withdrawAmount <= 0.00)
            System.out.print ("Withdrawal must be greater than zero\n\n");
        else if (withdrawAmount > 500.00)
            System.out.print ("Withdrawal must not exceed $500.00\n\n");
        else if (intWithdrawX100 % 2000 != 0 )   // 2000 = $20.00 in pennies
            System.out.print ("Withdrawal must be in increments of $20.00\n\n");
        // Withdraw amount legal
        else  {
            // Try and open the file
            try {
                ATM_file = new RandomAccessFile(ATM_FILENAME, "rw");
                // Seek to selected customer record
                ATM_file.seek(customerIndex * ATM_record.size);
                // Read customer record
                customer.setAcctNo(ATM_file.readInt());
                customer.setPIN(ATM_file.readInt());
                customer.setChecking(ATM_file.readDouble());
                customer.setSavings(ATM_file.readDouble());

                // Update the balance for the selected account type
                if (accountType == 'C') {
                    newBalance = customer.getChecking() - withdrawAmount;
                    customer.setChecking(newBalance);
                }   // End of checking
                else if (accountType == 'S') {
                    newBalance = customer.getSavings() - withdrawAmount;
                    customer.setSavings(newBalance);
                }   // End of savings

                // Seek back to the same record and write the updated record back to disk
                ATM_file.seek(customerIndex * ATM_record.size);
                ATM_file.writeInt(customer.getAcctNo());
                ATM_file.writeInt(customer.getPIN());
                ATM_file.writeDouble(customer.getChecking());
                ATM_file.writeDouble(customer.getSavings());
                ATM_file.close();

                // Display the updated balance
                System.out.printf ("Your balance is $%.2f\n", newBalance);

            }   // End of try
            catch (EOFException e) {
                return -1;
            }   // End of EOFException
            catch (Exception e) {
                System.out.print ("Unable to open ATM_accounts ");
                return -2;
            }   // End of Exception
        }   // End of valid withdraw amount

        return 0;   // No Error
    }   // End of withdraw

}   // End of javaATM
