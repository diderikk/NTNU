package idatt2106.group3.backend.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import idatt2106.group3.backend.Model.*;

import idatt2106.group3.backend.Repository.ChatRepository;
import idatt2106.group3.backend.Repository.MessageRepository;
import idatt2106.group3.backend.Repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChatControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        Chat chat = new Chat();
        Chat chat2 = new Chat();

        chat = chatRepository.save(chat);
        chatRepository.save(chat2);

        User user = new User();
        user = userRepository.save(user);

        Message message = new Message("A message", null, chat2, null);
        Message message2 = new Message("A second message", null, chat2, null);
        Message message3 = new Message("Test", null, chat, user);
        messageRepository.save(message);
        messageRepository.save(message2);
        messageRepository.save(message3);
    }

    @AfterEach
    public void teardown() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
        chatRepository.deleteAll();
    }

    @Test
    public void getChat_ReturnsFirstChat_StatusOk() throws Exception {
        long id = chatRepository.findAll().get(0).getChatId();

        this.mockMvc.perform(get("/api/v1/chats/" + id ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].message", containsStringIgnoringCase("Test")))
                //.andExpect(jsonPath("$.messages.timeSent", is(null)))
                .andReturn();
    }

    @Test
    public void getChats_ReturnsChats_StatusOk() throws Exception {
        this.mockMvc.perform(get("/api/v1/chats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
                .andReturn();
    }

    @Test
    public void createChat_PostChat_StatusCreated() throws Exception {
        Chat chat = new Chat();
        chat.setMessages(Set.of());

        String chatJson = objectMapper.writeValueAsString(chat);

        this.mockMvc.perform(post("/api/v1/chats")
        .contentType(MediaType.APPLICATION_JSON).content(chatJson))
                .andExpect(status().isCreated())  //Creates chat, not messages. Cannot create chat with existing messages.
                .andReturn();
    }

    @Test
    public void updateChat_UpdateChatGetResponse_StatusOk() throws Exception
    {
        long id = chatRepository.findAll().get(0).getChatId();

        Chat chatTest = chatRepository.findAll().get(0);
        chatTest.setMessages(Set.of()); //We are not updating messages

        String chatJson = objectMapper.writeValueAsString(chatTest);

        this.mockMvc.perform(put("/api/v1/chats/" + id)
                .contentType(MediaType.APPLICATION_JSON).content(chatJson))
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(get("/api/v1/chats/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].message", containsStringIgnoringCase("Test")))
                .andReturn();
    }

    @Test
    public void getMessages_ReturnsMessagesFromAChat_StatusOk() throws Exception {
        long id = chatRepository.findAll().get(0).getChatId();
        this.mockMvc.perform(get("/api/v1/chats/" + id + "/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].message", containsStringIgnoringCase("Test")))
                //.andExpect(jsonPath("$.[0].timeSent", is(LocalDateTime.now())))
                .andReturn();
    }

    @Test
    public void createMessage_PostMessage_StatusCreated() throws Exception {
        Message message = new Message();
        message.setMessage("A second message");
        String messageJson = objectMapper.writeValueAsString(message);

        long id = chatRepository.findAll().get(0).getChatId();

        this.mockMvc.perform(post("/api/v1/chats/" + id + "/messages")
                .contentType(MediaType.APPLICATION_JSON).content(messageJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("A second message")))
                //.andExpect(jsonPath("$.messages[0].timeSent", is(null)))
                .andReturn();
    }

    @Test
    public void deleteChat_ShouldDeleteChat_StatusOk() throws Exception {
        long id = chatRepository.findAll().get(0).getChatId();
        this.mockMvc.perform(delete("/api/v1/chats/" + id))
                .andExpect(status().isOk());
    }
}