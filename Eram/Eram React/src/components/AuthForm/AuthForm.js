import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../../service/AuthService";
import styles from "./AuthForm.module.css";

function AuthForm() {
  const [isRightPanelActive, setIsRightPanelActive] = useState(false);
  const [id, setId] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [mobile, setMobile] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [isSignUp, setIsSignUp] = useState(false);
  const navigate = useNavigate();

  const handleSignUpSuccess = () => {
    setIsSignUp(false);
    setError("");
    setIsRightPanelActive(false);
  };

  const handleSignUp = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await authService.signup({ id, email, password, name, mobile });
      setError("");
      setLoading(false);
      handleSignUpSuccess();
    } catch (err) {
      setError("Signup failed");
      setLoading(false);
    }
  };

  const handleSignIn = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await authService.login({ email, password });
      setError("");
      setLoading(false);
      setEmail("");
      setPassword("");
      setId("");
      setName("");
      setMobile("");
      navigate("/");
    } catch (err) {
      setError("Login failed");
      setLoading(false);
    }
  };

  const toggleForm = (isSignUp) => {
    setIsSignUp(isSignUp);
    setError("");
    setIsRightPanelActive(isSignUp);
  };

  return (
    <div className={styles.authForm}>
      <div
        className={`${styles.container} ${isRightPanelActive ? styles.containerRightPanelActive : ""}`}
        id="container"
      >
        {isSignUp ? (
          <div className={`${styles.formContainer} ${styles.signUpContainer}`}>
            <form onSubmit={handleSignUp}>
              <h1>회원가입</h1>
              <span>or use your email for registration</span>
              <input
                type="text"
                placeholder="Id"
                value={id}
                onChange={(e) => setId(e.target.value)}
              />
              <input
                type="text"
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <input
                type="text"
                placeholder="Mobile"
                value={mobile}
                onChange={(e) => setMobile(e.target.value)}
              />
              <button type="submit" disabled={loading}>
                Sign Up
              </button>
              {error && <p>{error}</p>}
            </form>
          </div>
        ) : (
          <div className={`${styles.formContainer} ${styles.signInContainer}`}>
            <form onSubmit={handleSignIn}>
              <h1>로그인</h1>
              <span>or use your account</span>
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <a href="#">Forgot your password?</a>
              <button type="submit" disabled={loading}>
                Sign In
              </button>
              {error && <p>{error}</p>}
            </form>
          </div>
        )}
        <div className={styles.overlayContainer}>
          <div className={styles.overlay}>
            <div className={`${styles.overlayPanel} ${styles.overlayLeft}`}>
              <h1>Welcome Back!</h1>
              <p>
                To keep connected with us please login with your personal info
              </p>
              <button
                className={styles.ghost}
                id="signIn"
                onClick={() => toggleForm(false)}
                type="button"
              >
                Sign In
              </button>
            </div>
            <div className={`${styles.overlayPanel} ${styles.overlayRight}`}>
              <h1>Hello, Friend!</h1>
              <p>Enter your personal details and start journey with us</p>
              <button
                className={styles.ghost}
                id="signUp"
                onClick={() => toggleForm(true)}
                type="button"
              >
                Sign Up
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AuthForm;
