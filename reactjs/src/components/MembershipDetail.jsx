import { useEffect, useState } from "react";
import { getMembership, addPoint } from "../services/api";

export default function MembershipDetail({ id, onClose }) {
    const [data, setData] = useState(null);
    const [point, setPoint] = useState("");

    const fetchDetail = async () => {
        const res = await getMembership(id);
        setData(res.data);
    };

    useEffect(() => {
        if (id) fetchDetail();
    }, [id]);

    const handleAddPoint = async () => {
        await addPoint(id, { point: Number(point) });
        setPoint("");
        fetchDetail();
    };

    if (!data) return null;

    return (
        <div className="detail">
            <h2>🔍 상세 정보</h2>
            <p>타입: {data.membershipType}</p>
            <p>포인트: {data.point}P</p>
            <input
                type="number"
                value={point}
                onChange={(e) => setPoint(e.target.value)}
                placeholder="추가할 포인트"
            />
            <button onClick={handleAddPoint}>포인트 추가</button>
            <button onClick={onClose}>닫기</button>
        </div>
    );
}
