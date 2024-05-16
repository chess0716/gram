import axios from 'axios';

const API_URL = 'http://localhost:8005/api/sports';

export const fetchSports = async () => {
    try {
        const response = await axios.get(API_URL);
        return response.data;
    } catch (error) {
        console.error('Failed to fetch sports:', error);
        throw error;
    }
};

export const fetchSportById = async (id) => {
    try {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error('Failed to fetch sport by ID:', error);
        throw error;
    }
};
