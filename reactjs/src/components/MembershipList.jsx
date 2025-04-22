import { useEffect, useState } from "react";
import { getMemberships, deleteMembership } from "../services/api";

export default function MembershipList({ onSelect }) {
    const [list, setList] = useState([]);

    const fetchData = async () => {
        const res = await getMemberships();
        setList(res.data);
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleDelete = async (id) => {
        await deleteMembership(id);
        fetchData();
    };

    return (
        <div className="list">
            <h2>📄 멤버십 목록</h2>
            <ul>
                {list.map((m) => (
                    <li key={m.id}>
                        {m.membershipType} - {m.point}P
                        <button onClick={() => onSelect(m.id)}>상세</button>
                        <button onClick={() => handleDelete(m.id)}>삭제</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
