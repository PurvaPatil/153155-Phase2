package com.capgemini.paytm.repo;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.util.DBUtil;

public class WalletRepoImpl implements WalletRepo{

	DBUtil dbutil=new DBUtil();
	
	public WalletRepoImpl() {
		
	}
	@Override
	public boolean save(Customer customer) {
		
			
		try(Connection con=dbutil.getConnection())
		{
			Statement stmt=con.createStatement();
			PreparedStatement pstmt=con.prepareStatement("Insert into Customer values(?,?,?)");
			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getMobileNo());
			float balance=customer.getWallet().getBalance().floatValue();
			pstmt.setFloat(3, balance);
			pstmt.execute();
			return true;
			
		} catch (SQLException e) {
			
			System.out.println("Sql exception "+e);			
		}
		return false;
	}

	@Override
	public Customer findOne(String mobileNo) {		
		Customer customer=new Customer();
		try(Connection con=dbutil.getConnection())
		{
			PreparedStatement pstmt=con.prepareStatement("select * from Customer where mobileNo=?");
			pstmt.setString(1, mobileNo);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()==false)
			{
				throw new InvalidInputException("Account does not exist!");				
			}
			customer.setName(rs.getString(1));
			customer.setMobileNo(rs.getString(2));
			BigDecimal balance=new BigDecimal(rs.getFloat(3));
			Wallet wallet =new Wallet();
			wallet.setBalance(balance);
			customer.setWallet(wallet);		
		}
		catch(SQLException e)
		{
			System.out.println("Sql exception "+e);	
		}
		return customer;
	}
	@Override
	public Customer Update(String mobileNo, Customer customer)
	{
		try(Connection con=dbutil.getConnection())
		{
			PreparedStatement pstmt=con.prepareStatement("Update Customer set wallet=? where mobileNo=?");
			pstmt.setString(2, mobileNo);
			float bal=customer.getWallet().getBalance().floatValue();
			pstmt.setFloat(1, bal);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()==false)
				throw new InvalidInputException("Account does not exist");
			return customer;
		}			
		catch(SQLException e)
		{
			System.out.println("Sql exception "+e);	
		}
		return null;
	}
	@Override
	public boolean saveTransaction(Transaction trans) {
		
		
		try(Connection con=dbutil.getConnection())
		{
			Statement stmt=con.createStatement();
			PreparedStatement pstmt=con.prepareStatement("Insert into transaction values(?,?,?,?,?)");
			pstmt.setString(1,trans.getMobileNo() );
			pstmt.setString(2, trans.getTransaction_type());
			pstmt.setString(4, trans.getTransaction_status());
			pstmt.setString(5, trans.getDate().toString());
			float balance=trans.getTransaction_amt().floatValue();
			pstmt.setFloat(3, balance);
			pstmt.execute();
			return true;
			
		} catch (SQLException e) {
			
			System.out.println("Sql exception "+e);			
		}
		return false;
	}
	@Override
	public List<Transaction> selectTransaction(String mobileNo) {		
	
		List<Transaction> list=new ArrayList<>();
		try(Connection con=dbutil.getConnection())
		{		
			PreparedStatement pstmt=con.prepareStatement("select * from transaction where mobileNo=?");
			pstmt.setString(1, mobileNo);
			ResultSet rs=pstmt.executeQuery();	
			if(rs.next()==false)
				throw new InvalidInputException("No transaction details are found.");
			while(rs.next()) 
			{				
				Transaction trans1=new Transaction();
				trans1.setMobileNo(mobileNo);			
				trans1.setTransaction_type(rs.getString(2));
				BigDecimal balance1=new BigDecimal(rs.getFloat(3));
				trans1.setTransaction_amt(balance1);
				trans1.setTransaction_status(rs.getString(4));		
				trans1.setDate(rs.getString(5));					
				list.add(trans1);				
			}			
		}
		catch(SQLException e)
		{
			System.out.println("Sql exception "+e);	
		}
		return list;
	}
}
