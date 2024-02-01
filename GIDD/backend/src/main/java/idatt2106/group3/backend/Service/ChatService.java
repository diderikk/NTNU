package idatt2106.group3.backend.Service;

import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.Message;
import idatt2106.group3.backend.Model.DTO.MessageDTO;
import idatt2106.group3.backend.Repository.ChatRepository;

import idatt2106.group3.backend.Repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Gets a list of all chats stored in the database
     * @return List of chats
     */
    public List<Chat> getChats() {
        LOGGER.info("getChats() called"); 
        return chatRepository.findAll();
    }

    /**
     * Gets a Chat object if it exists in the database
     * @param chatId
     * @return Chat
     */
    public Chat getChat(long chatId) {
        LOGGER.info("getChat(long chatId) called with chatId: {}", chatId); 
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent())
        {
            return chat.get();
        }
        return null;
    }

    /**
     * Stores a given Chat object in the database
     * @param chat
     * @return Chat
     */
    public Chat createChat(Chat chat) {
        LOGGER.info("createChat(Chat chat) called with chatId: {}", chat.getChatId()); 
        return chatRepository.save(chat);
    }

    /**
     * Finds a Chat object from database and changes fields that are not null in the input Chat object
     * @param chatId
     * @param chat
     * @return updated Chat object
     */
    public Chat updateChat(long chatId, Chat chat) {
        LOGGER.info("updateChat(long chatId, Chat chat) called with chatId: {}", chatId); 
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if(optionalChat.isPresent()){
            Chat tempChat = optionalChat.get();
            if(chat.getActivity() != null) tempChat.setActivity(chat.getActivity());
            if(chat.getMessages() != null) tempChat.setMessages(chat.getMessages());
            return chatRepository.save(tempChat);
        }
        return null;
    }

    /**
     * Returns all messages written in the given Chat
     * @param chatId
     * @return List of all Message objects, empty list in case chat not exists
     */
    public List<MessageDTO> getMessages(long chatId) {
        LOGGER.info("getMessages(long chatId) called with chatId: {}", chatId); 
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isPresent()){
            List<Message> set = chat.get().getMessages().stream().collect(Collectors.toList());
            set.sort(Comparator.comparing(Message::getMessageId));
            return set.stream().map(message -> new MessageDTO(message)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * Deletes Chat object from the database based on chatId
     * @param chatId
     * @return true if was deleted, false if it still exists
     */
    public boolean deleteChat(long chatId) {
        LOGGER.info("deleteChat(long chatId) called with chatId: {}", chatId);
        List<Message> messages = messageRepository.findAll().stream().filter(message -> message.getChat().getChatId() == chatId).collect(Collectors.toList());
        messageRepository.deleteInBatch(messages);
        chatRepository.deleteById(chatId);
        return !chatRepository.existsById(chatId);
    }
}
