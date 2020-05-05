package bank;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class AccountBean {

    @Inject
    private AccountRepository accountRepository;

    public List<Account> listAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void credit(long accountId, long amount) throws IllegalCreditException {
        var account = accountRepository.findBy(accountId);
        var newBalance = account.getBalance() + amount;
        if (newBalance < 0) {
            throw new IllegalCreditException("Result would be:" + newBalance);
        }
        account.setBalance(newBalance);
    }
}
