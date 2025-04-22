import { useState } from "react";
import MembershipForm from "./components/MembershipForm";
import MembershipList from "./components/MembershipList";
import MembershipDetail from "./components/MembershipDetail";
import "./App.css";

function App() {
  const [selectedId, setSelectedId] = useState(null);

  return (
    <div className="container">
      <h1>🧾 Membership 관리</h1>
      <p>포인트 유형: NAVER, KAKAO, LINE</p>
      <MembershipForm onAdd={() => window.location.reload()} />
      <MembershipList onSelect={setSelectedId} />
      {selectedId && (
        <MembershipDetail id={selectedId} onClose={() => setSelectedId(null)} />
      )}
    </div>
  );
}

export default App;
