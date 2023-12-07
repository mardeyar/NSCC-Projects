#include <iostream>
#include "BankAccount.h"

int main() {
    // Instantiate an instance of a bank account to use functions from the class
    BankAccount account;

    // Start the program
    std::cout << "\nWelcome to CashStop ATM Machine" << std::endl;

    account.start();
    return 0;
}