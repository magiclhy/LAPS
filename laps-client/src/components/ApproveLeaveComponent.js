import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ApproveLeaveComponent = () => {
    const [leaves, setLeaves] = useState([]);

    useEffect(() => {
        const fetchPendingLeaves = async () => {
            try {
                const response = await axios.get('http://localhost:8080/leaves/viewLeaveForApproval');
                setLeaves(response.data);
            } catch (error) {
                console.error('Failed to fetch pending leaves', error);
            }
        };

        fetchPendingLeaves();
    }, []);

    const approveLeave = async (leaveId) => {
        try {
            await axios.post(`http://localhost:8080/leaves/approve/${leaveId}`);
            alert('Leave approved successfully');
        } catch (error) {
            console.error('Failed to approve leave', error);
            alert('Failed to approve leave');
        }
    };

    return (
        <div>
            <h2>Approve Leave</h2>
            <ul>
                {leaves.map((leave) => (
                    <li key={leave.id}>
                        {leave.type} from {leave.startDate} to {leave.endDate}
                        <button onClick={() => approveLeave(leave.id)}>Approve</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ApproveLeaveComponent;
