import axios from 'axios';

const BASE_URL = 'http://localhost:8005'; // Spring Boot 서버의 포트

export const getPosts = () => {
    return axios.get(`${BASE_URL}/posts`);
};

export const getPostById = (id) => {
    return axios.get(`${BASE_URL}/posts/${id}`);
};

export const createPost = (postData) => {
    return axios.post(`${BASE_URL}/posts`, postData);
};

export const updatePost = (id, postData) => {
    return axios.put(`${BASE_URL}/posts/${id}`, postData);
};

export const deletePost = (id) => {
    return axios.delete(`${BASE_URL}/posts/${id}`);
};
