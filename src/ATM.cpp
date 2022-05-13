/*
 * ATM.cpp Version#1.0
 * Frank Bernal
 * CIS 054 C/C++ Programming
 * Input
 * Output
 * 13 May 2022
 */

#include <iostream>
#include <fstream>
#include <iomanip>
#include <cstring>
#include <cstdlib>
#include <cctype>
#include "ATM.hpp"
using namespace std;

// Start main
int main() {

}   // End of main

// Start searchForCustomer
//      Return:
//          >= 0 index into the file for the selected customer
//          -1 = unable to open the customer file
//          -2 = customer not found  in the file
//          -3 = PIN doesn't match
//          -4 = customer file displayed to stdout
streamoff searchForCustomer() {
    int accountNo;
    int pin;
    streamoff customerIndex= 0;
    ATM customer;
    cout << "Select a customer account number" << endl;
    cout << "[0] key to display the customer data file" << endl;
    cout << "Enter selection: ";
    accountNo = getInt();

    if (accountNo == 0) {  // Display file
        displayFile();
        return -4;        // File displayed
    }   // End of Display file

    if (accountNo >= 0) {
        cout << "Enter PIN: ";
        pin = getInt();
    }   // End of Account number entered

    // Open customer data file
    ifstream ATM_file(ATMfilename, ios::binary);

    if (ATM_file.fail()) {
        cout << "Unable to open ATM_accounts" << endl;
        return -1;   // Unable to open file
    }   // End of fail

    ATM_file.read((char*)&customer, sizeof(customer));   // Read first record

    for (int i =0; !ATM_file.eof(); i++) {
        if (accountNo == customer.getAcctNo()) {
            // Customer found
            if (pin == customer.getPIN()) customerIndex = i;   // Pin matches customer
            else customerIndex = -3;                           // Pin doesn't match
            break;                                             // Pin and customer match, exit
        }   // End of valid customer and pin

        ATM_file.read((char*)&customer, sizeof(customer));   // Next customer
    }   // End of loop

    if (ATM_file.eof())   // Reached end of file with no match
        customerIndex = -2;
    ATM_file.close();

    if (customerIndex >= 0) {
        // Customer found 
        cout << fixed << showpoint << setprecision(2);
        cout << endl << "Checking $" << customer.getChecking()
             << " Savings $" << customer.getSavings() << endl << endl;
    }   // End of found customer

    return customerIndex;
}   // End of searchForCustomer

// Start displayFile
//      Displays all of the customer records on file
//      Returns: 0 - Success, -1 - Error displaying file
int displayFile() {
    ifstream ATM_file(ATMfilename, ios::binary);

    if (ATM_file.fail()) {
        cout << " Unable to open ATM_accounts data file" << endl;
        return -1;
    }   // End of fail

    ATM customer;
    // Display header info
    cout << fixed << showpoint << setprecision(2);   // Format output
    cout << setw(-7) << " Acct #" << "     "
         << setw(-4) << "PIN" << "   "
         << setw(-8) << "Checking" << "   "
         << setw(-8) << "Savings" << endl;

    ATM_file.read((char*)&customer, sizeof(customer));   // Read first customer
    for (int i = 0; !ATM_file.eof(); i++) {
        cout << setw(7) << customer.getAcctNo() << "     "
             << setw(4) << customer.getPIN() << "   "
             << setw(8) << customer.getChecking() << "   "
             << setw(8) << customer.getSavings() << endl;
        ATM_file.read((char*)&customer, sizeof(customer));   // Read next customer
    }   // End of loop
    ATM_file.close();

    return 0;
}   // End of displayFile

// Start selectAccount
//      Returns: 'C' checking or 'S' savings
char selectAccount(streamoff customerIndex) {
    cout << "select account:" << endl;
    cout << "  C = checking" << endl;
    cout << "  S = savings" << endl;
    cout << "  X = cancel" << endl;

    char accountType = ' '; 

    do {
        cout << ": ";
        accountType = toupper(getChar());

        if (accountType == 'C' || accountType == 'S' || accountType == 'X')
            break;   // Legal selection
    } while (accountType != 'C' && accountType != 'S' && accountType != 'X');

    return accountType;
}   // End of selectAccount

