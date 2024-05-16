import axios from "axios";

const API_URL = "http://localhost:8005"; // 스프링 서버 주소

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

const AuthService = {
  // 로그인 함수
  login: async (data) => {
    try {
      const response = await axiosInstance.post("/auth/login", data);

      if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("user", JSON.stringify({
          id: response.data.user.id,
          email: response.data.user.email,
          name: response.data.user.name,
        }));
      }

      return response.data;
    } catch (error) {
      console.error("Login failed:", error);
      throw new Error("Login failed");
    }
  },

  // 가입 함수
  signup: async (data) => {
    try {
      const response = await axiosInstance.post("/auth/join", data);
      return response.data;
    } catch (error) {
      console.error("Signup failed:", error);
      throw new Error("Signup failed");
    }
  },

  // 회원 정보 수정 함수
  updateProfile: async (data) => {
    try {
      const response = await axiosInstance.put("/members/update", data);
      return response.data;
    } catch (error) {
      console.error("Profile update failed:", error);
      throw new Error("Profile update failed");
    }
  }
};

export default AuthService;
