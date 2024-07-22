package com.phegondev.PhegonHotel.service.impl;

import com.phegondev.PhegonHotel.dto.Response;
import com.phegondev.PhegonHotel.dto.RoomDTO;
import com.phegondev.PhegonHotel.entity.Room;
import com.phegondev.PhegonHotel.exception.OurException;
import com.phegondev.PhegonHotel.repo.BookingRepository;
import com.phegondev.PhegonHotel.repo.RoomRepository;
import com.phegondev.PhegonHotel.service.interfac.IRoomService;
import com.phegondev.PhegonHotel.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private static final String PHOTO_DIRECTORY = "C:/Users/gamer/Downloads/Jwt/phegon-hotel-booking-and-management/frontend/public";

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws IOException {
        // Upload the image and get the URL
        String photoUrl = uploadImage(photo);

        // Create and save the new room
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        room.setRoomPhotoUrl(photoUrl);
        room.setRoomDescription(description);
        roomRepository.save(room);

        // Prepare the response
        Response response = new Response();
        response.setStatusCode(201); // Assuming 201 Created status
        response.setMessage("Room added successfully");

        return response;
    }

        private String uploadImage(MultipartFile photo) throws IOException {
            Path uploadPath = Paths.get(PHOTO_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    //        return ServletUriComponentsBuilder.fromCurrentContextPath()
    //                .path("/images/")
    //                .path(fileName)
    //                .toUriString();
            return fileName.toString();
        }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Room deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (photo != null && !photo.isEmpty()) {
                String photoUrl = uploadImage(photo);
                room.setRoomPhotoUrl(photoUrl);
            }

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);
            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setRoom(roomDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving available rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving available rooms: " + e.getMessage());
        }
        return response;
    }
}
