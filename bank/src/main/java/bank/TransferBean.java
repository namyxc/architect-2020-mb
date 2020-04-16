package bank;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransferBean {

    @Inject
    private TransferRepository transferRepository;

    @EJB
    private AccountBean accountBean;

    @Transactional(rollbackOn = IllegalCreditException.class)
    public Transfer createTransfer(CreateTransferCommand command) throws IllegalCreditException {
        var transfer = new Transfer();
        transfer.setSrc(command.getSrc());
        transfer.setDest(command.getDest());
        transfer.setAmount(command.getAmount());
        var created = transferRepository.save(transfer);

        accountBean.credit(command.getSrc(), -command.getAmount());
        accountBean.credit(command.getDest(), command.getAmount());

        created.setResult("ok");
        return created;

    }

    public List<Transfer> listTransfers() {
        return transferRepository.findAll();
    }
}
