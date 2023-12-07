// Created by Mark Deyarmond on 2023-10-25.

#include "BankAccount.h"
#include <iostream>
#include <limits>
#include <iomanip>

void BankAccount::start() {

    // Loop code in a while loop, exit if user types 'q'
    while (true) {
        printIntroMenu();
        std::cin >> menuInput;

        // Switch statement that will cycle through possible options, converting menuInput to lowercase
        switch (tolower(menuInput)) {
            case 'l':
                login();
                break;
            case 'c':
                createAccount();
                break;
            case 'q':
                std::cout << "\nThank you for using the ATM, goodbye!" << std::endl;
                exit(0);
            default:
                // The in event someone enters anything more than 1 character not from the above
                std::cout << "Invalid option: please try again.";
                std::cin.clear(); // Clears the "fail state" from input error
                std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
                break;
        }
    }
}

void BankAccount::printIntroMenu() {
    std::cout << "Please select an option from the menu below:" << std::endl;
    std::cout << "L -> Login\nC -> Create new account\nQ -> Quit" << std::endl;
    std::cout << "> ";
}

void BankAccount::printMainMenu() {
    // Variables for a transaction
    double depAmt;
    double wdAmt;

    // Loop through the options until user types 'q' to logout
    while (true) {

        std::cout << "Please select an option from the menu below:" << std::endl;
        std::cout << "D -> Deposit Money\nW -> Withdraw money\nR -> Request balance\nQ -> Logout" << std::endl;
        std::cout << "> ";
        std::cin >> menuInput;

        // Find the index of currentUser in userID vector to find the matching index in 'accountBalance' vector
        int userIndex = -1; // Initialize this variable to an invalid value

        for (int i = 0; i < userID.size(); ++i) {
            if (userID[i] == currentUser) {
                userIndex = i;
                break;
            }
        }

        // Error handling for userID iteration: if it returns -1 then there was a problem
        if (userIndex == -1) {
            std::cout << "Error: user not found" << std::endl;
            return;
        }


        // Switch statement to handle deposit, withdraw or balance
        switch (tolower(menuInput)) {
            case 'd':
                std::cout << "Amount of deposit: $";
                // Error handling to ensure only numbers are entered for depAmt, otherwise program enters infinite loop
                // 'peek' checks if there are any non number type values entered by the user
                if (!(std::cin >> depAmt) || std::cin.peek() != '\n') {
                    std::cout << "Error: only number values accepted\n" << std::endl;
                    std::cin.clear(); // Clears the "fail state" from input error
                    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
                } else {
                    std::cout << std::endl; // Add extra line for padding
                    accountBalance[userIndex] += depAmt;
                }
                //std::cin >> depAmt;
                break;
            case 'w':
                std::cout << "Amount of withdrawal: $";
                // Error handling to ensure only numbers are entered for wdAmt, otherwise program enters infinite loop
                // 'peek' checks if there are any non number type values entered by the user
                if (!(std::cin >> wdAmt) || std::cin.peek() != '\n') {
                    std::cout << "Error: only number values accepted\n" << std::endl;
                    std::cin.clear(); // Clears the "fail state" from input error
                    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
                } else {
                    std::cout << std::endl; // Add an extra line for padding
                    if (wdAmt > accountBalance[userIndex]) {
                        std::cout << "Error: insufficient funds\n" << std::endl;
                    } else {
                        accountBalance[userIndex] -= wdAmt;
                    }
                }
                break;
            case 'r':
                std::cout << "Your balance is $" << std::fixed << std::setprecision(2) << accountBalance[userIndex] << "\n" << std::endl;
                break;
            case 'q':
                std::cout << "\nLogging out...\n" << std::endl;
                return;
            default:
                std::cout << "Invalid option: please try again\n" << std::endl;
                // The in event someone enters anything more than 1 character not from the above
                std::cin.clear(); // Clears the "fail state" from input error
                std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
                break;
        }
    }
}

void BankAccount::login() {
    // Variables for fetching userID and passcode info
    int loginID;
    int loginPass;

    std::cout << "\nEnter user id: ";

    // Error handling to ensure only int type is entered, otherwise an infinite loop occurs
    if (!(std::cin >> loginID)) {
        std::cout << "Error: Only numbers are accepted\nExiting to main menu...\n" << std::endl;
        std::cin.clear(); // Clears the "fail state" from input error
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
        return;
    }

    // Error handling to ensure only int type is entered, otherwise an infinite loop occurs
    std::cout << "Enter passcode: ";
    if (!(std::cin >> loginPass)) {
        std::cout << "Error: only numbers are accepted\nExiting to main menu...\n" << std::endl;
        std::cin.clear(); // Clears the "fail state" from input error
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
        return;
    }

    // Loop through userID and passcode vectors to match values and validate
    bool validLogin = false;
    for (int i = 0; i < userID.size(); ++i) {
        if (userID[i] == loginID && passcode[i] == loginPass) {
            validLogin = true;
            currentUser = userID[i];
            break;
        }
    }

    if (validLogin) {
        std::cout << "Access granted!\n" << std::endl;
        printMainMenu();
    } else {
        std::cout << "\n******** LOGIN FAILED! ********\n" << std::endl;
    }
}

void BankAccount::createAccount() {
    // Variables for creating userID and passcode info
    int newUserID;
    int newUserPass;
    bool isProcessInvalid = true; // Flag to consider 'is the account creation process invalid?'

    while (isProcessInvalid) {
        std::cout << "\nChoose a User ID: ";

        // Error handling to check only type int is used for newUserID
        // 'peek' checks if there are any non int values entered by the user
        if (!(std::cin >> newUserID) || std::cin.peek() != '\n') {
            std::cout << "Error: only numbers are accepted\nExiting to main menu...\n" << std::endl;
            std::cin.clear(); // Clears the "fail state" from input error
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
            return;
        }

        // Flag for duplicate account check
        bool userExists = false;

        for (int i = 0; i < userID.size(); ++i) {
            if (userID[i] == newUserID) {
                std::cout << "Error: User ID already exists.\n";
                userExists = true;
                break;
            }
        }

        // If userID doesn't exist in vector, break loop by setting account creation invalidity to 'false'
        if (!userExists) {
            isProcessInvalid = false;
        }
    }

    std::cout << "Choose a passcode: ";

    // Error handling to check only type int is used for newUserPass
    // 'peek' checks if there are any non int values entered by the user
    if (!(std::cin >> newUserPass) || std::cin.peek() != '\n') {
        std::cout << "Error: only numbers are accepted\nExiting to main menu...\n" << std::endl;
        std::cin.clear(); // Clears the "fail state" from input error
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Clear input buffer
        return;
    }

    userID.push_back(newUserID); // Push the user created ID into the userID vector
    passcode.push_back(newUserPass); // Push the user created passcode in passcode vector
    accountBalance.push_back(0.00); // Set the users account balance to $0.00 to start
    std::cout << "Thank you! Your account has been created!\n" << std::endl;
}