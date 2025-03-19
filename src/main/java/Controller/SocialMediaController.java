package Controller;
import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import com.fasterxml.jackson.core.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
         accountService= new AccountService();
         messageService= new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this:: PostRegisterHandler);
        app.post("/login", this::PostLoginHandler);
        app.post("/messages", this:: PostMessageHandler);
        app.get("/messages", this::GetallMessagesHandler);
        app.get("/messages/{message_id}", this::GetMessagebyIdHandler);
        app.get("/accounts/{account_id}/messages", this:: GetMessageByUser);
        app.delete("/messages/{message_id}", this::DeleteByIdHandler);
        app.patch("/messages/{message_id}", this::UpdateMessagebyIdHandler);
        


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */


     //Handler to create a new registration
    private void PostRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper obm= new ObjectMapper();
        Account account= obm.readValue(context.body(), Account.class);  
        Account newAccount= accountService.CreateNewAccountService(account);
        //System.out.println(newAccount);
        if(newAccount==null){
            context.status(400);  // bad request
        }
        else{
            context.json(newAccount).status(200);      // ok          
       
        }
    }
       


    //Handler to create a login 
    private void PostLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper obm = new ObjectMapper();
        Account account= obm.readValue(context.body(), Account.class);
        Account LoggedIn= accountService.LoginService(account);
       // System.out.println("actual: "+ LoggedIn);
        if(LoggedIn==null){
            context.status(401);  // unauthorized
        }
        else{
            context.status(200).json(LoggedIn);   //ok
        }
    }

    //Handler to create a new message
    private void PostMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper obm= new ObjectMapper();
        Message message= obm.readValue(context.body(), Message.class);  
        Message newmessage= messageService.CreateMessage(message);
        //System.out.println(newmessage);       
        if( newmessage!=null){
            context.json(obm.writeValueAsString(newmessage));        // ok
        }
        else{
            context.status(400);        // bad request
        } 
    }



    // Handler to retrieve all the messages
    private void GetallMessagesHandler(Context context)  {
        System.out.println("We are in hanlder");
        List<Message> messages=messageService.GetallMessages();
        context.json(messages);   // ok
    }


    //Handler to retrieve messages by id
    private void GetMessagebyIdHandler(Context context) {
        System.out.println("we are in handler");
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message= messageService.GetMessagebyId(messageId);
        System.out.println(message);
            //context.status(400);
            if(message!=null){
                context.status(200).json(message);  //ok
            }       
        }

    //Handler to retrieve message of a user
    private void GetMessageByUser(Context context) {
        int User_id= Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getallMessagesOfaUser(User_id));   //ok
    }



     // Handler to delete a message
    private void DeleteByIdHandler(Context context){
      int IdtoDelete= Integer.parseInt(context.pathParam("message_id"));
      Message messagedeleted= messageService.DeleteMessageByID(IdtoDelete);  
        if(messagedeleted==null){
            context.status(200);   //ok by default but no message  
        }  
        else{
            context.status(200).json(messagedeleted);    // ok
        }        
    }
    
     // Handler to update a message
    private void UpdateMessagebyIdHandler(Context context) throws JsonProcessingException  {
        ObjectMapper obm = new ObjectMapper();
        int IdtoUpdate= Integer.parseInt(context.pathParam("message_id"));
        String requestString= context.body();
        String message= obm.readTree(requestString).get("message_text").asText();
        Message Updatedmessage= messageService.UpdateMessageById(IdtoUpdate, message);
        System.out.println("Update :" +Updatedmessage);
        if(Updatedmessage!=null){
            context.status(200).json(Updatedmessage);   //ok
         } 
         else{
            context.status(400);// bad request
   
         }
    }
}