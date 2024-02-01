package idatt2106.group3.backend.Web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import idatt2106.group3.backend.Model.DTO.MessageDTO;
import idatt2106.group3.backend.Service.MessageService;

@Controller
@Profile("!test")
public class MessageController {

    @Autowired 
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/{chat_id}")
    public void connectChat(@DestinationVariable("chat_id") long chatId, @Payload MessageDTO payload){
        LOGGER.info("connectChat(@DestinationVariable(chat_id) long chatId, @Payload MessageDTO payload) with chatId {}", chatId);
        simpMessagingTemplate.convertAndSend("/api/v1/chat/"+ chatId +"/messages", payload);
        messageService.createMessageDTO(payload);
    }
}
