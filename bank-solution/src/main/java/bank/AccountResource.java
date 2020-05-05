package bank;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("accounts")
public class AccountResource {

    @Inject
    private AccountBean accountBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> listAccounts() {
        return accountBean.listAccounts();
    }
}
