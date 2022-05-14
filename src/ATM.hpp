/*
 * ATM.hpp Version#1.0
 * Frank Bernal
 * CIS 054 C/C++ Programming
 * Input
 * Output
 * 13 May 2022
 */
 
 #pragma once
 using namespace std;

 // Location of the ATM data file on the disk
 static const char ATMfilename[] = "/Users/frankbernal/Documents/GitHub/FakeATM/src/ATM_accounts.dat";

 class ATM {
     private:
        int acctNo;
        int pin;
        double checking;
        double savings;

    public:
        static const int ATMsize = sizeof(int) * 2 + sizeof(double) * 2;
        ATM(int acct = 0, int p =0, double chk = 0, double sav = 0) {
            acctNo = acct;
            pin = p;
            checking = chk;
            savings = sav;
        }

        // Setters
        int setAcctNo(int acctNo) { return this -> acctNo = acctNo; }
        int setPIN(int pin) { return this -> pin = pin; }
        double setChecking(double checking) {return this -> checking = checking; }
        double setSavings(double savings) { return this -> savings = savings; }

        // Getters
        int getAcctNo() { return acctNo; }
        int getPIN() { return pin; }
        double getChecking() { return checking; }
        double getSavings() { return savings; }
 };   // End of Class ATM

 // Function prototypes
 char getChar();
 int getInt();
 double getDouble();
 streamoff searchForCustomer();   // Position in the file, or neg-number if not found
 int displayFile();
 char selectAccount(streamoff customerIndex);
 double getBalance(streamoff customerIndex, char accountType);
 char selectTransaction();
 void deposit(streamoff customerIndex, char accountType);
 void withdraw(streamoff customerIndex, char accountType);