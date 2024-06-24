import React, { useState } from 'react';
import axios from 'axios';

const CreateLeaveComponent = () => {
    const [type, setType] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/leaves/create', {
                type,
                startDate,
                endDate
            });
            alert('Leave created successfully');
        } catch (error) {
            console.error('Failed to create leave', error);
            alert('Failed to create leave');
        }
    };

    return (
        <div>
            <h2>Create Leave</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Type:</label>
                    <input
                        type="text"
                        value={type}
                        onChange={(e) => setType(e.target.value)}
                    />
                </div>
                <div>
                    <label>Start Date:</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                    />
                </div>
                <div>
                    <label>End Date:</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                    />
                </div>
                <button type="submit">Create Leave</button>
            </form>
        </div>
    );
};

export default CreateLeaveComponent;
