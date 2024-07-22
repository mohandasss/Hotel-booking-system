import axios from "axios";

const BASE_URL = process.env.REACT_APP_API_BASE_URL || "http://localhost:4040";

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json"
  }
});

axiosInstance.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default class ApiService {
  /**AUTH */
  static async registerUser(registration) {
    try {
      const response = await axiosInstance.post(`/auth/register`, registration);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async loginUser(loginDetails) {
    try {
      const response = await axiosInstance.post(`/auth/login`, loginDetails);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  /**USERS */
  static async getAllUsers() {
    try {
      const response = await axiosInstance.get(`/users/all`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getUserProfile() {
    try {
      const response = await axiosInstance.get(`/users/get-logged-in-profile-info`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getUser(userId) {
    try {
      const response = await axiosInstance.get(`/users/get-by-id/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getUserBookings(userId) {
    try {
      const response = await axiosInstance.get(`/users/get-user-bookings/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async deleteUser(userId) {
    try {
      const response = await axiosInstance.delete(`/users/delete/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  /**ROOM */
  static async addRoom(formData) {
    try {
      const response = await axiosInstance.post(`/rooms/add`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getAllAvailableRooms() {
    try {
      const response = await axiosInstance.get(`/rooms/all-available-rooms`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType) {
    try {
      const response = await axiosInstance.get(
        `/rooms/available-rooms-by-date-and-type`,
        { params: { checkInDate, checkOutDate, roomType } }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getRoomTypes() {
    try {
      const response = await axiosInstance.get(`/rooms/types`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getAllRooms() {
    try {
      const response = await axiosInstance.get(`/rooms/all`);
      console.log(response)
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  } 
  

  static async getRoomById(roomId) {
    try {
      const response = await axiosInstance.get(`/rooms/room-by-id/${roomId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async deleteRoom(roomId) {
    try {
      const response = await axiosInstance.delete(`/rooms/delete/${roomId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async updateRoom(roomId, formData) {
    try {
      const response = await axiosInstance.put(`/rooms/update/${roomId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  /**BOOKING */
  static async bookRoom(roomId, userId, booking) {
    try {
      const response = await axiosInstance.post(`/bookings/book-room/${roomId}/${userId}`, booking);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getAllBookings() {
    try {
      const response = await axiosInstance.get(`/bookings/all`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async getBookingByConfirmationCode(bookingCode) {
    try {
      const response = await axiosInstance.get(`/bookings/get-by-confirmation-code/${bookingCode}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  static async cancelBooking(bookingId) {
    try {
      const response = await axiosInstance.delete(`/bookings/cancel/${bookingId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  }

  /**AUTHENTICATION CHECKER */
  static logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
  }

  static isAuthenticated() {
    return !!localStorage.getItem("token");
  }

  static isAdmin() {
    return localStorage.getItem("role") === "ADMIN";
  }

  static isUser() {
    return localStorage.getItem("role") === "USER";
  }
}
