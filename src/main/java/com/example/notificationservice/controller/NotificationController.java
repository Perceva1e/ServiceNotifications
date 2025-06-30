package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationDTO;
import com.example.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notification API", description = "Endpoints for managing user notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get notifications for a user", description = "Retrieves all notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications"),
            @ApiResponse(responseCode = "404", description = "User not found or no notifications")
    })
    public ResponseEntity<List<NotificationDTO>> getNotifications(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        log.info("Fetching notifications for user ID: {}", userId);
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        log.debug("Retrieved {} notifications for user ID: {}", notifications.size(), userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/mark-read/{id}")
    @Operation(summary = "Mark a notification as read", description = "Marks a specific notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification marked as read"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<Void> markNotificationAsRead(
            @Parameter(description = "ID of the notification") @PathVariable Long id) {
        log.info("Marking notification with ID {} as read", id);
        try {
            notificationService.markNotificationAsRead(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Failed to mark notification with ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/mark-all-read/{userId}")
    @Operation(summary = "Mark all notifications as read for a user", description = "Marks all notifications as read for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All notifications marked as read"),
            @ApiResponse(responseCode = "404", description = "User not found or no notifications")
    })
    public ResponseEntity<Void> markAllNotificationsAsRead(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        log.info("Marking all notifications as read for user ID: {}", userId);
        try {
            notificationService.markAllNotificationsAsRead(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Failed to mark all notifications for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}