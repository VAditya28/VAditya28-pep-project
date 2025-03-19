package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;
 

public class MessageDAO {


    List<Message> messages= new ArrayList<>();        // createing list globally so that methods that need this can consume

            // Get all messages
    
        public List<Message> getallmessages(){
            Connection connection= ConnectionUtil.getConnection();
        try {
            String sql= "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs= preparedStatement.executeQuery();
            System.out.println(rs);
            while (rs.next()){
            Message message = new Message
            (rs.getInt("message_id"), 
            rs.getInt("posted_by"), 
            rs.getString("message_text"), 
            rs.getLong("time_posted_epoch"));
            messages.add(message);
        }       
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
           return messages;      // returns all messages in a list 
    }

    // Get message by id 

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();      
        try {
          //  System.out.println("we are in dao");
            String sql= "Select * from message where message_id =?";
            PreparedStatement preparedStatement= connection.prepareStatement(sql); 
            preparedStatement.setInt(1, id);
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){
                 Message message = new Message
                 (rs.getInt("message_id"), 
                 rs.getInt("posted_by"),
                 rs.getString("message_text"), 
                 rs.getLong("time_posted_epoch"));              
                return message;          // returns fetched message
            }        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;           // fetch message by id fails
    }
   
    //retreive all messages by user
    public List<Message> getallmessagesofuser(int userid){
        Connection connection= ConnectionUtil.getConnection();     
        try {
            String sql= "select * from message where posted_by=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userid);
        ResultSet rs= preparedStatement.executeQuery();
        while (rs.next()){
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;              // returns a list of messages of a user
    }


    // Create a new message

    public Message CreateNewMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql= "insert into message(posted_by, message_text, time_posted_epoch) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet rSet= preparedStatement.getGeneratedKeys();
            if(rSet.next()){
                int generated_message_id= (int) rSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }    // retruns new inserted message
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());         
        }
        return null;         // insert fails
    }

    // update message text
    public int UpdateMessageById(int id, String  message){

        Connection connection= ConnectionUtil.getConnection();
        try {
            String sql= "update message set message_text= ? where message_id= ?";
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeUpdate();      // returns rows affected
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;        //update fails
    }
    
// delete message by message id
    public int  DeleteMessageByID(int id){
        Connection connection= ConnectionUtil.getConnection();     
        try {    
                String sql=" delete from message where message_id = ?";
                PreparedStatement preparedStatement= connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                int rows_affected =  preparedStatement.executeUpdate();
                return rows_affected;           // returns rows affected
        } catch (SQLException e) {
            System.out.println(e.getMessage());          
        }
        return 0;       // delete fails
    }   
}
