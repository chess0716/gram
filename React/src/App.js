import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header/Header";
import AboutUs from './components/AboutUs/AboutUs';
import Places from "./components/Places/Places";
import DetailPage from "./components/Event/DetailPage"; // 상세 페이지 컴포넌트
import PostList from './components/Posts/PostList'; // PostList 컴포넌트 import
import PostDetail from './components/'; // PostDetail 컴포넌트 import
import ChatRoom from './components/ChatRoom/ChatRoom'; // ChatRoom 컴포넌트 import
import AuthForm from './components/AuthForm/AuthForm'; // AuthForm 컴포넌트 import

function App() {
  return (
    <Router>
      <div className="holder">
        <Header />
        <Routes>
          <Route path="/" element={
            <>
              <Places />
              <AboutUs />
            </>
          } />
          <Route path="/details/:index" element={<DetailPage />} /> {/* 기존 DetailPage 라우트 유지 */}
          <Route path="/posts" element={<PostList />} /> {/* PostList 라우트 추가 */}
          <Route path="/posts/:id" element={<PostDetail />} /> {/* PostDetail 라우트 추가 */}
          <Route path="/chat/:postId" element={<ChatRoom />} /> {/* ChatRoom 라우트 추가 */}
          <Route path="/auth/join" element={<AuthForm />} /> {/* AuthForm 라우트 추가 */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
