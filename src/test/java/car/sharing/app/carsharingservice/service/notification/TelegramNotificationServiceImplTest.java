package car.sharing.app.carsharingservice.service.notification;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceImplTest {
    private static final Long DEFAULT_ID = 1L;
    @InjectMocks
    private TelegramNotificationServiceImpl notificationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TelegramBotsApi telegramBotsApi;

    @Test
    @DisplayName("Should link up user's chat id with user")
    void onUpdateReceived_ShouldLinkUpUser() {
        Chat chat = new Chat();
        chat.setId(DEFAULT_ID);
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText("/start 1");
        update.getMessage().setChat(chat);
        update.getMessage().setMessageId(1);

        TelegramNotificationServiceImpl spyService = spy(notificationService);

        User user = new User().setId(DEFAULT_ID).setTgChatId(null);
        when(userRepository.findByTgChatId(DEFAULT_ID)).thenReturn(Optional.empty());
        when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(user));
        doNothing().when(spyService).sendMessageToCustomer(
                anyLong(), anyString());

        spyService.onUpdateReceived(update);

        verify(userRepository).save(any(User.class));
        verify(spyService).sendMessageToCustomer(anyLong(), anyString());
    }

    @Test
    @DisplayName("Given existing user id, should send message back")
    void onUpdateReceived_GivenExistingUserId_ShouldSendMessage() {
        Chat chat = new Chat();
        chat.setId(DEFAULT_ID);
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText("/start 1");
        update.getMessage().setChat(chat);
        update.getMessage().setMessageId(1);

        TelegramNotificationServiceImpl spyService = spy(notificationService);

        User user = new User().setId(DEFAULT_ID).setTgChatId(null);
        when(userRepository.findByTgChatId(DEFAULT_ID)).thenReturn(
                Optional.of(user));
        doNothing().when(spyService).sendMessageToCustomer(
                anyLong(), anyString());

        spyService.onUpdateReceived(update);

        verify(spyService).sendMessageToCustomer(anyLong(),
                anyString());
    }

    @Test
    @DisplayName("Given invalid user id, should throw exception")
    void onUpdateReceived_InvalidUserId_ShouldThrowException() {
        Chat chat = new Chat();
        chat.setId(DEFAULT_ID);
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText("/start 1");
        update.getMessage().setChat(chat);
        update.getMessage().setMessageId(1);

        TelegramNotificationServiceImpl spyService = spy(notificationService);

        when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> spyService.onUpdateReceived(update));
    }

    @Test
    @DisplayName("Given invalid form, should send message back")
    void onUpdateReceived_UserGaveInvalidForm_ShouldSendTextBack() {
        Chat chat = new Chat();
        chat.setId(DEFAULT_ID);
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText(". stayt 23413");
        update.getMessage().setChat(chat);
        update.getMessage().setMessageId(1);

        TelegramNotificationServiceImpl spyService = spy(notificationService);

        doNothing().when(spyService).sendMessageToCustomer(anyLong(),
                anyString());

        spyService.onUpdateReceived(update);

        verify(spyService).sendMessageToCustomer(anyLong(), anyString());
    }

    @Test
    @DisplayName("Send messages to two managers")
    void sendMessageToAllManagers_ShouldSendMessages() throws TelegramApiException {
        String message = "message";
        List<User> managers = List.of(
                new User().setTgChatId(111L),
                new User().setTgChatId(222L),
                new User().setTgChatId(null)
        );

        TelegramNotificationServiceImpl spyService = spy(notificationService);

        doReturn(null).when(spyService)
                .execute(any(SendMessage.class));

        spyService.sendMessageToAllManagers(message, managers);

        verify(spyService, times(2))
                .execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Chat id is null, should throw IllegalArgumentException")
    void sendMessageToCustomer_ChatIdIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> notificationService.sendMessageToCustomer(null, "Message"));
    }

    @Test
    @DisplayName("Message is null, should throw IllegalArgumentException")
    void sendMessageToCustomer_MessageIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> notificationService.sendMessageToCustomer(DEFAULT_ID, null));
    }

    @Test
    @DisplayName("Message does not have text, should throw IllegalArgumentException")
    void sendMessageToCustomer_MessageDoesNotHaveText_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> notificationService.sendMessageToCustomer(DEFAULT_ID, ""));
    }

    @Test
    @DisplayName("Send message to customer")
    void sendMessageToCustomer_ValidFields_ShouldSendMessage()
            throws TelegramApiException {
        String message = "message";
        Long chatId = 5345345L;

        TelegramNotificationServiceImpl spyService = spy(notificationService);

        doReturn(null).when(spyService)
                .execute(any(SendMessage.class));

        spyService.sendMessageToCustomer(chatId, message);

        verify(spyService, times(1))
                .execute(any(SendMessage.class));
    }
}
