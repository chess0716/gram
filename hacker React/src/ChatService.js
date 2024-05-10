import axios from 'axios';

const BASE_URL = 'http://localhost:8005';

export const getChatRoomByPostId = postId => {
    return axios.get(`${BASE_URL}/chat/room/by-post/${postId}`);
};

export const getMessagesByRoomId = chatRoomId => {
    return axios.get(`${BASE_URL}/chat/messages/${chatRoomId}`);
};

export const sendMessageToRoom = (chatRoomId, message) => {
    return axios.post(`${BASE_URL}/chat/${chatRoomId}/send`, message);
};
