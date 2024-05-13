import axios from 'axios';

const BASE_URL = 'http://localhost:8005'; // 백엔드 서버 URL

// 채팅방에 대한 정보를 Post ID로 가져오는 함수
export const getChatRoomByPostId = postId => {
    return axios.get(`${BASE_URL}/chat/room/by-post/${postId}`);
};

// 채팅방 ID로 메시지 목록을 가져오는 함수
export const getMessagesByRoomId = chatRoomId => {
    return axios.get(`${BASE_URL}/chat/messages/${chatRoomId}`);
};

// 채팅방에 메시지를 보내는 함수
export const sendMessageToRoom = (chatRoomId, message) => {
    return axios.post(`${BASE_URL}/chat/${chatRoomId}/send`, { message });
};

// 채팅방에 참여한 사용자 목록을 가져오는 함수
export const getMembersByRoomId = chatRoomId => {
    return axios.get(`${BASE_URL}/chatroom/{chatRoomId}/members`);
};
