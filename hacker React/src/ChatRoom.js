import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { MainContainer, ChatContainer, MessageList, Message, MessageInput } from "@chatscope/chat-ui-kit-react";
import { getChatRoomByPostId, getMessagesByRoomId, sendMessageToRoom } from './ChatService';

function ChatRoom() {
    const [messages, setMessages] = useState([]);
    const { postId } = useParams();
    const [chatRoomId, setChatRoomId] = useState(null);
    const [client, setClient] = useState(null);

    const loadChatRoom = useCallback(async () => {
        try {
            const roomResponse = await getChatRoomByPostId(postId);
            const data = roomResponse.data;
            if (data.chatRoomId) {
                setChatRoomId(data.chatRoomId);
                const messagesResponse = await getMessagesByRoomId(data.chatRoomId);
                const existingMessages = messagesResponse.data;
                setMessages(existingMessages.map(msg => ({
                    ...msg,
                    direction: msg.userId === 'currentUserId' ? 'outgoing' : 'incoming' // Replace 'currentUserId' with actual logic to determine the user
                })));
                setupWebSocket(data.chatRoomId);
            } else {
                console.error('Chat room not found for the provided post ID');
            }
        } catch (error) {
            console.error('Error fetching chat room:', error);
        }
    }, [postId]);

    useEffect(() => {
        loadChatRoom();
        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [postId, client, loadChatRoom]);

    const setupWebSocket = (roomId) => {
        const socket = new SockJS('http://localhost:8005/ws');

        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
        });

        stompClient.onConnect = () => {
            stompClient.subscribe(`/topic/chatroom/${roomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages(prev => [...prev, { ...receivedMessage, direction: "incoming" }]);
            });
        };

        stompClient.activate();
        setClient(stompClient);
    };

    const sendMessage = async (input) => {
        if (input.trim() && client && client.active && chatRoomId) {
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
                <ChatContainer style={{ border: '1px solid #ccc', borderRadius: '5px', width: '400px' }}>
                    <MessageList>
                        {messages.map((msg, index) => (
                            <Message key={index} model={{ message: msg.message, direction: msg.direction }} />
                        ))}
                    </MessageList>
                    <MessageInput placeholder="Type message here" onSend={sendMessage} />
                </ChatContainer>
            </MainContainer>
        </div>
    );
}

export default ChatRoom;
