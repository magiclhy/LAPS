import React, { useState } from 'react';
import axios from 'axios';

const LoginComponent = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8080/', {
         username,
         password
      }, {
        withCredentials: true
      });
      alert('Login successful');
    } catch (error) {
      alert('Login failed');
      console.error('There was an error!', error);
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <div>
        <label>Username:</label>
        <input 
          type="text" 
          value={username} 
          onChange={(e) => setUsername(e.target.value)} 
        />
      </div>
      <div>
        <label>Password:</label>
        <input 
          type="password" 
          value={password} 
          onChange={(e) => setPassword(e.target.value)} 
        />
      </div>
      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default LoginComponent;

