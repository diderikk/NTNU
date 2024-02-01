package idatt2106.group3.backend.Web;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import idatt2106.group3.backend.Configuration.Jwt.UsernameAndPasswordAuthenticationRequest;
import idatt2106.group3.backend.Enum.Difficulty;
import idatt2106.group3.backend.Model.DTO.User.UserWithPasswordDTO;
import idatt2106.group3.backend.Repository.UserRepository;
import idatt2106.group3.backend.Service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test1")
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup(){
        UserWithPasswordDTO user = new UserWithPasswordDTO("forename", "surname", "email", LocalDate.of(2005, 1, 1), Difficulty.EASY, "hash",null);
        userService.createUser(user);
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void login_UserDoesNotExist_StatusUnAuthorized() throws Exception{
        UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest = new UsernameAndPasswordAuthenticationRequest();
        usernameAndPasswordAuthenticationRequest.setEmail("test@test.com");
        usernameAndPasswordAuthenticationRequest.setPassword("testPassword");
        String objectJson = objectMapper.writeValueAsString(usernameAndPasswordAuthenticationRequest);
        mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectJson))
        .andExpect(status().isUnauthorized());
    }

    // Equal test in UserControllerTest
    @Test
    public void registration_CorrectFormat_StatusCreated() throws Exception {
        UserWithPasswordDTO user = new UserWithPasswordDTO("forename", "surname", "email1", LocalDate.of(2005, 1, 1), Difficulty.EASY, "hash", null);
        String objectJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectJson))
        .andExpect(status().isCreated());
        
        
    }

    @Test
    public void login_CorrectFormat_StatusOk() throws Exception {
        UsernameAndPasswordAuthenticationRequest user = new UsernameAndPasswordAuthenticationRequest();
        user.setEmail("email");
        user.setPassword("hash");
        String objectJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token", isA(String.class)))
        .andExpect(jsonPath("$.userId", greaterThanOrEqualTo(1)));
    }

}
