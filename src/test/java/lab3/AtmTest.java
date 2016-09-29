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
	private Account account;
	private CashDispenser cashDispenser;
	private static final int ACCOUNTNUMBER = 1; 
	private static final double AMOUNT = 123;
	
	@Before
	public void initializeTest(){
		bank = mock(Bank.class);
		cashDispenser = mock(CashDispenser.class);
		account = mock(Account.class);
		willReturn(account).given(bank).findAccountByNumber(ACCOUNTNUMBER);
		atm = new Atm(bank,cashDispenser);
	}
	
	@Test
	public void givenAAccountNumberAndAmount_whenWantDebitAccount_thenShouldDrawMoney() {
		willReturn(account).given(bank).findAccountByNumber(ACCOUNTNUMBER);
		atm.withdrawMoney(ACCOUNTNUMBER, AMOUNT);
		
		verify(bank).findAccountByNumber(ACCOUNTNUMBER);
		verify(account).withDraw(AMOUNT);
	}
	
	@Test
	public void whenAccountWasDebited_shouldHaveMoneyWithCashDispenser(){
		atm.withdrawMoney(ACCOUNTNUMBER, AMOUNT);

		verify(cashDispenser).giveMoney(AMOUNT);
	}
	
	@Test(expected=RuntimeException.class)
	public void whenAccountHasEnoughtMoney_ThenShouldThrowException(){
		willThrow(new RuntimeException()).given(account).withDraw(AMOUNT);
		atm.withdrawMoney(ACCOUNTNUMBER, AMOUNT);
		
		
	}

}
