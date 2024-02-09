package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;



@Service
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        if(message != null){
            return this.messageRepository.save(message);
        }
        return null;
    }

    public List<Message> findAllMessages() {
        return this.messageRepository.findAll();
    }

    public Optional<Message> findMessageById(int message_id) {
        return this.messageRepository.findById(message_id);
    }

}
