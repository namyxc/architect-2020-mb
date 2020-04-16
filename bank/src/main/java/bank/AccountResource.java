package bank;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
