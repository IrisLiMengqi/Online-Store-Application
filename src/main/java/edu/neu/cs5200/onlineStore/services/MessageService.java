package edu.neu.cs5200.onlineStore.services;

import java.util.List;

import edu.neu.cs5200.onlineStore.entities.Message;

public interface MessageService {

	void save(Message message);
	
	List<Message> findAll();

}
