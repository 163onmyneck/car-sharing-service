package car.sharing.app.carsharingservice.service.notification;

import car.sharing.app.carsharingservice.model.User;
import java.util.List;

public interface NotificationService {
    void sendMessageToAllManagers(String message, List<User> managers);

    void sendMessageToCustomer(Long chatId, String message);
}
