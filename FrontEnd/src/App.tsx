import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AuthCard from "./components/authentication/AuthCard";
import RegistryCard from "./components/registry/RegistryCard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<AuthCard />} />
        <Route path="/register" element={<RegistryCard />} />
      </Routes>
    </Router>
  );
}

export default App;
