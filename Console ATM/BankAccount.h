// Created by Mark Deyarmond on 2023-10-25.

#ifndef A2_BANKACCOUNT_H
#define A2_BANKACCOUNT_H

#include <vector>

class BankAccount {
private:
    // Global variables: menuInput to store user choice & vectors to store user info
    char menuInput;
    std::vector<int> userID;
    std::vector<int> passcode;
    std::vector<double> accountBalance;
    int currentUser;

public:
    // Functions for the bank account
    void printIntroMenu();
    void printMainMenu();
    void start();
    void login();
    void createAccount();
};

#endif //A2_BANKACCOUNT_H