package edu.neu.cs5200.onlineStore.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.cs5200.onlineStore.entities.Message;
import edu.neu.cs5200.onlineStore.repositories.MessageRepository;
import edu.neu.cs5200.onlineStore.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService{

	
	@Autowired
	private MessageRepository messageRepository;
	
	@Override
	public void save(Message message) {
		messageRepository.save(message);
	}
	
	@Override
	public List<Message> findAll() {
		return (List<Message>) messageRepository.findAll();
	}

	
}
