import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header/Header";
import AboutUs from './components/AboutUs/AboutUs';
import Places from "./components/Places/Places";
import DetailPage from "./components/Event/DetailPage"; // 이미지 클릭 시 이동할 컴포넌트 /예시 페이지 입니다.

function App() {
  return (
    <Router>
      <div className="holder">
        <Header />
        <AboutUs />
        <Places />

        <Routes>
          <Route path="/details/:index" element={<DetailPage />} />
        </Routes>
        
      </div>
    </Router>
  );
}

export default App;
