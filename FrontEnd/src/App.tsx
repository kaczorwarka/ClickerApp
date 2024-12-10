import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AuthCard from "./components/authentication/AuthCard";
import RegistryCard from "./components/registry/RegistryCard";
import Main from "./components/main/Main";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<AuthCard />} />
        <Route path="/register" element={<RegistryCard />} />
        <Route path="/main" element={<Main />} />
      </Routes>
    </Router>
  );
}

export default App;
