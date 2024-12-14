package car.sharing.app.carsharingservice.service.notification;

import car.sharing.app.carsharingservice.exception.EntityNotFoundException;
import car.sharing.app.carsharingservice.model.User;
import car.sharing.app.carsharingservice.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl extends TelegramLongPollingBot
        implements NotificationService {
    private static final Logger logger = LoggerFactory
            .getLogger(TelegramNotificationServiceImpl.class);
    private static final String NOTIFY_SUCCESS_MESSAGE = "Hi! I'll notify you about all "
            + "recent activities you must know";
    private static final String RENOTIFY_SUCCESS_MESSAGE = "Hi! You've already linked up your tg."
            + " I'll notify you about all recent activities you must know";
    private static final String ENTER_ID_MESSAGE = "Please, enter your ID from the "
            + "website using /start userId";
    private static final String START_COMMAND = "/start";
    private static final byte ID_INDEX = 1;
    private static final byte COMMAND_INDEX = 0;
    private final TelegramBotsApi telegramBotsApi;
    private final UserRepository userRepository;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.key}")
    private String botToken;

    @PostConstruct
    public void registerBot() {
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error("Failed to register tg bot");
            throw new RuntimeException("Can not register telegram bot." + e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String[] parts = text.split(" ");
            if (parts.length > 1 && parts[COMMAND_INDEX].equalsIgnoreCase(START_COMMAND)
                    && StringUtils.hasText(parts[ID_INDEX])) {
                long userId = Long.parseLong(parts[ID_INDEX]);
                handleUserLinking(userId, chatId);
            } else {
                sendMessageToCustomer(chatId, ENTER_ID_MESSAGE);
            }
        }
    }

    @Override
    public void sendMessageToAllManagers(String message, List<User> managers) {
        managers.stream()
                .filter(manager -> manager.getTgChatId() != null)
                .forEach(manager -> sendMessageToCustomer(manager.getTgChatId(), message));
    }

    @Override
    public void sendMessageToCustomer(Long chatId, String message) {
        if (chatId == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Chat ID or message cannot be null or empty");
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        sendMessage.setParseMode("HTML");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message. Chat id {}, error: {}", chatId, e.getMessage());
            throw new RuntimeException("Can not send message to chat with id: " + chatId, e);
        }
    }

    private void linkUpUserId(Long chatId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can not find user with id: " + userId));
        user.setTgChatId(chatId);
        userRepository.save(user);
    }

    private void handleUserLinking(Long userId, Long chatId) {
        userRepository.findByTgChatId(userId).ifPresentOrElse(
                (user) -> {
                    sendMessageToCustomer(chatId, RENOTIFY_SUCCESS_MESSAGE);
                    logger.info("User with ID {} is already linked to chat ID {}",
                            userId, chatId);
                },
                () -> {
                    logger.info("Linking user with ID {} to chat ID {}", userId, chatId);
                    linkUpUserId(chatId, userId);
                    sendMessageToCustomer(chatId,
                            NOTIFY_SUCCESS_MESSAGE);
                }
        );
    }
}
