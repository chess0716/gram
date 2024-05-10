import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PostList from './PostList';
import PostDetail from './PostDetail';
import ChatRoom from './ChatRoom';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<PostList />} />
                <Route path="/posts/:id" element={<PostDetail />} />
                <Route path="/chat/:postId" element={<ChatRoom />} />
            </Routes>
        </Router>
    );
}

export default App;
