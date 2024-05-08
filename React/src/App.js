import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header/Header";
import AboutUs from './components/AboutUs/AboutUs';
import Places from "./components/Places/Places";
import DetailPage from "./components/Event/DetailPage"; // 상세 페이지 컴포넌트

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
          <Route path="/details/:index" element={<DetailPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