// Start getBalance
//      Where:
//          customerIndex = customer number in the file
//          accountType = 'C' checking or 'S' savings
//      Return: balance of either checking or savings account of customer
double getBalance(streamoff customerIndex, char accountType) {
    double balance = 0.0;
    // Search the file for the customer number
    ifstream ATM_file(ATMfilename, ios::binary);

    if (ATM_file.fail()) {
        cout << "Unable to open ATM_accounts " << endl;
        return balance;
    }   // End of fail

    ATM customer;    // Customer object
    // Seek to the selected customer
    streamoff customerPositionInFile = customerIndex * ATM::ATMsize;
    ATM_file.seekg(customerPositionInFile, ATM_file.beg);
    ATM_file.read((char*)&customer, sizeof(customer));

    if (accountType == 'C')
        balance = customer.getChecking();
    else if (accountType == 'S')
        balance = customer.getSavings();
    ATM_file.close();   // Close file

    return balance;
}   // End of getBalance

// Start selectTransaction
//      Returns 'B' || 'D' || 'W' || 'X'
char selectTransaction() {
    cout << "Select transaction:" << endl;
    cout << "  B = Balance" << endl;
    cout << "  D = Deposit" << endl;
    cout << "  W = Withdraw" << endl;
    cout << "  X = Cancel" << endl;

    char transactionType = ' ';

    do {
        transactionType  = toupper(getChar());

        if (transactionType == 'B' || transactionType == 'D' || transactionType == 'W' || transactionType == 'X')
            break;   // Legal selection
        cout << "  Illegal selection. Try Again." << endl;

    } while (transactionType != 'B' && transactionType != 'D' && transactionType != 'W' && transactionType != 'X');

    return transactionType;
}   // End of selectTransaction

// Start deposit
//      Where:
//          customerIndex = customer within the file
//          accountType = 'C' checking or 'S' savings
//      The function requests the amount to deposit, then
//      updates the data file    
void deposit(streamoff customerIndex, char accountType) {
    double depositAmount;
    double newBalance = 0.0;

    cout << "Enter the amount to deposit: ";
    depositAmount = getDouble();   // Check for > 0 after getting current balance

    if (depositAmount <= 0.00)
        cout << "Deposit must be greater than zero" << endl << endl;
    else {
        // Open in binary mode for both reading and writing
        fstream ATM_file(ATMfilename, ios::binary | ios::in | ios::out);
        if (ATM_file.fail()) {
            cout << "Unable to open ATM_accounts data file" << endl;
            return;
        }
        ATM currentCustomer;
        // Seek to the selected customer from beginning of the file and update the balance
        streamoff customerPositionInFile = customerIndex * ATM::ATMsize;
        ATM_file.seekg(customerPositionInFile, ATM_file.beg);
        ATM_file.read((char*)&currentCustomer, sizeof(ATM));

        if (accountType == 'C') {
            // Update checking balance
            newBalance = currentCustomer.getChecking() + depositAmount;
            currentCustomer.setChecking(newBalance);
        }   // End of checking
        else if (accountType == 'S') {
            // Update savings balance
            newBalance = currentCustomer.getSavings() + depositAmount;
            currentCustomer.setSavings(newBalance);
        }   // End of savings

        // Seek back to the same record and write the updated record back to disk
        ATM_file.seekg(customerPositionInFile, ATM_file.beg);
        ATM_file.write((char*)&currentCustomer, sizeof(ATM));

        ATM_file.close();   // Close the file

        // Display updated balance
        cout << fixed << showpoint << setprecision(2); // display 2 digits past decimal
        cout << "Your balance is $" << newBalance << endl;
    }   // End of processed deposit
}   // End of deposit

