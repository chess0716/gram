import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PostList from './PostList';
import PostDetail from './PostDetail';
import ChatRoom from './ChatRoom.tsx'; // 파일 확장자 명시적으로 추가
import AuthForm from './AuthForm'; // 로그인 폼 추가

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<PostList />} />
                <Route path="/posts/:id" element={<PostDetail />} />
                <Route path="/chat/:postId" element={<ChatRoom />} />
                <Route path="/auth/join" element={<AuthForm />} /> 
            </Routes>
        </Router>
    );
}

export default App;
