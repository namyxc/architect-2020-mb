package bank;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AccountBean {

    @Inject
    private AccountRepository accountRepository;

    public List<Account> listAccounts() {
        return accountRepository.findAll();
    }



    @Transactional
    public Account credit(long accountId, long amount) throws IllegalCreditException {
        var account = accountRepository.findBy(accountId);
        var newBalance = account.getBalance() + amount;
        if (newBalance < 0){
            throw new IllegalCreditException();
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
        return account;
    }
}
