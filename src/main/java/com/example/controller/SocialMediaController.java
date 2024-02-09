package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

//   ## 1: Our API should be able to process new User registrations.

//   As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register.
//  The body will contain a representation of a JSON Account, but will not contain an account_id.

    @PostMapping("register")
    public ResponseEntity<Account> createNewAccount(@RequestBody Account newAccount ){
        return this.accountService.createAccount(newAccount);
    }

    // ## 2: Our API should be able to process User logins.

    // As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
    // The request body will contain a JSON representation of an Account.

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        return this.accountService.login(account);
    }

    // ## 3: Our API should be able to process the creation of new messages.

    // As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
    // 
    // The request body will contain a JSON representation of a message, which should be persisted to 
    // the database, but will not contain a message_id.
    // - The creation of the message will be successful if and only if the message_text is not blank, 
    // is not over 255 characters, and posted_by refers to a real, existing user. If successful, the 
    // response body should contain a JSON of the message, including its message_id. The response status 
    // should be 200, which is the default. The new message should be persisted to the database.
    // - If the creation of the message is not successful, the response status should be 400. (Client error)
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Optional<Account> optional = this.accountService.findById(message.getPosted_by());
        String messageText = message.getMessage_text();
        int messageLength = messageText.length();
        if (!optional.isEmpty() && messageText != null){
            if(messageLength <= 255 && !messageText.isBlank()){
                Message createdMessage = this.messageService.createMessage(message);
                return ResponseEntity.status(200).body(createdMessage);
            }
        }
        return ResponseEntity.status(400).body(message);
        }

    //     ## 4: Our API should be able to retrieve all messages.

    // As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

    // - The response body should contain a JSON representation of a list containing all messages retrieved
    //  from the database. It is expected for the list to simply be empty if there are no messages. 
    // The response status should always be 200, which is the default.
        @GetMapping("messages")
        public ResponseEntity<List<Message>> getMessages(){
            List<Message> messageList = this.messageService.findAllMessages();
            return ResponseEntity.status(200).body(messageList);
        }

    // ## 5: Our API should be able to retrieve a message by its ID.

    // As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

    // - The response body should contain a JSON representation of the message identified by the message_id. 
    // It is expected for the response body to simply be empty if there is no such message. 
    // The response status should always be 200, which is the default.
    @GetMapping("messages/{message_id}")
        public ResponseEntity<Message> getMessageById(@PathVariable int message_id){
            Optional<Message> message = this.messageService.findMessageById(message_id);
            if(message.isEmpty()){
                return ResponseEntity.status(200).body(null);
            }
            return ResponseEntity.status(200).body(message.get());
        }


    //     ## 6: Our API should be able to delete a message identified by a message ID.

    // As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

    // - The deletion of an existing message should remove an existing message from the database. If the message existed, 
    //  the response body should contain the number of rows updated (1). The response status should be 200, which is the
    //  default.
    // - If the message did not exist, the response status should be 200, but the response body should be empty.
    //  This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint 
    // should respond with the same type of response.
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Integer> deleteMesssage(@PathVariable int message_id){
        int rowsDeleted = this.messageService.deleteMessage(message_id);
        if(rowsDeleted == 1){
            return ResponseEntity.status(200).body(rowsDeleted);
        }
        return ResponseEntity.status(200).body(null);
    }


    // ## 7: Our API should be able to update a message text identified by a message ID.

    // As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
    // The request body should contain a new message_text values to replace the message identified by message_id. 
    // The request body can not be guaranteed to contain any other information.

    // - The update of a message should be successful if and only if the message id already exists and the new message_text
    //  is not blank and is not over 255 characters. If the update is successful, the response body should contain 
    // the number of rows updated (1), and the response status should be 200, which is the default. The message existing 
    // on the database should have the updated message_text.
    // - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int message_id, @RequestBody Message message_text){
        String text = message_text.getMessage_text();
        if(!text.isBlank() && text.length() <= 255){
            int updated_message = this.messageService.updateMessageById(message_id, text);
            if(updated_message == 1){
                return ResponseEntity.status(200).body(1);
            }
        }
        return ResponseEntity.status(400).body(null);
    }


//     ## 8: Our API should be able to retrieve all messages written by a particular user.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
// - The response body should contain a JSON representation of a list containing all messages posted by a particular user,
//  which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. 
// The response status should always be 200, which is the default.
@GetMapping("accounts/{account_id}/messages")
public ResponseEntity<List<Message>> retrieveMessagesByUserId(@PathVariable int account_id){
    List<Message> messages = this.messageService.findByAccountId(account_id);
    return ResponseEntity.status(200).body(messages);
}

}