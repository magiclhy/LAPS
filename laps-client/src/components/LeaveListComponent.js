import React, { useState, useEffect } from 'react';
import axios from 'axios';

const LeaveListComponent = () => {
    const [leaves, setLeaves] = useState([]);

    useEffect(() => {
        const fetchLeaves = async () => {
            try {
                const response = await axios.get('http://localhost:8080/leaves/view');
                setLeaves(response.data);
            } catch (error) {
                console.error('Failed to fetch leaves', error);
            }
        };

        fetchLeaves();
    }, []);

    return (
        <div>
            <h2>Leave List</h2>
            <ul>
                {leaves.map((leave) => (
                    <li key={leave.id}>
                        {leave.type} from {leave.startDate} to {leave.endDate}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default LeaveListComponent;
