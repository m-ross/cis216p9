package lab09;

import lab09.Transaction;
import java.io.*;

public class Customer {
	private String name, address, cityStateZip, transFileN;
	private double balance;

	public Customer(String fileName) throws FileNotFoundException, IOException, NumberFormatException {
		BufferedReader custFile = new BufferedReader(new FileReader(fileName));
		name = custFile.readLine();
		address = custFile.readLine();
		cityStateZip = custFile.readLine();
		balance = Double.parseDouble(custFile.readLine());
		transFileN = custFile.readLine();
		custFile.close();
	}

	public String getName() {
		return name;
	}

	public String getAddr() {
		return address;
	}

	public String getCSZ() {
		return cityStateZip;
	}

	public double getBalance() {
		return balance;
	}

	public String getTransFileN() {
		return transFileN;
	}

	public void modBalance(Transaction trans) {
		if (trans.getCode()=='C' || trans.getCode()=='W' || trans.getCode()=='T')
			balance -= trans.getAmt();
		else
			balance += trans.getAmt();
	}
}