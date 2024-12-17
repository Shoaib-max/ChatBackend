package com.shoaib.Chat.controller;


import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.MessageRequestReplyOperations;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoClient;
import com.shoaib.Chat.entities.Message;
import com.shoaib.Chat.entities.MessageRequest;
import com.shoaib.Chat.entities.Room;
import com.shoaib.Chat.repostory.RoomRepo;

@RestController
@RequestMapping("/api/v1/chat/")
@CrossOrigin("http://localhost:5173")
public class ChatController {
	
	@Autowired
	MongoClient mongo;
	
	@Autowired
	RoomRepo repo;
	
	@MessageMapping("/sendMessage/{roomId}") // app//sendMessage/{roomId}
	@SendTo("/topic/room/{roomId}")//subscribe
	public Message sendMessage(
			@DestinationVariable String roomId,
			@RequestBody MessageRequest request
			) {
		
		System.out.println(request.getContent());
		System.out.println(request.getSender());
		
		Room room = repo.findByRoomId(roomId);
		Message message = new Message();
		message.setContent(request.getContent());
		message.setSender(request.getSender());
		message.setTimeStamp(LocalDateTime.now());
		
		if(room!=null) {
			room.getMessages().add(message);
			repo.save(room);
		} else {
			throw new RuntimeException("room not found");
		}
		
		return message;
	}
	
	
	
	
	
	
//	@GetMapping("send")
//	public List<Map<String, Object>> test() {
//
//		MongoDatabase db = mongo.getDatabase("chat");
//		MongoCollection<Document> coll = db.getCollection("test-chat");
//		
//		try {
//			
//			Document jsonObject = new Document();
//			jsonObject.put("name", "Shoaib");
//			jsonObject.put("role", "Developer");
//			jsonObject.put("language", "Java");
//			Document doc = new ObjectMapper().convertValue(jsonObject, Document.class);
//			List<Document> data = new ArrayList<>();
//			data.add(doc);
//			coll.insertMany(data);
//			List<Map<String, Object>> lst = coll.find().into(new LinkedList<Map<String, Object>>());
//			return lst;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

}
