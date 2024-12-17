package com.shoaib.Chat.repostory;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shoaib.Chat.entities.Room;

public interface RoomRepo extends MongoRepository<Room, String> {
	
	//get room using room Id
	
	Room findByRoomId(String roomId);

}
