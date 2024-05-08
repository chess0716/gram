import axios from 'axios';

const BASE_URL = 'http://localhost:8088/api/sports'; // Adjust the port and route according to your Spring Boot app

export const fetchSports = async () => {
  try {
    const response = await axios.get(BASE_URL);
    return response.data;
  } catch (error) {
    console.error('Failed to fetch sports:', error);
    return [];
  }
};

export const fetchSportById = async (id) => {
  try {
    const response = await axios.get(`${BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Failed to fetch sport with id ${id}:`, error);
    return null;
  }
};
