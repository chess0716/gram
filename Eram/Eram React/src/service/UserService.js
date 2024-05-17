import axios from "axios";
import AuthService from "./AuthService";

const API_URL = "http://localhost:8005"; // 서버의 주소와 포트

const UserService = {
  updateUser: async (userData) => {
    try {
      return sendRequest("/members/update", "put", userData);
    } catch (error) {
      console.error("Failed to update user:", error);
      throw error;
    }
  },

  deleteUser: async (num) => {
    try {
      return sendRequest(`/members/delete/${num}`, "delete");
    } catch (error) {
      console.error("Failed to delete user:", error);
      throw error;
    }
  },

  getUser: async (num) => {
    try {
      return sendRequest(`/members/get/${num}`);
    } catch (error) {
      console.error("Failed to fetch user:", error);
      throw error;
    }
  },

  getAllUsers: async () => {
    try {
      return sendRequest("/members/get/all");
    } catch (error) {
      console.error("Failed to fetch all users:", error);
      throw error;
    }
  },

  getCurrentUser: async () => {
    try {
      const accessToken = localStorage.getItem("accessToken");
      if (!accessToken) {
        throw new Error("No access token found");
      }
      return sendRequest("/members/me", "get", null, accessToken);
    } catch (error) {
      console.error("Failed to fetch current user:", error);
      throw error;
    }
  },

  getCurrentUserPosts: async () => {
    try {
      const accessToken = localStorage.getItem("accessToken");
      if (!accessToken) {
        throw new Error("No access token found");
      }
      return sendRequest("/members/posts", "get", null, accessToken);
    } catch (error) {
      console.error("Failed to fetch current user posts:", error);
      throw error;
    }
  },

  getCurrentUserChatRooms: async () => {
    try {
      const accessToken = localStorage.getItem("accessToken");
      if (!accessToken) {
        throw new Error("No access token found");
      }
      return sendRequest("/members/chatrooms", "get", null, accessToken);
    } catch (error) {
      console.error("Failed to fetch current user chat rooms:", error);
      throw error;
    }
  },

  setCurrentUser: (user) => {
    localStorage.setItem("user", JSON.stringify(user));
  },

  setCurrentUserToken: (token) => {
    localStorage.setItem("accessToken", token);
  },

  logout: async () => {
    try {
      await AuthService.logout();
    } catch (error) {
      console.error("Failed to logout:", error);
      throw error;
    }
  },

  deleteAccount: async () => {
    try {
      await AuthService.deleteAccount();
    } catch (error) {
      console.error("Failed to delete account:", error);
      throw error;
    }
  },
};

const sendRequest = async (url, method = "get", data, accessToken) => {
  try {
    const response = await axios({
      url: `${API_URL}${url}`,
      method,
      data,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error(`Request failed: ${url}`, error);
    throw error;
  }
};

export default UserService;
