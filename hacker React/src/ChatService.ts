import axios from 'axios';

const BASE_URL = 'http://localhost:8005'; // 백엔드 서버 URL

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const getChatRoomByPostId = (postId: string) => {
  return axiosInstance.get(`/chat/room/by-post/${postId}`);
};

export const getMessagesByRoomId = (chatRoomId: string) => {
  return axiosInstance.get(`/chat/messages/${chatRoomId}`);
};

export const sendMessageToRoom = (chatRoomId: string, message: { message: string }) => {
  return axiosInstance.post(`/chat/${chatRoomId}/send`, message);
};

export const getMembersByRoomId = (chatRoomId: string) => {
  return axiosInstance.get(`/chat/chatroom/${chatRoomId}/user`);
};
