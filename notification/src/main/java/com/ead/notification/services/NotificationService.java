package com.ead.notification.services;

import com.ead.notification.dtos.NotificationRecordCommandDto;
import com.ead.notification.models.NotificationModel;

public interface NotificationService {
    NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto);
}
