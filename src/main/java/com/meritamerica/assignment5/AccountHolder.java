package com.meritamerica.assignment5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
@Entity
@Table(name="accountholder")
public class AccountHolder implements Comparable<AccountHolder> {

	private static int nextId = 1;
	
	@Column(unique = true)
	private int id;
	@NotBlank
	private String firstName;
	private String middleName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String ssn;
	@OneToMany(mappedBy = "accountholder")
	CheckingAccount[] checkingArray = new CheckingAccount[0];
	@OneToMany(mappedBy = "accountholder")
	SavingsAccount[] savingsArray = new SavingsAccount[0];
	@OneToMany(mappedBy = "accountholder")
	CDAccount[] cdAccountArray = new CDAccount[0];

	public AccountHolder() {
		this.id = nextId++;
		this.firstName = "";
		this.middleName = "";
		this.lastName = "";
		this.ssn = "";
	}

	public AccountHolder(String first, String middle, String last, String ssn) {
		this.id = nextId++;
		this.firstName = first;
		this.middleName = middle;
		this.lastName = last;
		this.ssn = ssn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CheckingAccount addCheckingAccount(double openBalance) throws ExceedsCombinedBalanceLimitException {
		if (getCheckingBalance() + getSavingsBalance() + openBalance >= 250000) {
			System.out.println("Cannot open a new Checking Account because aggregate balance of accounts is to high.");
			return null;
		}
		CheckingAccount newA = new CheckingAccount(openBalance, CheckingAccount.INTEREST_RATE);
		CheckingAccount[] newCheckingArray = new CheckingAccount[checkingArray.length + 1];
		for (int i = 0; i < newCheckingArray.length - 1; i++) {
			newCheckingArray[i] = checkingArray[i];
		}
		checkingArray = newCheckingArray;
		checkingArray[checkingArray.length - 1] = newA;
		return newA;
	}

	public CheckingAccount addCheckingAccount(CheckingAccount checkingAccount)
			throws ExceedsCombinedBalanceLimitException {
		try {
			if (checkingAccount.getBalance() + getCheckingBalance() + getSavingsBalance() >= 250000) {
				System.out.println(
						"Cannot open a new Checking Account because aggregate balance of accounts is to high.");
				return null;
			}
			CheckingAccount[] newCheckingArray = new CheckingAccount[checkingArray.length + 1];
			for (int i = 0; i < newCheckingArray.length - 1; i++) {
				newCheckingArray[i] = checkingArray[i];
			}
			checkingArray = newCheckingArray;
			checkingArray[checkingArray.length - 1] = checkingAccount;
			return checkingAccount;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	public double getCheckingBalance() {
		double total = 0.0;
		int i;
		for (i = 0; i < checkingArray.length; i++) {
			total += checkingArray[i].getBalance();
		}
		return total;
	}

	public SavingsAccount addSavingsAccount(double openBalance) throws ExceedsCombinedBalanceLimitException {
		if (getCheckingBalance() + getSavingsBalance() + openBalance >= 250000) {
			System.out.println("Cannot open a new Savings Account because aggregate balance of accounts is to high.");
			return null;
		}
		SavingsAccount newA = new SavingsAccount(openBalance, SavingsAccount.INTEREST_RATE);
		SavingsAccount[] newSavingsArray = new SavingsAccount[savingsArray.length + 1];
		for (int i = 0; i < newSavingsArray.length - 1; i++) {
			newSavingsArray[i] = savingsArray[i];
		}
		savingsArray = newSavingsArray;
		savingsArray[savingsArray.length - 1] = newA;
		return newA;
	}

	public SavingsAccount addSavingsAccount(SavingsAccount savingsAccount) throws ExceedsCombinedBalanceLimitException {
		if (savingsAccount.getBalance() + getCheckingBalance() + getSavingsBalance() >= 250000) {
			System.out.println("Cannot open a new Savings Account because aggregate balance of accounts is to high.");
			return null;
		}
		SavingsAccount[] newSavingsArray = new SavingsAccount[savingsArray.length + 1];
		for (int i = 0; i < newSavingsArray.length - 1; i++) {
			newSavingsArray[i] = savingsArray[i];
		}
		savingsArray = newSavingsArray;
		savingsArray[savingsArray.length - 1] = savingsAccount;
		return savingsAccount;
	}

	public SavingsAccount[] getSavingsAccounts() {
		return savingsArray;
	}

	public int getNumberOfSavingsAccounts() {
		return savingsArray.length;
	}

	public double getSavingsBalance() {
		double total = 0.0;
		for (SavingsAccount balance : savingsArray) {
			total += balance.getBalance();
		}
		return total;

	}

	public CDAccount addCDAccount(CDOffering offering, double openBalance) {
		CDAccount newA = new CDAccount(offering, openBalance);
		CDAccount[] newCDArray = new CDAccount[cdAccountArray.length + 1];
		for (int i = 0; i < newCDArray.length - 1; i++) {
			newCDArray[i] = cdAccountArray[i];
		}
		cdAccountArray = newCDArray;
		cdAccountArray[cdAccountArray.length - 1] = newA;
		return newA;
	}

	public CDAccount addCDAccount(CDAccount cdAccount) {
		CDAccount[] newCDArray = new CDAccount[cdAccountArray.length + 1];
		for (int i = 0; i < newCDArray.length - 1; i++) {
			newCDArray[i] = cdAccountArray[i];
		}
		cdAccountArray = newCDArray;
		cdAccountArray[cdAccountArray.length - 1] = cdAccount;
		return cdAccount;
	}

	public CDAccount[] getCDAccounts() {
		return cdAccountArray;
	}

	public int getNumberOfCDAccounts() {
		return cdAccountArray.length;
	}

	public double getCDBalance() {
		double total = 0.0;
		for (CDAccount balance : cdAccountArray) {
			total += balance.getBalance();
		}
		return total;
	}

	public double getCombinedBalance() {
		return getCDBalance() + getSavingsBalance() + getCheckingBalance();
	}

	@Override
	public int compareTo(AccountHolder account) {
		if (this.getCombinedBalance() > account.getCombinedBalance()) {
			return 1;
		} else {
			return -1;
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String first) {
		this.firstName = first;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middle) {
		this.middleName = middle;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last) {
		this.lastName = last;
	}

	public String getSSN() {
		return ssn;
	}

	public void setSSN(String ssn) {
		this.ssn = ssn;
	}

	public CheckingAccount[] getCheckingAccounts() {
		return checkingArray;
	}

	public int getNumberOfCheckingAccounts() {
		return checkingArray.length;
	}

	public String writeToString() {
		StringBuilder accountHolderData = new StringBuilder();
		accountHolderData.append(firstName).append(",");
		accountHolderData.append(middleName).append(",");
		accountHolderData.append(lastName).append(",");
		accountHolderData.append(ssn);
		return accountHolderData.toString();
	}

	public static AccountHolder readFromString(String accountHolderData) {
		String[] holding = accountHolderData.split(",");
		String firstName = holding[0];
		String middleName = holding[1];
		String lastName = holding[2];
		String ssn = holding[3];
		return new AccountHolder(firstName, middleName, lastName, ssn);
	}

	public String toString() {
		return "Combined Balance for Account Holder" + this.getCombinedBalance();
	}

}
