package lab3;

public class Atm{
	
	private Bank bank;
	private CashDispenser cashDispenser;
	
	public Atm(Bank bank,CashDispenser cashDispenser){
		this.bank = bank;
		this.cashDispenser = cashDispenser;
	}
	
	public void withdrawMoney(int accountNumber, double amount){
		Account account = bank.findAccountByNumber(accountNumber);
		account.withDraw(amount);
		cashDispenser.giveMoney(amount);
	}

	public void transferMoney(int fromAccountNumber, int targetAccountNumber, double amount) {
		Account fromAccount = bank.findAccountByNumber(fromAccountNumber);
		Account targetAccount = bank.findAccountByNumber(targetAccountNumber);
		fromAccount.withDraw(amount);
		targetAccount.addAmount(amount);
		
	}

}
