package com.capgemini.paytm.beans;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {

	
	private String transaction_type,transaction_status,mobileNo;
	private BigDecimal transaction_amt;
	private String date;
	
	public Transaction(String mobileNo, String transaction_type, String transaction_status, BigDecimal transaction_amt,
			String date) {
		super();
		this.mobileNo = mobileNo;
		this.transaction_type = transaction_type;
		this.transaction_status = transaction_status;
		this.transaction_amt = transaction_amt;
		this.date = date;
	}
	public Transaction() {
		// TODO Auto-generated constructor stub
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	public BigDecimal getTransaction_amt() {
		return transaction_amt;
	}
	public void setTransaction_amt(BigDecimal transaction_amt) {
		this.transaction_amt = transaction_amt;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Transaction [mobileNo=" + mobileNo + ", transaction_type=" + transaction_type + ", transaction_status="
				+ transaction_status + ", transaction_amt=" + transaction_amt + ", date=" + date + "]\n";
	} 
	
}
