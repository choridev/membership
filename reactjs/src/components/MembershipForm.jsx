import { useState } from "react";
import { addMembership } from "../services/api";

export default function MembershipForm({ onAdd }) {
    const [type, setType] = useState("");
    const [point, setPoint] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        await addMembership({ membershipType: type, point: Number(point) });
        onAdd();
        setType("");
        setPoint("");
    };

    return (
        <form onSubmit={handleSubmit} className="form">
            <label>
                Membership Type:
                <input value={type} onChange={(e) => setType(e.target.value)} required />
            </label>
            <label>
                Point:
                <input
                    type="number"
                    value={point}
                    onChange={(e) => setPoint(e.target.value)}
                    required
                />
            </label>
            <button type="submit">추가</button>
        </form>
    );
}
