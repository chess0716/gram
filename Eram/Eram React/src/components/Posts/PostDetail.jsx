import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getPostById } from '../../service/PostService'; 

function PostDetail() {
    const [post, setPost] = useState(null);
    const { id } = useParams();

    useEffect(() => {
        getPostById(id)
            .then(response => {
                setPost(response.data);
            })
            .catch(error => console.error('Error fetching post:', error));
    }, [id]);

    if (!post) return <div>Loading...</div>;

    return (
        <div>
            <h1>{post.title}</h1>
            <p>{post.content}</p>
            <Link to={`/chat/${id}`} className="btn btn-primary">채팅방으로 이동</Link>
        </div>
    );
}

export default PostDetail;
