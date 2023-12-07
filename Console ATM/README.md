# ATM Machine: Mark Deyarmond
This program is designed to mimic an ATM machine in which you can simulate a few different processes:<br>
1. Create an account
2. Login to your account
3. Deposit money into your account
4. Withdraw money from your account
5. Request a balance from your account
6. Exit the program

## How does it work?
Upon starting the program, you will be given 3 different options: *Login*, *create new account* or *quit*. Each option will cycle through a switch statement to choose one of the unique functions found in the **BankAccount.cpp** file.
Attempting to login before creating an account will result in a **LOGIN FAILED** error, as seen below. The same error will also be shown in the event that invalid credentials are entered. Due to this, the user will first need to create an account.<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/f586f52f-ee0f-4037-afbc-fcf9908fad18)<br>
## Create an account
Upon being presented with the initial intro menu, select the option 'c' to create your account. After entering your unique UserID information, you will have access to the function of the ATM machine.<br>
**NOTE:** only number types will be accepted. Attempting to enter a value of any type other than a number will result in an error and the user will be brought back to the main screen. Your UserID must also be *unique*: duplicate values are not allowed.<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/f4b869d9-33dd-4ef7-8544-b01709d3e464)<br>
### Login
Once your account has been created, you will have instant access to the ATM features: deposit, withdraw and request balance. These are listed in a menu where you must enter the corresponding letter to access the function. Upon account creation, account balance will be set to $0.00 until a deposit is made.<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/e6055078-5f2c-4238-9822-2ae85ebc4036)<br>
### Deposit
Selecting the option 'D' will allow the user to make a deposit into their account. **NOTE:** this will only accept number values, anything else will throw an error.<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/205e1ae0-71f6-48aa-8707-faefb3cfeca7)<br>
### Withdraw
Selecting the option 'W' will allow the user to make a withdrawal from their account. **NOTE:** this will only accept number values, anything else will throw an error. This ATM also does not allow overdraft. Thus, if user attempts to make a withdrawl that exceeds their account balance, they will get an *insufficient funds* error.<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/4a602391-5b76-401c-8a45-37431d15b8f7)<br>
### Request Balance
Selecting the option 'R' will allow the user to view the balance of their account<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/4a15486b-19ff-48bb-9814-642be5348baa)<br>
### Logout
Selecting the option 'Q' will allow the user to logout of their account and bring them back to the intro menu.<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/0ec68721-eaac-4417-9432-49a41b37e9c7)
### Quit
Back at the intro menu, user can select 'Q' to terminate the program<br><br>
![image](https://github.com/Fall2023-NSCC-ECampus/prog2100-assignment02-mardeyar/assets/117761940/89e57c9a-311b-4541-9338-ef3ed8b027df)
