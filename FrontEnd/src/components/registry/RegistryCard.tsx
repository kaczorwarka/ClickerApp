import { useState } from "react";
import AuthForm from "../AuthForm";
import Alert from "../Alert";
import Button from "../Button";
import { useNavigate } from "react-router-dom";

function RegistryCard() {
    const setUser = async () => {
      await fetch("http://localhost:8080/api/auth/registry", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
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
          } else if (response.status === 403) {
            throw Error("Wrong login data");
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
          Authorization: `Bearer ${localToken}`,
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else if (response.status === 403) {
            throw Error("Your tocken has expired");
          }
        })
        .then((data) => {
          setAlertType("alert-success");
          setAlertVisible(true);
          setAltertText(`Hello ${data.firstName}`);
          setFirstName("");
          setLastName("");
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

  const goBack = () => {
    navigate("/");
  };

  const handleSubmit = () => {
    setUser();
  };

  const navigate = useNavigate();

  let [email, setEmail] = useState("");
  let [password, setPassword] = useState("");
  let [firstName, setFirstName] = useState("");
  let [lastName, setLastName] = useState("");
  let [alertVisible, setAlertVisible] = useState(false);
  let [alertText, setAltertText] = useState("");
  let [alertType, setAlertType] = useState("");

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

  let buttons = [
    { type: "btn-primary", value: "Sign In", onAction: handleSubmit },
    { type: "btn-outline-danger", value: "Go Back", onAction: goBack },
  ];

  return (
    <div className="container-fluid vh-100 bg-primary-subtle">
      <div className="position-absolute top-50 start-50 translate-middle bg-white p-4 w-50 rounded shadow">
        <form>
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

export default RegistryCard;
