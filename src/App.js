import React from 'react';
import Login from './components/Login';
import Register from './components/Register';

const App = () => {
  return (
    <div>
      <h1>Welcome to Mental.ly</h1>
      <Login />
      <Register />
    </div>
  );
};

export default App;
