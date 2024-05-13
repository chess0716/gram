import axios from 'axios';

const AuthService = {
  register: async (userData) => {
    try {
      const response = await axios.post('http://localhost:8005/auth/join', userData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  login: async (email, password) => {
    try {
      const response = await axios.post('http://localhost:8005/auth/login', { email, password });
      return response.data.token;
    } catch (error) {
      throw error;
    }
  },

  logout: async () => {
    try {
      const response = await axios.post('http://localhost:8005/auth/logout');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default AuthService;
