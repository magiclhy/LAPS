import React from 'react';
import { BrowserRouter as Router, Route, Routes, useLocation,Navigate } from 'react-router-dom';
import LoginComponent from './components/LoginComponent';
// import LeaveListComponent from './components/LeaveListComponent';
// import CreateLeaveComponent from './components/CreateLeaveComponent';
// import ApproveLeaveComponent from './components/ApproveLeaveComponent';
import './App.css';

function Header() {
    return (
        <header className="App-header">
            <img src={require('./logo.svg').default} className="App-logo" alt="logo" />
            <p>
                Edit <code>src/App.js</code> and save to reload.
            </p>
            <a
                className="App-link"
                href="https://reactjs.org"
                target="_blank"
                rel="noopener noreferrer"
            >
                Learn React
            </a>
        </header>
    );
}

function App() {
    const location = useLocation();

    return (
        <div className="App">
            {location.pathname !== '/login' && <Header />}
            <Routes>
                <Route path="/login" element={<LoginComponent />} />
                {/* <Route path="/leaves" element={<LeaveListComponent />} /> */}
                {/* <Route path="/create-leave" element={<CreateLeaveComponent />} /> */}
                {/* <Route path="/approve-leave" element={<ApproveLeaveComponent />} /> */}
                <Route path="/" element={<Navigate to="/login" />} />
            </Routes>
        </div>
    );
}

function AppWrapper() {
    return (
        <Router>
            <App />
        </Router>
    );
}

export default AppWrapper;





