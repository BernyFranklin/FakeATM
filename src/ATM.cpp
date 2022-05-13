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
void withdraw(streamoff customerIndex, char accountType) {

}   // End of withdraw

// Start getChar
char getChar() {

}   // End of getChar

// Start getInt
int getInt() {

}   // End of getInt

// Start getDouble
//      Reads a real number from standard input (keyboard)
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
            tryAgain = true;
        }
    } while (tryAgain);

    return result;
}   // End of getDouble