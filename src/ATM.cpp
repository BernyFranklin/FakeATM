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
streamoff searchForCustomer() {

}   // End of searchForCustomer

// Start displayFile
int displayFile() {

}   // End of displayFile

// Start selectAccount
char selectAccount(streamoff customerIndex) {

}   // End of selectAccount

// Start getBalance
double getBalance(streamoff customerIndex, char accountType) {

}   // End of getBalance

// Start selectTransaction
char selectTransaction() {

}   // End of selectTransaction

// Start deposit
void deposit(streamoff customerIndex, char accountType) {

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
