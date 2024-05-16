import React, { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import {
  MainContainer,
  ChatContainer,
  MessageList,
  Message,
  MessageInput,
  Sidebar,
  ConversationHeader,
  Avatar,
  ConversationList,
  Conversation,
  MessageModel
} from "@chatscope/chat-ui-kit-react";
import { getChatRoomByPostId, getMessagesByRoomId, sendMessageToRoom, getMembersByRoomId } from "../../service/ChatService";
import "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";



interface Members {
  mno: number;
  id: string;
  name: string;
  email: string;
}

function ChatRoom() {
  const [messages, setMessages] = useState<any[]>([]);
  const [members, setMembers] = useState<Members[]>([]);
  const { postId } = useParams<{ postId: string }>();
  const [chatRoomId, setChatRoomId] = useState<string | null>(null);
  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    const loadChatRoom = async () => {
      try {
        const roomResponse = await getChatRoomByPostId(postId!);
        const data = roomResponse.data;
        if (data.chatRoomId) {
          setChatRoomId(data.chatRoomId);
          const messagesResponse = await getMessagesByRoomId(data.chatRoomId);
          setMessages(
            messagesResponse.data.map((msg: any) => ({
              ...msg,
              direction: msg.userId === "currentUserId" ? "outgoing" : "incoming",
            }))
          );
          const membersResponse = await getMembersByRoomId(data.chatRoomId);
          setMembers(membersResponse.data);
          setupWebSocket(data.chatRoomId);
        } else {
          console.error("Chat room not found for the provided post ID");
        }
      } catch (error) {
        console.error("Error fetching chat room:", error);
      }
    };

    loadChatRoom();

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
      }
    };
  }, [postId]);

  const setupWebSocket = (roomId: string) => {
    const socket = new SockJS("http://localhost:8005/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 10000,
    });

    stompClient.onConnect = () => {
      stompClient.subscribe(`/topic/chat/${roomId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prev) => [...prev, { ...receivedMessage, direction: "incoming" }]);
      });
      stompClient.subscribe(`/topic/chat/${roomId}/members`, (message) => {
        const updatedMembers = JSON.parse(message.body);
        setMembers(updatedMembers);
      });

      const token = localStorage.getItem("token");
      if (token) {
        stompClient.publish({
          destination: `/app/chat/${roomId}/join`,
          body: token
        });
      } else {
        console.error("User token is missing");
      }
    };

    stompClient.onStompError = (frame) => {
      console.error(`Broker reported error: ${frame.headers.message}`);
      console.error(`Additional details: ${frame.body}`);
    };

    stompClient.activate();
    clientRef.current = stompClient;
  };

  const sendMessage = async (input: string) => {
    if (input.trim() && clientRef.current && clientRef.current.active && chatRoomId) {
      const messageData = { message: input };
      try {
        await sendMessageToRoom(chatRoomId, messageData);
        setMessages((prev) => [...prev, { message: input, direction: "outgoing" }]);
      } catch (error) {
        console.error("Error sending message:", error);
      }
    }
  };

  return (
    <div className="container">
      <MainContainer className="main-container">
        <Sidebar position="left" scrollable={true} className="custom-sidebar">
          <ConversationList>
            <ConversationHeader>
              <ConversationHeader.Content userName="User list" />
            </ConversationHeader>
            {members.map((member) => (
              <Conversation key={member.mno} name={member.name}>
                <Avatar name={member.name} />
              </Conversation>
            ))}
          </ConversationList>
        </Sidebar>
        <ChatContainer className="custom-chat-container">
          <MessageList>
            {messages.map((msg, index) => (
              <Message
                key={index}
                model={{
                  message: msg.message,
                  direction: msg.direction,
                  sender: msg.name,
                } as MessageModel}
              />
            ))}
          </MessageList>
          <MessageInput placeholder="Type message here" onSend={sendMessage} />
        </ChatContainer>
      </MainContainer>
    </div>
  );
}

export default ChatRoom;
