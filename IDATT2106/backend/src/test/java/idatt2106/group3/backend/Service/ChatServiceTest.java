package idatt2106.group3.backend.Service;

import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.Message;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.DTO.MessageDTO;
import idatt2106.group3.backend.Repository.ChatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ChatServiceTest
{
    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @BeforeEach
    public void setup() {
    
        Chat chat1 = new Chat();
        chat1.setChatId(1);
        Chat chat2 = new Chat();
        chat2.setChatId(2);

        Message message = new Message("Hello",LocalDateTime.now());
        User user = new User();
        user.setUserId(1);
        message.setUser(user);
        message.setChat(chat1);
        
        Set<Message> messages = Set.of(message);
        chat1.setMessages(messages);

        Mockito.lenient()
        .when(chatRepository.save(any()))
        .thenReturn(chat1);

        Mockito.lenient()
        .when(chatRepository.findById(anyLong()))
        .thenReturn(Optional.of(chat1));

        Mockito.lenient()
        .when(chatRepository.findAll())
        .thenReturn(List.of(chat1,chat2));
    }

    @Test
    public void getChats_ListWithTwoObjects_ReturnsList(){
        List<Chat> chats = chatService.getChats();

        assertEquals(2, chats.size());
        assertEquals(1, chats.get(0).getChatId());
        assertEquals(2, chats.get(1).getChatId());
    }

    @Test
    public void getChat_IdExists_UserIsCorrect(){
        Chat chat = chatService.getChat(1);

        assertEquals(1, chat.getChatId());
    }

    @Test
    public void createChat_userGetsAdded_ReturnsUser(){
        Chat chat = new Chat();
        chat = chatService.createChat(chat);

        assertNotNull(chat);
        assertEquals(1, chat.getChatId());
    }

    @Test
    public void updateChat_updatesChat_ReturnsUpdatedChat(){
        Chat chat = new Chat();
        chat = chatService.updateChat(1, chat);

        assertNotNull(chat);
        assertEquals(1, chat.getChatId());
    }

    @Test
    public void getMessages_MessagesExists_ReturnsSet(){
        List<MessageDTO> messageList = chatService.getMessages(1);
        

        assertEquals(1, messageList.size());
        assertEquals("Hello", messageList.get(0).getMessage());
    }

}
