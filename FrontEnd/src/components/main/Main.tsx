import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import NavBar from "./NavBar";
import AuthForm from "../AuthForm";
import LabelForm from "../LabelForm";
import Button from "../Button";
import Score from "./Score";
import Alert from "../Alert";

interface User {
  firstName: string;
  lastName: string;
  email: string;
  amountOfLives: number;
  password: string;
}

interface Game {
  id: string;
  score: number;
  gameDate: Date;
  userName: string;
}

function Main() {
  const handleDelete = () => {
    deleteUser();
  };

  const goToModify = () => {
    setUserUpdate(true);
  };

  const handleModify = () => {
    modifyUser();

    setUserUpdate(false);
  };

  const handleDeleteGame = (id: string) => {
    deleteGame(id);
  }

  let [user, setUser] = useState<User>();
  let [token, setToken] = useState("");
  let [userUpdate, setUserUpdate] = useState(false);
  let [email, setEmail] = useState("");
  let [password, setPassword] = useState("");
  let [firstName, setFirstName] = useState("");
  let [lastName, setLastName] = useState("");
  let [alertVisible, setAlertVisible] = useState(false);
  let [alertText, setAltertText] = useState("");
  let [alertType, setAlertType] = useState("");
  let [globalGames, setGlobalGames] = useState<Game[]>([])
  let [userGames, setUserGames] = useState<Game[]>([])
  let userString = sessionStorage.getItem("user");
  let tokenString = sessionStorage.getItem("token");
  const navigate = useNavigate();

  let forms = [
    {
      type: "text",
      placeHolder: "first name",
      label: "First Name",
      value: firstName,
      setValue: setFirstName,
    },
    {
      type: "text",
      placeHolder: "last name",
      label: "Last Name",
      value: lastName,
      setValue: setLastName,
    },
    {
      type: "email",
      placeHolder: "name@example.com",
      label: "Email",
      value: email,
      setValue: setEmail,
    },
    {
      type: "password",
      placeHolder: "Password",
      label: "Password",
      value: password,
      setValue: setPassword,
    },
  ];

  let labels = [user?.firstName, user?.lastName, user?.email];
  let buttons1 = [
    { type: "btn-primary", value: "Modify", onAction: goToModify },
    { type: "btn-outline-danger", value: "Delete", onAction: handleDelete },
  ];

  let buttons2 = [
    { type: "btn-success", value: "Accept", onAction: handleModify },
    { type: "btn-outline-danger", value: "Delete", onAction: handleDelete },
  ];

  const logOut = () => {
    sessionStorage.clear();
    navigate("/");
  };

  const deleteUser = async () => {
    if (user) {
      await fetch(`http://localhost:8080/api/user/${user.email}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((response) => {
          if (response.status === 204) {
            logOut();
          } else if (response.status === 403) {
            throw Error("Your tocken has expired");
          } else {
            throw Error(`${response.status}`);
          }
        })
        .catch((err) => {
          setAlertType("alert-danger");
          setAlertVisible(true);
          setAltertText(err.message);
        });
    }
  };

  const modifyUser = async () => {
    if (user) {
      await fetch(`http://localhost:8080/api/user/${user.email}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          firstName: firstName,
          lastName: lastName,
          email: email,
          password: password,
        }),
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else if (response.status === 400) {
            throw Error("Wrong login data");
          } else if (response.status === 404) {
            logOut();
          } else if (response.status === 403) {
            throw Error("Your tocken has expired");
          } else {
            throw Error(`${response.status}`);
          }
        })
        .then((data) => {
          setEmail("");
          setPassword("");
          setFirstName("");
          setLastName("");

          sessionStorage.setItem(
            "user",
            JSON.stringify({
              firstName: data.firstName,
              lastName: data.lastName,
              email: data.email,
              amountOfLives: data.amountOfLives,
            })
          );

          setAlertType("alert-success");
          setAlertVisible(true);
          setAltertText("Success");
        })
        .catch((err) => {
          setAlertType("alert-danger");
          setAlertVisible(true);
          setAltertText(err.message);
        });
    }
  };

  const getGlobalGames = async (numberOfGames: number, localToken: string) => {
    await fetch(`http://localhost:8080/api/games/bestGlobal/${numberOfGames}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localToken}`,
      },
    })
      .then((response) => {
        if (response.ok) return response.json();
      })
      .then((data) => {
        const games = data.map((game: any) => ({
          id: game.id,
          score: game.score,
          gameDate: new Date(game.gameDate),
          userName: game.userName,
        }));
        setGlobalGames(games);
      });
  };

  const getUserGames = async (localToken: string, email: string) => {
    await fetch(`http://localhost:8080/api/games/user/${email}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localToken}`,
      },
    })
      .then((response) => {
        if (response.ok) return response.json();
      })
      .then((data) => {
        const games = data.map((game: any) => ({
          id: game.id,
          score: game.score,
          gameDate: new Date(game.gameDate),
          userName: '',
        }));
        setUserGames(games);
        getGlobalGames(30, localToken);
      });
  };

  const deleteGame = async (id: string) => {
    await fetch("http://localhost:8080/api/games", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        id: id 
      })
    }).then((response) => {
      if (response.status === 204 && user) {
        getUserGames(token, user?.email);
      }
    })
  }

  const makePayment = () => {
    if (user) {
      setUser({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        amountOfLives: user.amountOfLives + 1,
        password: user.password,
      });
    }
  };

  useEffect(() => {
    if (userString !== null && tokenString !== null) {
      setUser(JSON.parse(userString));
      setToken(tokenString);
      getUserGames(tokenString, JSON.parse(userString).email)
    } else {
      logOut();
    }
  }, [navigate, userString, tokenString]);

  const userData = () => {
    if (userUpdate)
      return forms.map((form, index) => (
        <AuthForm
          dynamicType={form.type}
          dynamicPlaceHolder={form.placeHolder}
          dynamicLabel={form.label}
          dynamocId={index.toString()}
          value={form.value}
          setValue={form.setValue}
        />
      ));
    else
      return labels.map((label, index) => (
        <LabelForm dynamocId={index.toString()} value={label ?? ""} />
      ));
  };

  const getButtons = () => {
    if (userUpdate)
      return buttons2.map((button) => (
        <Button
          buttonType={button.type}
          buttonValue={button.value}
          buttonAction={button.onAction}
        />
      ));
    else
      return buttons1.map((button) => (
        <Button
          buttonType={button.type}
          buttonValue={button.value}
          buttonAction={button.onAction}
        />
      ));
  };

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
      <div className="d-flex align-items-center flex-grow-1">
        <div className="container">
          <div
            className="row g-5 justify-content-center"
            style={{ height: "50vh" }}
          >
            <div className="col-12 col-md-5 col-lg-4">
              <div className="bg-white p-3 rounded shadow">
                <form>{userData()}</form>
                <div className="d-flex justify-content-evenly">
                  {getButtons()}
                </div>
                {alertVisible && (
                  <Alert
                    alertType={alertType}
                    alertText={alertText}
                    setVisible={setAlertVisible}
                  />
                )}
              </div>
            </div>
            <div className="col-12 col-md-5 col-lg-4">
              <div className="border border-white rounded">
                <div className="mb-3 bg-white p-1 text-center">
                  <p>
                    <strong>Global Ranking</strong>
                  </p>
                </div>
                <div
                  className="p-3"
                  style={{ height: "300px", overflowY: "auto" }}
                >
                  <ul className="list-unstyled">
                    {globalGames.map((game, index) => (
                       <li key={index} className="mb-3">
                       <Score
                         date={game.gameDate.toLocaleDateString()}
                         user={game.userName}
                         score={game.score}
                         buttonType={""}
                         buttonValue={""}
                         isButtonEnabled={false}
                         buttonAction={() => {}}
                       />
                     </li>
                    ))}
                  </ul>
                </div>
              </div>
            </div>
            <div className="col-12 col-md-5 col-lg-4">
              <div className="border border-white rounded">
                <div className="mb-3 bg-white p-1 text-center">
                  <p>
                    <strong>Personal Ranking</strong>
                  </p>
                </div>
                <div
                  className="p-3"
                  style={{ height: "300px", overflowY: "auto" }}
                >
                  <ul className="list-unstyled">
                  {userGames.map((game, index) => (
                       <li key={index} className="mb-3">
                       <Score
                         date={game.gameDate.toLocaleDateString()}
                         user=""
                         score={game.score}
                         buttonType={"btn-danger"}
                         buttonValue={"Delete"}
                         isButtonEnabled={true}
                         buttonAction={() => handleDeleteGame(game.id)}
                       />
                     </li>
                    ))}
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default Main;
