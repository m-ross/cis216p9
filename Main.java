/*	Program Name:	Lab 09 Checking Account Utility
	Programmer:		Marcus Ross
	Date Due:		15 Nov 2013
	Description:	This program prompts the user for the name of a file containing a customer's information, including the name of a file containing information on their transactions. The program will not proceed until a proper customer file is found. The program ends early only if the user closes the file name prompt, the transaction file has unexpected data, it is not found, or if updating either file is unsuccessful.	*/

package lab09;

import stuff.MyClass;
import lab09.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;

public class Main {
	public static void main(String args[]) {
		Customer cust;
		String custFileN;
		String text;

		while (true) {
			custFileN = GetCustFileN();
			try {
				cust = new Customer(custFileN);
				break;
			} catch (FileNotFoundException e) {
				MyClass.errorDialog("Customer file not found.");
			} catch (IOException e) {
				MyClass.errorDialog("Failed to read customer file.");
			} catch (NumberFormatException e) {
				MyClass.errorDialog("Customer file has incorrect format.");
			}
		}

		text = ShowRptHead(cust);

		try {
			text = ProcessTrans(cust,text);
		} catch (FileNotFoundException e) {
			MyClass.errorDialog("Transaction file not found.");
			System.exit(1);
		} catch (IOException e) {
			MyClass.errorDialog("Failed to read transaction file.");
			System.exit(2);
		}

		text = ShowEndBal(cust,text);
		ShowAcctSumm(text);

		try {
			RewriteFiles(cust, custFileN);
		} catch (IOException e) {
			MyClass.errorDialog("I/O failed.");
			System.exit(3);
		}
	}

	public static String GetCustFileN() { //prompt for file name
		String custFileN;
		while (true) {
			custFileN = JOptionPane.showInputDialog(null, "Customer Information File Name", "Checking Account Utility", JOptionPane.DEFAULT_OPTION);
			if (custFileN == null) { //end program if dialog is closed
				System.exit(0);
			} else
				if (!custFileN.isEmpty()) //if dialog not closed and input not empty, return
					return custFileN;
			MyClass.errorDialog("Please enter a file name.");
		}
	}

	public static String ShowRptHead(Customer cust) { //return formatted customer information
		String rptHead = String.format("%-10s%s\n%10s%s\n%10s%s\n\n%-18s%.2f\n%-18s%s\n\n%-12s%-16s%12s%12s\n","Customer:",cust.getName(),"",cust.getAddr(),"",cust.getCSZ(),"Initial Balance:",cust.getBalance(),"Transaction file:",cust.getTransFileN(),"DATE","DESCRIPTION","AMOUNT","BALANCE");
		return rptHead;
	}

	public static String ProcessTrans(Customer cust, String text) throws FileNotFoundException, IOException { //get transactions infos and add them to string
		Transaction trans = new Transaction();
		BufferedReader transFile = new BufferedReader(new FileReader(cust.getTransFileN()));
		while (transFile.ready()) {
			trans.loadTransInfo(transFile);
			cust.modBalance(trans);
			text += ShowTransSumm(trans,cust);
		}
		return text;
	}

	public static String ShowTransSumm(Transaction trans, Customer cust) { //return formatted transaction info
		String transSumm = String.format("%-12s%-16s%12.2f%12.2f\n",trans.getDate(),trans.getDesc(),trans.getAmt(),cust.getBalance());
		return transSumm;
	}

	public static String ShowEndBal(Customer cust, String text) { //return formatted end balance
		text += String.format("%40s%12.2f","Final balance:",cust.getBalance());
		return text;
	}

	public static void ShowAcctSumm(String text) { //show formatted account summary in dialog
		JTextArea summText = new JTextArea(text);
		summText.setFont(new Font("Lucida Console", Font.PLAIN, 12));
		summText.setEditable(false);
		JScrollPane summScroll = new JScrollPane(summText);
		summScroll.setPreferredSize(new Dimension(403,227));
		JOptionPane.showMessageDialog(null,summScroll,"Checking Account Transaction Summary",JOptionPane.PLAIN_MESSAGE);
	}

	public static void RewriteFiles(Customer cust, String custFileN) throws IOException { //updates balance in customer file and rewrites transaction file to be blank
		BufferedWriter custFile = new BufferedWriter(new FileWriter(custFileN));
		// BufferedWriter custFile = new BufferedWriter(new FileWriter("c.txt")); // testing
		custFile.write(String.format("%s\n%s\n%s\n%.2f\n%s\n",cust.getName(),cust.getAddr(),cust.getCSZ(),cust.getBalance(),cust.getTransFileN()));
		custFile.close();
		FileWriter transFile = new FileWriter(cust.getTransFileN());
		// FileWriter transFile = new FileWriter("t.txt"); // testing
		transFile.write("");
		transFile.close();
	}
}