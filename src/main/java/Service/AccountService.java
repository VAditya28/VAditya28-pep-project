package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService{
    AccountDAO accountDAO;

    public AccountService(){
         accountDAO= new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO= accountDAO;
    }

    // Creating a user/account service
    public Account CreateNewAccountService(Account account){

        if(account.getUsername()==null || account.getPassword()==null||account.getUsername().isBlank() || account.getPassword().length()<4){
            return null; // Checks username and password with conditions and returns null if not validated
        }
        if(accountDAO.UserExists(account.username)){
            return null;  // checks whether username exists or not and returns null if already exists
        }
        return accountDAO.CreateNewAccount(account);  //returns an account if all conditions pass
    }

    //Login service
    public Account LoginService(Account account){
        if(account.getUsername()==null || account.getPassword()==null){
            return null;    // Checks username and password with conditions and returns null if not validated
        }
        Account ExistingAcc= accountDAO.GetAccountByusername(account.getUsername());
        if(ExistingAcc==null){
            return null; // checks whether account exists or not and returns null if already exists
        }
        if(ExistingAcc.getPassword().equals(account.getPassword())){

                 return ExistingAcc;   // checks whether existed account's password match returns null if it not matches
           }
           return null;
        }
   
}
