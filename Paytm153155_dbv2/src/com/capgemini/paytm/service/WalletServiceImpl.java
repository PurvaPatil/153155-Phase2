package com.capgemini.paytm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activity.InvalidActivityException;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InsufficientBalanceException;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.repo.WalletRepo;
import com.capgemini.paytm.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {


public WalletRepo repo;
	
	public WalletServiceImpl(){
		repo= new WalletRepoImpl();
	}
	
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}
	WalletRepoImpl obj=new WalletRepoImpl();
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws InvalidInputException {		
		
		Customer cust=new Customer(name,mobileNo,new Wallet((amount)));
		validate(cust);
		boolean result=repo.save(cust);
		if(result==true)
			return cust;
		else
			return null;
				//create object of customer, and call dao save layer
		}

	
	public Customer showBalance(String mobileNo) {
		
		Customer customer=repo.findOne(mobileNo);		
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {	
		
		Customer scust=new Customer();
		Customer tcust=new Customer();
		Wallet sw=new Wallet();
		Wallet tw=new Wallet();
		Transaction trans1,trans2;
		scust=repo.findOne(sourceMobileNo);
		tcust=repo.findOne(targetMobileNo);
		if(scust!=null && tcust!=null)
		{	
			if(tcust!=scust) 
			{
				BigDecimal balance=scust.getWallet().getBalance();			
				if(balance.compareTo(amount)>=1 && amount.compareTo(BigDecimal.ZERO)>0)
				{
					BigDecimal diff=balance.subtract(amount);
					sw.setBalance(diff);
					scust.setWallet(sw);				
					BigDecimal sum=tcust.getWallet().getBalance().add(amount);			
					tw.setBalance(sum);
					tcust.setWallet(tw);
					repo.Update(targetMobileNo, tcust);
					repo.Update(sourceMobileNo, scust);
					String date=new Date().toString();
					trans1=new Transaction(sourceMobileNo, "FundTransfer(W)", "Success", amount, date);
					repo.saveTransaction(trans1);
					
					trans2=new Transaction(targetMobileNo, "FundTransfer(D)", "Success", amount, date);
					repo.saveTransaction(trans2);					
				}
				else
				{
					String date=new Date().toString();
					trans1=new Transaction(sourceMobileNo, "FundTransfer(W)", "Failure", amount, date);
					repo.saveTransaction(trans1);
					
					trans2=new Transaction(targetMobileNo, "FundTransfer(D)", "Failure", amount, date);
					repo.saveTransaction(trans2);
					throw new InsufficientBalanceException("Sorry amount can not be transfered. Insufficient balance!!");
				}
			}
			else
				throw new InvalidInputException("Both accounts cannot same!");
		}
		else
		{
			throw new InvalidInputException("Account does not exist!!");
		}		
		return tcust;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		Transaction trans;
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			if(amount.compareTo(BigDecimal.ZERO)>0)
			{
				BigDecimal amtAdd=cust.getWallet().getBalance().add(amount);
				wallet.setBalance(amtAdd);
				cust.setWallet(wallet);
				repo.Update(mobileNo, cust);
				trans=new Transaction(mobileNo, "Deposit", "Success", amount, new Date().toString());
				repo.saveTransaction(trans);				
			}
			else
				throw new InvalidInputException("Amount cannot be negative.");
		}
		else
		{			
			throw new InvalidInputException("Account does not exist!!");
		}	
		return cust;
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		cust=repo.findOne(mobileNo);
		Transaction trans;
		if(cust!=null)
		{
			BigDecimal balance=cust.getWallet().getBalance();
			BigDecimal amtSub;
			if(balance.compareTo(amount) >=1)
			{
				if(amount.compareTo(BigDecimal.ZERO)>0)
				{
					amtSub=balance.subtract(amount);
					wallet.setBalance(amtSub);
					cust.setWallet(wallet);
					repo.Update(mobileNo, cust);
					trans=new Transaction(mobileNo, "Withdraw", "Success", amount, new Date().toString());
					repo.saveTransaction(trans);					
				}
				else
					throw new InvalidInputException("Amount cannot be negative.");
			}
			else
			{
				trans=new Transaction(mobileNo, "Withdraw", "Failure", amount, new Date().toString());
				repo.saveTransaction(trans);
				throw new InsufficientBalanceException("Sorry amount can not be withdrawn. Insufficient balance!!");
			}
		}
		else
		{
			throw new InvalidInputException("Account does not exist!!");
		}	
		return cust;
	}
	
	public void validate(Customer customer) throws InvalidInputException {
		
			String phoneno=customer.getMobileNo();			
			Pattern pattern=Pattern.compile("(0/91)?[7-9][0-9]{9}");
			Matcher matcher=pattern.matcher(phoneno);
			if(matcher.matches())
				System.out.println("");
			else
			{				
				throw new InvalidInputException("Incorrect Mobile number!!");							
			}	
		
		
			if(validateName(customer.getName()))
				System.out.println("");
			else
			{				
				throw new InvalidInputException("Incorrect Name!!Please start name with capital letter");
			}			
		
		
	}
	private boolean validateName(String name) {
		
		String pattern="[A-Z][a-zA-Z]*";
		return name.matches(pattern)?true:false;
	}

	@Override
	public List<Transaction> selectTransaction(String mobileNo) {
		// TODO Auto-generated method stub
		return repo.selectTransaction(mobileNo);
	}
}
