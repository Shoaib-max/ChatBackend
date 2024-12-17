package com.shoaib.Chat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoaib.Chat.entities.Message;
import com.shoaib.Chat.entities.Room;
import com.shoaib.Chat.repostory.RoomRepo;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("http://localhost:5173")
public class RoomController {
	
	@Autowired
	RoomRepo roomRepo;
	
	@GetMapping("/data")
	public String fetch() {
		return "running";
	}
	
	@PostMapping("/CreateRoom")
	public ResponseEntity<?> createRoom(@RequestBody String roomId) {
		
		if(roomRepo.findByRoomId(roomId) != null) {
			return ResponseEntity.badRequest().body("Room already Exists");
		}
		
		Room room = new Room();
		room.setRoomId(roomId);
		roomRepo.save(room);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(room);
		
	}
	
	@GetMapping("/{roomId}")
	public ResponseEntity<?> joinRoom(@PathVariable String roomId){
		
		Room byRoomId = roomRepo.findByRoomId(roomId);
		
		if(byRoomId == null) {
			return ResponseEntity.badRequest().body("Room not found");
		}
		
		return ResponseEntity.ok(byRoomId);
		
	}
	
	@GetMapping("/{roomId}/messages")
	public ResponseEntity<?> getMessage(@PathVariable String roomId,
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "20", required = false) int size) {

		List<Room> lst = new ArrayList<Room>();

		Room room = roomRepo.findByRoomId(roomId);

		if (room == null) {
			return ResponseEntity.badRequest().build();
		}
		
		List<Message> message = room.getMessages();
		int start = Math.max(0, message.size() - page * size);
		int end = Math.min(message.size(),start+size);
		
		List<Message> PaginatesMessage = message.subList(start, end);
		return ResponseEntity.ok(PaginatesMessage);

	}
	

}
