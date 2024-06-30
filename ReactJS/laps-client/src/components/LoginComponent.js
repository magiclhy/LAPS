import React, { useState } from 'react';
import axios from 'axios';

const LoginComponent = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/account/', {
         username,
         password
      }, {
        withCredentials: true
      });
      alert('Login successful');
      window.location.href = 'http://localhost:8080/home'; 
    } catch (error) {
      alert('Login failed');
      console.error('There was an error!', error);
    }
  };

  return (
    <div style={{ fontSize: '40px' }}>
      <h2>Login</h2>
      <div>
        <label>Username:</label>
        <input 
          type="text" 
          value={username} 
          onChange={(e) => setUsername(e.target.value)} 
          style={{ fontSize: '18px', padding: '5px' }}
        />
      </div>
      <div>
        <label>Password:</label>
        <input 
          type="password" 
          value={password} 
          onChange={(e) => setPassword(e.target.value)} 
          style={{ fontSize: '18px', padding: '5px' }}
        />
      </div>
      <button onClick={handleLogin} style={{ fontSize: '18px', padding: '10px 20px' }}>Login</button>
    </div>
  );
};

export default LoginComponent;

