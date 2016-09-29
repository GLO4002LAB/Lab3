package lab3;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AtmTest {
	
	private Atm atm;
	private Bank bank;
	private Account account, fromAccount, targetAccount;
	private CashDispenser cashDispenser;
	private static final int ACCOUNT_NUMBER = 1; 
	private static final int FROM_ACCOUNT_NUMBER = 2; 
	private static final int TARGET_ACCOUNT_NUMBER = 3; 
	private static final double AMOUNT = 123;
	
	@Before
	public void initializeTest(){
		bank = mock(Bank.class);
		cashDispenser = mock(CashDispenser.class);
		account = mock(Account.class);
		fromAccount = mock(Account.class);
		targetAccount = mock(Account.class);
		willReturn(account).given(bank).findAccountByNumber(ACCOUNT_NUMBER);
		willReturn(targetAccount).given(bank).findAccountByNumber(TARGET_ACCOUNT_NUMBER);
		willReturn(fromAccount).given(bank).findAccountByNumber(FROM_ACCOUNT_NUMBER);
		atm = new Atm(bank,cashDispenser);
	}
	
	@Test
	public void givenAnAccountNumberAndAmount_whenWantDebitAccount_thenShouldDrawMoney() {
		atm.withdrawMoney(ACCOUNT_NUMBER, AMOUNT);
		
		verify(bank).findAccountByNumber(ACCOUNT_NUMBER);
		verify(account).withDraw(AMOUNT);
	}
	
	@Test
	public void whenAccountWasDebited_thenShouldHaveMoneyWithCashDispenser(){
		atm.withdrawMoney(ACCOUNT_NUMBER, AMOUNT);

		verify(cashDispenser).giveMoney(AMOUNT);
	}
	
	@Test(expected=RuntimeException.class)
	public void whenAccountHasEnoughtMoney_thenShouldThrowRuntimeException(){
		willThrow(new RuntimeException()).given(account).withDraw(AMOUNT);
		atm.withdrawMoney(ACCOUNT_NUMBER, AMOUNT);
	}
	
	@Test
	public void whenAccountHasEnoughtMoney_thenShouldNotGiveMoneyByCashDispenser(){
		willThrow(new RuntimeException()).given(account).withDraw(AMOUNT);
		try{
			atm.withdrawMoney(ACCOUNT_NUMBER, AMOUNT);
		}
		catch(RuntimeException re){}
		
		verify(cashDispenser,Mockito.never()).giveMoney(AMOUNT);
	}
	
	@Test
	public void whenTransferedAmountFromAccountToTargetAccount_thenShouldHaveMoneyInTargetAccount(){
		atm.transferMoney(FROM_ACCOUNT_NUMBER,TARGET_ACCOUNT_NUMBER,AMOUNT);
		
		verify(bank).findAccountByNumber(FROM_ACCOUNT_NUMBER);
		verify(bank).findAccountByNumber(TARGET_ACCOUNT_NUMBER);
		verify(targetAccount).addAmount(AMOUNT);
		verify(fromAccount).withDraw(AMOUNT);
	}

	@Test(expected=RuntimeException.class)
	public void whenFromAccountDoesNotEnoughtMoney_thenShouldThrowRuntimeException(){
		willThrow(new RuntimeException()).given(fromAccount).withDraw(AMOUNT);
		atm.transferMoney(FROM_ACCOUNT_NUMBER,TARGET_ACCOUNT_NUMBER,AMOUNT);
	}
	
	@Test
	public void whenFromAccountDoesNotEnoughtMoney_thenShouldNotTransferAmount(){
		willThrow(new RuntimeException()).given(fromAccount).withDraw(AMOUNT);
		
		try{
			atm.transferMoney(FROM_ACCOUNT_NUMBER,TARGET_ACCOUNT_NUMBER,AMOUNT);
		}
		catch(RuntimeException re){}
		
		verify(targetAccount,Mockito.never()).addAmount(AMOUNT);
	}
}
