import React, { useState, useEffect } from "react";
import authService from "../../service/AuthService";
import styled from "styled-components";

const ModifyProfileFormBlock = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f0f0;
`;

const FormContainer = styled.div`
  width: 100%;
  height: 100%;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h1`
  text-align: center;
  font-size: 25px;
`;

const ModifyProfileForm = () => {
  const [id, setId] = useState(0); // 기본값을 숫자로 설정
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [mobile, setMobile] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchUserData = async () => {
    try {
      setLoading(true);
      const userData = await authService.fetchUserData();
      setId(userData.mno); // ID를 숫자로 설정
      setName(userData.name);
      setEmail(userData.email);
      setMobile(userData.mobile);
      setLoading(false);
    } catch (error) {
      console.error("Failed to fetch user data:", error);
      setError("Failed to fetch user data");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  const handleProfileUpdate = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);
      await authService.updateProfile({
        mno: id,
        name,
        password: newPassword,
        mobile,
      });
      await fetchUserData();
      console.log("Profile updated successfully");
    } catch (error) {
      console.error("Profile update failed:", error);
      setError("Profile update failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <ModifyProfileFormBlock>
      <FormContainer>
        <Title>회원 정보 수정</Title>
        <form onSubmit={handleProfileUpdate}>
          <input type="number" placeholder="Id" value={id} readOnly />
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input type="email" placeholder="Email" value={email} readOnly />
          <input
            type="password"
            placeholder="New Password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <input
            type="text"
            placeholder="Mobile"
            value={mobile}
            onChange={(e) => setMobile(e.target.value)}
          />
          <button type="submit" disabled={loading}>
            Update Profile
          </button>
          {error && <p>{error}</p>}
        </form>
      </FormContainer>
    </ModifyProfileFormBlock>
  );
};

export default ModifyProfileForm;
