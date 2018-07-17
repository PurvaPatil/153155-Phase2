package com.capgemini.paytm.ui;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.service.WalletService;
import com.capgemini.paytm.service.WalletServiceImpl;

public class Client {

	private WalletService walletService;
	public Client()
	{
		walletService=new WalletServiceImpl();
	}
	public void menu() {
		String ans="";	
		Scanner sc=new Scanner(System.in);
		System.out.println("****Paytm Application****");
		Customer customer=new Customer();
		BigDecimal amount;
		String mobileNo;
		do
		{
			System.out.println("1. Create Account ");
			System.out.println("2. Show Balance");
			System.out.println("3. Fund Transfer");
			System.out.println("4. Deposit amount");
			System.out.println("5. Withdraw amount");
			System.out.println("6. Print Transaction");
			System.out.println("7. Exit");			
			System.out.println("\nPlease Select an option:");
			int choice=sc.nextInt();
			
			switch (choice) {
			case 1:
				System.out.println("Enter Name: ");
				String name=sc.next();
				System.out.println("Enter Mobile Number: ");
				mobileNo=sc.next();
				System.out.println("Enter Balance");
				amount=sc.nextBigDecimal();				
				try {
					customer=walletService.createAccount(name, mobileNo, amount);
					if(customer!=null)
					{
						System.out.println("Your account is created!");
						System.out.println(customer);
					}
					else
						System.out.println("Account already exist!");
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					System.out.println("Something went wrong "+e1);
				}
				break;				
			case 2:				
				System.out.println("Enter the mobile number to view balance: ");
				try {
					mobileNo=sc.next();
					customer=walletService.showBalance(mobileNo);
					System.out.println("Your balance for mobile number: "+mobileNo+" is "+customer.getWallet());
				} catch (Exception e) {
					// TODO Auto-generated catch block
				System.out.println("Something went wrong "+e);
				}
				break;
			case 3:
				try {
					System.out.println("Enter Source Mobile Number: ");
					String sourceMobileNo=sc.next();
					System.out.println("Enter Target Mobile Number: ");
					String targetMobileNo=sc.next();
					System.out.println("Enter amount to transfer");
					amount=sc.nextBigDecimal();				
					customer=walletService.fundTransfer(sourceMobileNo, targetMobileNo, amount);
					System.out.println(customer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Something went wrong "+e);
				}
				break;
			case 4:
				try {
					System.out.println("Enter Mobile Number: ");
					mobileNo=sc.next();
					System.out.println("Enter amount to deposit");
					amount=sc.nextBigDecimal();				
					customer=walletService.depositAmount(mobileNo, amount);
					System.out.println(customer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Something went wrong "+e);
				}
				break;
			case 5:
				try {
					System.out.println("Enter Mobile Number: ");
					mobileNo=sc.next();
					System.out.println("Enter amount to withdraw");
					amount=sc.nextBigDecimal();				
					customer=walletService.withdrawAmount(mobileNo, amount);
					System.out.println(customer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Something went wrong "+e);
				}
				break;
			case 6:
				try {
					System.out.println("Enter mobile no to view print transaction");
					mobileNo=sc.next();
					List<Transaction> tList=walletService.selectTransaction(mobileNo);
					System.out.println(tList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Something went wrong. "+e);
				}
				break;
			case 7:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid options");
				break;
			}			
			System.out.println("\nDo you want to continue: Y/N ");
			ans=sc.next();
		}while(ans.equalsIgnoreCase("y"));
		System.out.println("\nThank you for using this service!");	
	}
	public static void main(String[] args) {
		Client client=new Client();
		client.menu();

	}

}
