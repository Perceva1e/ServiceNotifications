package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationDTO;
import com.example.notificationservice.model.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByUserId( Long userId ) {
        log.info( "Fetching notifications for user ID: {}", userId );
        List< Notification > notifications = notificationRepository.findByUserId( userId );
        List< NotificationDTO > notificationDTOs = notifications.stream( )
                .map( this::convertToDTO )
                .collect( Collectors.toList( ) );
        log.debug( "Retrieved {} notifications for user ID: {}", notificationDTOs.size( ), userId );
        return notificationDTOs;
    }

    @Transactional
    public void markNotificationAsRead( Long notificationId ) {
        log.info( "Marking notification with ID {} as read", notificationId );
        Notification notification = notificationRepository.findById( notificationId )
                .orElseThrow( ( ) -> {
                    log.warn( "Notification with ID {} not found", notificationId );
                    return new RuntimeException( "Notification not found with ID: " + notificationId );
                } );
        notification.setReading( true );
        notificationRepository.save( notification );
        log.debug( "Marked notification with ID {} as read", notificationId );
    }

    @Transactional
    public void markAllNotificationsAsRead( Long userId ) {
        log.info( "Marking all notifications as read for user ID: {}", userId );
        List< Notification > notifications = notificationRepository.findByUserIdAndReadingFalse( userId );
        notifications.forEach( notification -> notification.setReading( true ) );
        notificationRepository.saveAll( notifications );
        log.debug( "Marked {} notifications as read for user ID: {}", notifications.size( ), userId );
    }

    private NotificationDTO convertToDTO( Notification notification ) {
        NotificationDTO dto = new NotificationDTO( );
        dto.setId( notification.getId( ) );
        dto.setMessage( notification.getMessage( ) );
        dto.setCreatedAt( notification.getCreatedAt( ) );
        dto.setReading( notification.isReading( ) );
        dto.setUserId( notification.getUser( ).getId( ) );
        dto.setFilmId( notification.getFilm( ).getId( ) );
        return dto;
    }
}