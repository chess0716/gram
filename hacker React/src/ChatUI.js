import React from "react";

import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";

import {
  MainContainer,
  ChatContainer,
  MessageList,
  Message,
  MessageInput,
  Avatar,
} from "@chatscope/chat-ui-kit-react";

const AVATAR_IMAGE = "https://img1.daumcdn.net/thumb/C428x428/?scode=mtistory2&fname=https%3A%2F%2Ftistory3.daumcdn.net%2Ftistory%2F4431109%2Fattach%2F3af65be1d8b64ece859b8f6d07fafadc";
                  
const ChatUI = () => {
  return (
    <div>
      <div style={{ position: "relative", height: "500px" }}>
        <MainContainer>
          <ChatContainer>
            <MessageList>
              <Message
                model={{
                  direction: "incoming",
                  type: "custom",
                }}
              >
                <Avatar
                  src={AVATAR_IMAGE}
                  name="Akane"
                />
                <Message.CustomContent>
                  <strong>This is strong text</strong>
                  <br />
                  Message content is provided as{" "}
                  <span
                    style={{
                      color: "red",
                    }}
                  >
                    {" "}
                    custom elements
                  </span>{" "}
                  from child <strong>Message.CustomContent</strong> element
                </Message.CustomContent>
              </Message>
              <Message
                model={{
                  message: "Hello my friend",
                  sentTime: "15 mins ago",
                  direction: "outgoing",
                  position: "first",
                }}
              />
              <Message
                model={{
                  direction: "incoming",
                  payload: (
                    <Message.CustomContent>
                      <strong>This is strong text</strong>
                      <br />
                      Message content is provided as{" "}
                      <span
                        style={{
                          color: "red",
                        }}
                      >
                        {" "}
                        custom elements
                      </span>{" "}
                      from payload property
                    </Message.CustomContent>
                  ),
                }}
              >
                <Avatar
                  src={AVATAR_IMAGE}
                  name="Joe"
                />
              </Message>
            </MessageList>
            <MessageInput
              placeholder="Type message here"
            />
          </ChatContainer>
        </MainContainer>
      </div>
    </div>
  );
};

export default ChatUI;
