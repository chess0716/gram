import React, { useState, useEffect } from 'react';
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

    useEffect(() => {
        getChatRoomByPostId(postId)
            .then(response => {
                const data = response.data;
                if (data.chatRoomId) {
                    setChatRoomId(data.chatRoomId);
                    getMessagesByRoomId(data.chatRoomId)
                        .then(response => {
                            const existingMessages = response.data;
                            setMessages(existingMessages.map(msg => ({
                                ...msg,
                                direction: 'incoming' // 임시로 모든 메시지를 'incoming'으로 설정
                            })));
                        });

                    const socket = new SockJS('http://localhost:8005/ws');
                    const stompClient = new Client({
                        webSocketFactory: () => socket,
                        reconnectDelay: 5000,
                    });

                    stompClient.onConnect = () => {
                        stompClient.subscribe(`/topic/chatroom/${data.chatRoomId}`, (message) => {
                            const receivedMessage = JSON.parse(message.body);
                            setMessages(prev => [...prev, { ...receivedMessage, direction: "incoming" }]);
                        });
                    };

                    stompClient.activate();
                    setClient(stompClient);
                } else {
                    console.error('Chat room not found for the provided post ID');
                }
            })
            .catch(error => console.error('Error fetching chat room:', error));

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [postId]);

    const sendMessage = (input) => {
        if (input.trim() && client && client.active && chatRoomId) {
            const messageData = { message: input };
            sendMessageToRoom(chatRoomId, messageData)
                .then(() => setMessages(prev => [...prev, { message: input, direction: "outgoing" }]))
                .catch(error => console.error('Error sending message:', error));
        }
    };

    return (
        <div style={{ height: "500px", position: "relative" }}>
            <MainContainer>
                <ChatContainer>
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
