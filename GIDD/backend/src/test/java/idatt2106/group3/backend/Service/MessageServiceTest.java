package idatt2106.group3.backend.Service;

import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.Activity;
import idatt2106.group3.backend.Model.Chat;
import idatt2106.group3.backend.Model.Message;
import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.DTO.MessageDTO;
import idatt2106.group3.backend.Repository.ChatRepository;
import idatt2106.group3.backend.Repository.MessageRepository;
import idatt2106.group3.backend.Repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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
public class MessageServiceTest
{
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        Activity activity = new Activity();

        Message message1 = new Message("Message 1", LocalDateTime.now());
        Message message2 = new Message("Message 2", LocalDateTime.now());
        Message message3 = new Message("Message 3", LocalDateTime.now());

        Set<Message> messages = new HashSet<>();
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);

        Chat chat = new Chat(activity, messages);
        chat.setChatId(1l);

        User user = new User("Forename", "Surname", "Email", LocalDate.of(2000, 1, 1), Difficulty.MEDIUM, "Hash", "Salt", 1, "Role", 1, null);
        user.setUserId(1l);

        Mockito.lenient().when(chatRepository.findById(1l)).thenReturn(Optional.of(chat));
        Mockito.lenient().when(userRepository.findById(1l)).thenReturn(Optional.of(user));
    }

    @Test
    public void createMessage_ChatIsNotPresent_ReturnsNull() {
        long chatId = -1l;
        assertNull(messageService.createMessage(chatId, new Message()));
    }

    @Test
    public void createMessage_ChatIsPresent_ReturnsCreatedMessage() {
        User user = userRepository.findById(1l).get();
        Chat chat = chatRepository.findById(1l).get();

        Message message4 = new Message("Message", LocalDateTime.of(2000, 1, 1, 1, 1, 2), chat, user);
        
        Mockito.lenient().when(messageRepository.save(message4)).thenReturn(message4);

        assertNotNull(messageService.createMessage(chat.getChatId(), message4));
        assertThat(messageService.createMessage(chat.getChatId(), message4).getMessage()).isEqualTo("Message");
        assertThat(messageService.createMessage(chat.getChatId(), message4).getTimeSent()).isEqualTo(LocalDateTime.of(2000, 1, 1, 1, 1, 2));
    }

    @Test
    public void createMessageDTO_ChatAndUserArePresent_AddsMessageDTOAndReturnsIt() {
        User user = userRepository.findById(1l).get();
        Chat chat = chatRepository.findById(1l).get();

        MessageDTO messageDTO = new MessageDTO(new Message("Message", LocalDateTime.of(2000, 1, 1, 1, 1, 2), chat, user));
        messageDTO = messageService.createMessageDTO(messageDTO);
        assertThat(messageDTO.getChatId()).isEqualTo(1l);
        assertThat(messageDTO.getMessage()).isEqualTo("Message");
        assertThat(messageDTO.getTime()).isEqualTo(LocalDateTime.of(2000, 1, 1, 1, 1, 2));
    }
}
