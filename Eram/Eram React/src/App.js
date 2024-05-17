import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import Header from "./components/Header/Header";
import AboutUs from './components/AboutUs/AboutUs';
import Places from "./components/Places/Places";
import DetailPage from "./components/Event/DetailPage"; // 상세 페이지 컴포넌트
import PostList from "./components/Posts/PostList"; // PostList 컴포넌트
import PostDetail from "./components/Posts/PostDetail"; // PostDetail 컴포넌트
import ChatRoom from "./components/ChatRoom/ChatRoom"; // ChatRoom 컴포넌트
import AuthForm from "./components/AuthForm/AuthForm"; // AuthForm 컴포넌트

import styles from './App.css'; // CSS 모듈 import

const App = () => {
  const location = useLocation();

  return (
    <div className={styles.holder}>
      {location.pathname === '/' && <Header />}
      <Routes>
        <Route path="/" element={
          <>
            <Places />
            <AboutUs />
          </>
        } />
        <Route path="/details/:index" element={<DetailPage />} />
        <Route path="/posts" element={<PostList />} />
        <Route path="/posts/:id" element={<PostDetail />} />
        <Route path="/chat/:postId" element={<ChatRoom />} />
        <Route path="/auth/join" element={<AuthForm />} />
        <Route path="/auth/login" element={<AuthForm />} />
      </Routes>
    </div>
  );
};

const AppWrapper = () => (
  <Router>
    <App />
  </Router>
);

export default AppWrapper;
