import { useState } from "react";
import AuthForm from "../AuthForm";
import Alert from "../Alert";
import Button from "../Button";
import { useNavigate } from "react-router-dom";

function AuthCard() {
  const getToken = async () => {
    await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 400){
          throw Error("Wrong login data");
        } else if (response.status === 403) {
          throw Error("Wrong login data");
        } else {
          throw Error(`${response.status}`);
        }
      })
      .then((data) => {
        sessionStorage.setItem("token", data.token);
        getUser(data.token);
      })
      .catch((err) => {
        setAlertType("alert-danger");
        setAlertVisible(true);
        setAltertText(err.message);
      });
  };

  const getUser = async (localToken: String) => {
    await fetch(`http://localhost:8080/api/user/${email}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localToken}`,
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          throw Error("Your tocken has expired");
        } else {
          throw Error(`${response.status}`);
        }
      })
      .then((data) => {
        setAlertType("alert-success");
        setAlertVisible(true);
        setAltertText(`Hello ${data.firstName}`);
        setEmail("");
        setPassword("");
        sessionStorage.setItem(
          "user",
          JSON.stringify({
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            amountOfLives: data.amountOfLives,
          })
        );

        navigate("/main");
      })
      .catch((err) => {
        setAlertType("alert-danger");
        setAlertVisible(true);
        setAltertText(err.message);
      });
  };

  const registry = () => {
    navigate("/register");
  };

  const handleSubmit = () => {
    getToken();
  };

  const navigate = useNavigate();

  let [email, setEmail] = useState("");
  let [password, setPassword] = useState("");
  let [alertVisible, setAlertVisible] = useState(false);
  let [alertText, setAltertText] = useState("");
  let [alertType, setAlertType] = useState("");

  let forms = [
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

  let buttons = [
    { type: "btn-primary", value: "Log In", onAction: handleSubmit },
    { type: "btn-outline-success", value: "Registry", onAction: registry },
  ];

  return (
    <div className="container-fluid vh-100 bg-primary-subtle">
      <div className="position-absolute top-50 start-50 translate-middle bg-white p-4 w-50 rounded shadow">
        <form>
          <h1>Log In</h1>
          {forms.map((form, index) => (
            <AuthForm
              dynamicType={form.type}
              dynamicPlaceHolder={form.placeHolder}
              dynamicLabel={form.label}
              dynamocId={index.toString()}
              value={form.value}
              setValue={form.setValue}
            />
          ))}
          <div className="d-flex justify-content-evenly">
            {buttons.map((button) => (
              <Button
                buttonType={button.type}
                buttonValue={button.value}
                buttonAction={button.onAction}
              />
            ))}
          </div>
          {alertVisible && (
            <Alert
              alertType={alertType}
              alertText={alertText}
              setVisible={setAlertVisible}
            />
          )}
        </form>
      </div>
    </div>
  );
}

export default AuthCard;
