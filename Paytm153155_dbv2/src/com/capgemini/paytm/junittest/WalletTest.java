package com.capgemini.paytm.junittest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.activity.InvalidActivityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.repo.WalletRepo;
import com.capgemini.paytm.repo.WalletRepoImpl;
import com.capgemini.paytm.service.WalletService;
import com.capgemini.paytm.service.WalletServiceImpl;
import com.capgemini.paytm.util.DBUtil;

import static org.junit.Assert.*;

public class WalletTest {

	WalletService service;
	Customer cust1, cust2, cust3,cust4,cust5;
	
	@Before
	public void initData() {
	
		service = new WalletServiceImpl();
	}	
	//for testing null value
	@Test(expected = NullPointerException.class)
	public void testCreateAccount() throws InvalidActivityException {
		service.createAccount(null, null, null);
		
	}	
	@Before
	public void testCreateAccount1()
	{
		cust1 = new Customer("Kalyani", "9900112212", new Wallet(new BigDecimal(9000)));
		cust2 = new Customer("Joey", "9963242422", new Wallet(new BigDecimal(6000)));
		cust3 = new Customer("Sejal", "9922950519", new Wallet(new BigDecimal(7000)));
		
		WalletRepo repo=new WalletRepoImpl();
		repo.save(cust1);
		repo.save(cust2);
		repo.save(cust3);
	}
	@Test(expected=Exception.class)
	public void testCreateAccount3() throws InvalidInputException 
	{
		service.createAccount("joey", "999", new BigDecimal(1500));
	}
	//for testing invalid account
	@Test(expected = InvalidInputException.class)
	public void testShowBalance() {
		Customer cust = new Customer();
		cust = service.showBalance("9579405744");

	}

	//for testing showBalance method 
	@Test
	public void testShowBalance2() {
		Customer cust = new Customer();
		cust = service.showBalance("9922950519");
		assertEquals(cust, cust3);
	}	

	@Test(expected = Exception.class)
	public void testFundTransfer() {
		service.fundTransfer(null, null, new BigDecimal(7000));
	}
	@Test(expected = Exception.class)
	public void testFundTransfer3() {
		service.fundTransfer("", null, new BigDecimal(7000));
	}
	@Test(expected = Exception.class)
	public void testFundTransfer4() {
		service.fundTransfer(null, "", new BigDecimal(7000));
	}
	@Test
	public void testFundTransfer2() {
		Customer c1 = service.fundTransfer("9900112212", "9963242422", new BigDecimal(20));
		BigDecimal actual = c1.getWallet().getBalance();
		
		Customer c2=service.showBalance("9963242422");
		BigDecimal expected = c2.getWallet().getBalance();
		
		assertEquals(expected, actual);
	}

	@Test(expected = Exception.class)
	public void testDeposit() {
		service.depositAmount("900000000", new BigDecimal(2000));
	}

	//for testing wrong number
	@Test(expected = InvalidInputException.class)
	public void testDeposit3() {
		service.depositAmount("", new BigDecimal(2000));
	}
	
	@Test(expected = InvalidInputException.class)
	public void testDepositNegativeBal() {
		service.depositAmount("8550952801", new BigDecimal(-99));
	}
	@Test
	public void testDeposit2() {
		Customer c1=new Customer();
		Customer c=new Customer();
		c1 = service.depositAmount("9963242422", new BigDecimal(2000));
		BigDecimal actual = c1.getWallet().getBalance();
		c=service.showBalance("9963242422");
		BigDecimal expected = c.getWallet().getBalance();
		assertEquals(expected, actual);
	}

	@Test(expected = Exception.class)
	public void testWithdraw() {
		service.withdrawAmount("900000000", new BigDecimal(2000));
	}
	@Test(expected = Exception.class)
	public void testWithdraw1() {
		service.withdrawAmount("", new BigDecimal(2000));
	}
	//for testing wrong number
	@Test(expected = Exception.class)
	public void testWithdraw3() {
		service.withdrawAmount(null, new BigDecimal(2000));
	}
	
	@Test(expected = InvalidInputException.class)
	public void testWithdrawNegativeBal() {
		service.depositAmount("8550952801", new BigDecimal(-99));
	}
	
	@Test
	public void testWithdraw2() {
		Customer c1=new Customer();
		Customer c=new Customer();
		c1 = service.withdrawAmount("9767309287", new BigDecimal(100));
		BigDecimal actual = c1.getWallet().getBalance();
		c=service.showBalance("9767309287");
		BigDecimal expected = c.getWallet().getBalance();
		assertEquals(expected, actual);
	}

	//for validating name and mobile number
	@Test
	public void TestValidate() {
		Customer customer=new Customer("Vaishnavi","9123456789",new Wallet(new BigDecimal(100)));
		service.validate(customer);		
	}

	@After
	public void testAfter() {
		service = null;
	}
	
	@After
	public void clean()
	{
		/*DBUtil dbutil=new DBUtil();
		try {
			Connection con=dbutil.getConnection();
			Statement stmt=con.createStatement();
			PreparedStatement pstmt=con.prepareStatement("Delete from Customer");
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
