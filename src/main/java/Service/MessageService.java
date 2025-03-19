package Service;
import java.util.List;
import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

public class MessageService {

    MessageDAO messageDAO;
    AccountDAO accountDAO;
    Message message;
        
    public MessageService(){
         messageDAO= new MessageDAO();
         accountDAO= new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO= messageDAO;
    }

    // Adding or creating a new message based on the conditions
    public Message CreateMessage(Message message){
    
        if(message.message_text.isBlank()==false  && message.message_text.length()<255 ){
           return messageDAO.CreateNewMessage(message);  
        }
        return null;   // returns null if message is blank or length> 255
    }

    // Retrieving all messages
    public List<Message> GetallMessages(){
        return messageDAO.getallmessages();          // returns list of messages
    }
    

    //Retrieve a message by its Id
    public Message GetMessagebyId(int id){
     return messageDAO.getMessageById(id);            // returns message by id
    }

    //Update a message by Id using given conditions
    public Message UpdateMessageById(int id, String message){
         if(message==null|| message.isBlank()  || message.length()>255 ){
               return null;            // validation fails
         }
         Message existingmessage= messageDAO.getMessageById(id);
         if(existingmessage==null){      
            return null;  // returns null if message doesnt exist
         }
        int rowsUpdated= messageDAO.UpdateMessageById(id, message);
        if(rowsUpdated>0){
             return messageDAO.getMessageById(id);       // returns updated message
        }
        return null;        // update fails
    }

    // retrieve all messages of a user
    public List<Message> getallMessagesOfaUser(int user_id){
        return messageDAO.getallmessagesofuser(user_id);          // retruns list of messages of a user
    }


    //Delete message 
    public Message DeleteMessageByID(int messageid){
       Message message= messageDAO.getMessageById(messageid);
       if(message==null){
        return null;           // retruns null if message not found
       }
       else{
        int rows_affected= messageDAO.DeleteMessageByID(messageid);
        if(rows_affected>0){         // returns the message if rows are affected
            return message;
        }
        return null;
       }
           
        

    }
}
