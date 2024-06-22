import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginComponent from './components/LoginComponent';
import LeaveListComponent from './components/LeaveListComponent';
import CreateLeaveComponent from './components/CreateLeaveComponent';
import ApproveLeaveComponent from './components/ApproveLeaveComponent';
import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/login" element={<LoginComponent />} />
                    <Route path="/leaves" element={<LeaveListComponent />} />
                    <Route path="/create-leave" element={<CreateLeaveComponent />} />
                    <Route path="/approve-leave" element={<ApproveLeaveComponent />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;




