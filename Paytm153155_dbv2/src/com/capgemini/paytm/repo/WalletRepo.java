package com.capgemini.paytm.repo;

import java.util.List;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;

public interface WalletRepo {

	public boolean save(Customer customer);	
	public Customer findOne(String mobileNo);
	public Customer Update(String mobileNo, Customer customer);
	public boolean saveTransaction(Transaction trans);
	public List<Transaction> selectTransaction(String mobileNo);
}
