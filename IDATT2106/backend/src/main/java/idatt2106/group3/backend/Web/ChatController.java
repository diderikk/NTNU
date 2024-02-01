package idatt2106.group3.backend.Web;

import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.Message;
import idatt2106.group3.backend.Model.DTO.MessageDTO;
import idatt2106.group3.backend.Service.ChatService;
import idatt2106.group3.backend.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController
{
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Chat>> getChats() {
        List<Chat> chats = chatService.getChats();
        if (chats == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @GetMapping("/{chat_id}")
    public ResponseEntity<Chat> getChat(@PathVariable("chat_id") long chatId) {
        Chat chat = chatService.getChat(chatId);
        if (chat == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat returnChat = chatService.createChat(chat);
        if (returnChat == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnChat, HttpStatus.CREATED);
    }

    @PutMapping("/{chat_id}")
    public ResponseEntity<Chat> updateChat(@PathVariable("chat_id") long chatId, @RequestBody Chat chat) {
        Chat returnChat = chatService.updateChat(chatId, chat);
        if (returnChat == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnChat, HttpStatus.OK);
    }

    @GetMapping("/{chat_id}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable("chat_id") long chatId) {
        List<MessageDTO> messages = chatService.getMessages(chatId);
        if (messages == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/{chat_id}/messages")
    public ResponseEntity<Message> createMessage(@PathVariable("chat_id") long chatId, @RequestBody Message message) {
        Message messageCreated = messageService.createMessage(chatId, message);
        if (messageCreated != null)
        {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/{chat_id}")
    public ResponseEntity<String> deleteChat(@PathVariable("chat_id") long chatId) {
        if (chatService.deleteChat(chatId))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
