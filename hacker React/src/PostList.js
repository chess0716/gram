import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getPosts } from './PostService';

function PostsList() {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        getPosts()
            .then(response => {
                setPosts(response.data);
            })
            .catch(error => {
                console.error('Error fetching posts:', error);
            });
    }, []);

    return (
        <div>
            <h1>게시글 목록</h1>
            <ul>
                {posts.map(post => (
                    <li key={post.id}>
                        <Link to={`/posts/${post.id}`}>{post.title}</Link>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default PostsList;
