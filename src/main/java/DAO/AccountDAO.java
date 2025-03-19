package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // user registration or creating a new account
    public Account CreateNewAccount(Account account){
        Connection connection= ConnectionUtil.getConnection();
        try {
            String sql= "Insert into account (username, password) values(?,?)";
            PreparedStatement preparedStatement= connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2,account.getPassword());
           int rowsInserted=  preparedStatement.executeUpdate();
           // if any new rows are inserted then generates a key and assigns to the account object with other elements
           if(rowsInserted>0){
            ResultSet rs= preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generated_account_id=  rs.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

           }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // insertion fails
    }

     //Retreives all accounts for checking whether user already exist while creating a new account
    public List<Account> getallaccounts(){
        Connection connection= ConnectionUtil.getConnection();
        List<Account> accounts= new ArrayList<>();
        try {
            String sql= "select * from account";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs= preparedStatement.executeQuery();
        while (rs.next()){
             Account account = new Account(rs.getInt("account_id"),rs.getString("username"), rs.getString("password"));
             accounts.add(account);   // adds accounts to list until we do not have next row
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;    // retrieves all the account to a list
    }


     // retrieving by username 
     public boolean UserExists(String username){
        Connection connection = ConnectionUtil.getConnection();
        try { 
            String sql= "Select * from account where username =?";
            PreparedStatement preparedStatement= connection.prepareStatement(sql); 
            preparedStatement.setString(1,username);
            ResultSet rs= preparedStatement.executeQuery();
            return rs.next(); // returns true if account is fetched
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    

    // retrieving a single account by id
    public Account GetAccountById(int id){
        Account account= new Account();
        Connection connection = ConnectionUtil.getConnection();
        try { 
            String sql= "Select * from account where account_id =?";
            PreparedStatement preparedStatement= connection.prepareStatement(sql); 
            preparedStatement.setInt(1, account.getAccount_id());
            ResultSet rs= preparedStatement.executeQuery();
            while (rs.next()){
                 account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;     // returns an account if found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;   // fails to find an account
    }

    // retrieving by username 
    public Account GetAccountByusername(String username){
        Account account= new Account();
        Connection connection = ConnectionUtil.getConnection();
        try { 
            String sql= "Select * from account where username =?";
            PreparedStatement preparedStatement= connection.prepareStatement(sql); 
            preparedStatement.setString(1,username);
            ResultSet rs= preparedStatement.executeQuery();
            while (rs.next()){
                 account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;   //returns account with the passed username 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;  // fails if account not found
    }
}
