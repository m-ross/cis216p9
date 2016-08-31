package lab09;

import java.io.*;

public class Transaction {
	private char code;
	private String date, desc;
	private double amt;

	public void loadTransInfo(BufferedReader transFile) throws NumberFormatException, IndexOutOfBoundsException, IOException {
		code = transFile.readLine().charAt(0);
		date = transFile.readLine();
		desc = transFile.readLine();
		amt = Double.parseDouble(transFile.readLine());
	}

	public char getCode() {
		return code;
	}

	public String getDate() {
		return date;
	}

	public String getDesc() {
		return desc;
	}

	public double getAmt() {
		return amt;
	}
}