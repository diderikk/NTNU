package idatt2106.group3.backend.Service;

import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.Message;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.DTO.MessageDTO;
import idatt2106.group3.backend.Repository.ChatRepository;
import idatt2106.group3.backend.Repository.MessageRepository;
import idatt2106.group3.backend.Repository.UserRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Save a new message in the database, and add it to the chat.
     * @param chatId
     * @param message
     * @return created message
     */
    public Message createMessage(long chatId, Message message)
    {
        LOGGER.info("createMessage(Chat chat, Message message) was called with chatId: {}, and messageId: {}", chatId, message.getMessageId());
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (!chat.isPresent())
        {
            return null;
        }
        message.setChat(chat.get());
        return messageRepository.save(message);
    }

    /**
     * Creates new message based on information 
     * from messageDTO and saves it in the database.
     * @param messageDTO
     * @return messageDTO
     */
    public MessageDTO createMessageDTO(MessageDTO messageDTO){
        LOGGER.info("createMessageDTO(MessageDTO messageDTO) was called with chatId: {}", messageDTO.getChatId());
        Optional<Chat> chat = chatRepository.findById(messageDTO.getChatId());
        Optional<User> user = userRepository.findById(messageDTO.getUserId());
        if (!chat.isPresent() || !user.isPresent())
        {
            return null;
        }
        messageRepository.save(new Message(messageDTO, chat.get(), user.get()));
        return messageDTO;
    }
}
