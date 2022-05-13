/*
 * fakeATM Version#1.0
 * Frank Bernal
 * CIS 084 Java Programming
 * Input
 * Output
 * 12 May 2022
 */

 import java.io.Serializable;

 // Start class declaration ATM_record

 public class ATM_record implements Serializable {
     // Private member data
     private int acctNo;
     private int pin;
     private double checking;
     private double savings;

     // Default constructor
     ATM_record() {
         acctNo = 0;
         pin = 0;
         checking = 0.0;
         savings = 0.0;
     }   // End of default

     // Determine the size in bytes for the data in the ATM_record
     public final static int size = Integer.BYTES * 2 + Double.BYTES *2;

     // Setters
     public void setAcctNo (int acctNo) {this.acctNo = acctNo;}
     public void setPIN(int pin) {this.pin = pin;}
     public void setChecking(double checking) {this.checking = checking;}
     public void setSavings (double savings) {this.savings = savings;}

     // Getters
     public int getAcctNo() {return acctNo;}
     public int getPIN() {return pin;}
     public double getChecking() {return checking;}
     public double getSavings() {return savings;}

     @Override
     public String toString() {
         return String.format("%7s   %4s  %8s  %8s", acctNo, pin, checking, savings);
     }   // End of toString
     
 }   // End of ATM_record