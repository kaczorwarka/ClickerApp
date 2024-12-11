import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import NavBar from "./NavBar";

interface User {
  firstName: String;
  lastName: String;
  email: String;
  amountOfLives: number;
}

function Main() {
  let [user, setUser] = useState<User>();
  let [token, setToken] = useState(null);
  const navigate = useNavigate();
  const userString = sessionStorage.getItem("user");
  const tokenString = sessionStorage.getItem("token");

  const logOut = () => {
    sessionStorage.clear();
    navigate("/");
  };

  const makePayment = () => {
    if (user) {
      setUser({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        amountOfLives: user.amountOfLives + 1,
      });
    }
  };

  useEffect(() => {
    if (userString !== null && tokenString !== null) {
      setUser(JSON.parse(userString));
      setToken(JSON.parse(tokenString));
    } else {
      navigate("/");
    }
  }, [navigate, userString, tokenString]);

  if (!user) {
    return <></>;
  }

  return (
    <div className="container-fluid vh-100 bg-primary-subtle  d-flex flex-column">
      <NavBar
        amountOfLives={user.amountOfLives}
        handleLogOut={logOut}
        handlePayment={makePayment}
      />
      <div></div>
      <div className="d-flex align-items-center flex-grow-1">
        <div className="container">
          <div className="row g-5 justify-content-center">
            <div className="col-12 col-md-4">
              <div className="bg-white p-4 rounded shadow">Box 1</div>
            </div>
            <div className="col-12 col-md-4">
              <div className="bg-white p-4 rounded shadow">Box 2</div>
            </div>
            <div className="col-12 col-md-4">
              <div className="bg-white p-4 rounded shadow">Box 3</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Main;
