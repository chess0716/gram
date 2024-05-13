import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { MainContainer, ChatContainer, MessageList, Message, MessageInput } from "@chatscope/chat-ui-kit-react";
import { getChatRoomByPostId, getMessagesByRoomId, sendMessageToRoom, getMembersByRoomId } from './ChatService';
import './CustomStyle.css'; 

function ChatRoom() {
    const [messages, setMessages] = useState([]);
    const [members, setMembers] = useState([]);
    const { postId } = useParams();
    const [chatRoomId, setChatRoomId] = useState(null);
    const clientRef = useRef(null);

    useEffect(() => {
        const loadChatRoom = async () => {
            try {
                const roomResponse = await getChatRoomByPostId(postId);
                const data = roomResponse.data;
                if (data.chatRoomId) {
                    setChatRoomId(data.chatRoomId);
                    const messagesResponse = await getMessagesByRoomId(data.chatRoomId);
                    setMessages(messagesResponse.data.map(msg => ({
                        ...msg,
                        direction: msg.userId === 'currentUserId' ? 'outgoing' : 'incoming'
                    })));
                    const membersResponse = await getMembersByRoomId(data.chatRoomId);
                    setMembers(membersResponse.data);
                    setupWebSocket(data.chatRoomId);
                } else {
                    console.error('Chat room not found for the provided post ID');
                }
            } catch (error) {
                console.error('Error fetching chat room:', error);
            }
        };

        loadChatRoom();

        return () => {
            if (clientRef.current) {
                clientRef.current.deactivate();
            }
        };
    }, [postId]);

    const setupWebSocket = (roomId) => {
        const socket = new SockJS('http://localhost:8005/ws');
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 10000,
        });

        stompClient.onConnect = () => {
            stompClient.subscribe(`/topic/chatroom/${roomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages(prev => [...prev, { ...receivedMessage, direction: "incoming" }]);
            });
        };

        stompClient.activate();
        clientRef.current = stompClient;
    };

    const sendMessage = async (input) => {
        if (input.trim() && clientRef.current && clientRef.current.active && chatRoomId) {
            const messageData = { message: input };
            try {
                await sendMessageToRoom(chatRoomId, messageData);
                setMessages(prev => [...prev, { message: input, direction: "outgoing" }]);
            } catch (error) {
                console.error('Error sending message:', error);
            }
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <MainContainer>
                <ChatContainer className="chat-container">
                    <div className="members-list">
                        {members.map(member => (
                            <div key={member.id}>{member.name}</div>
                        ))}
                    </div>
                    <MessageList className="message-list">
                        {messages.map((msg, index) => (
                            <Message key={index} model={{ message: msg.message, direction: msg.direction }} className="message" />
                        ))}
                    </MessageList>
                    <MessageInput className="message-input" placeholder="Type message here" onSend={sendMessage} />
                </ChatContainer>
            </MainContainer>
        </div>
    );
}

export default ChatRoom;