// Start withdraw
//      Where:
//          customerIndex = customer within the file
//          accountType = 'C' checking or 'S' savings
//      The function requests the amount to withdraw, then 
//      validates that the amount is an even multiple of
//      $20.00, maximum withdraw is $500.00
//      Reads the customer record and validates sufficient funds
//      and deducts the amount and updates the data file    
void withdraw(streamoff customerIndex, char accountType) {
    double withdrawAmount;
    double newBalance = 0.0;

    cout << "Enter the amount of the withdrawal in increments of $20 up to $500: ";
    withdrawAmount = getDouble();   // Check for > 0 after getting current balance

    // Comput amount to withdraw in pennies used to check for increments of $20.00
    int intWithdrawX100 = (int)(withdrawAmount * 100);   // convert to pennies

    if (withdrawAmount <= 0.00)
        cout << "Withdrawal must be greater than zero" << endl << endl;
    else if (withdrawAmount > 500.00)
        cout << "Withdraw must not exceed $500" << endl << endl;
    else if (intWithdrawX100 % 2000 != 0)   // 2000 is $20 in pennies
        cout << "Withdraw must be in increments of $20" << endl << endl;
    else {
        // Open in binary mode for both reading and writing
        fstream ATM_file(ATMfilename, ios::binary | ios::in | ios::out);
        if (ATM_file.fail()) {
            cout << "Unable to open ATM_accounts data file" << endl;
            return;
        }
        ATM currentCustomer;
        // Seek to the selected customer from beginning of the file and update the balance
        streamoff customerPositionInFile = customerIndex * ATM::ATMsize;
        ATM_file.seekg(customerPositionInFile, ATM_file.beg);
        ATM_file.read((char*)&currentCustomer, sizeof(ATM));

        if (accountType == 'C') {
            // Update checking balance
            newBalance = currentCustomer.getChecking() - withdrawAmount;
            currentCustomer.setChecking(newBalance);
        }   // End of checking
        else if (accountType == 'S') {
            // Update savings balance
            newBalance = currentCustomer.getSavings() - withdrawAmount;
            currentCustomer.setSavings(newBalance);
        }   // End of savings

        // Seek back to the same record and write the updated record back to disk
        ATM_file.seekg(customerPositionInFile, ATM_file.beg);
        ATM_file.write((char*)&currentCustomer, sizeof(ATM));

        ATM_file.close();   // Close the file

        // Display updated balance
        cout << fixed << showpoint << setprecision(2); // display 2 digits past decimal
        cout << "Your balance is $" << newBalance << endl;
    }   // End of processed withdrawal

}   // End of withdraw

// Start getChar
//      Reads a line of text from standard input
//      Returns a character
char getChar() {
    char buff[100];
    cin.getline(buff, 100);
    return(buff[0]);
}   // End of getChar

// Start getInt
//      Reads a number from standard input
//      Retruns an integer
//      Displays error for bad input
int getInt() {
    int result;
    bool tryAgain;

    do {
        tryAgain = false;
        try {
            char buff[100];
            cin.getline(buff, 100);
            result = atoi(buff);

            if (cin.fail())
                throw 1;
        }
        catch (...) {
            cout << "*** Illegal entry. Enter an integer. Try again." << endl << ": ";
            cin.clear();              // Clear buffer
            cin.ignore(1000, '\n');   // Ignore anything else before cin
            tryAgain = true;
        }
    } while (tryAgain);

    return result;
}   // End of getInt

// Start getDouble
//      Reads a real number from standard input
//      Returns a double
//      Displays error for bad input
double getDouble() {
    double result;
    bool tryAgain;

    do {
        tryAgain = false;
        try {
            char buff[100];
            cin.getline(buff, 100);
            result = atof(buff);
            if (cin.fail())
                throw 1;   // Error code 1
        }
        catch(...) {
            cout << "*** Illegal entry. Enter a number. Try again." << endl << ": ";
            cin.clear();              // Clear buffer
            cin.ignore(1000, '\n');   // Ignore anything else before cin
            tryAgain = true;
        }
    } while (tryAgain);

    return result;
}   // End of getDouble
