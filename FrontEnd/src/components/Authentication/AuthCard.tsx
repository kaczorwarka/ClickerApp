import { useState} from "react";
import AuthForm from "./AuthForm";
import Alert from "../../Alert";

function AuthCard() {
  let [email, setEmail] = useState("");
  let [password, setPassword] = useState("");
  let [alertVisible, setAlertVisible] = useState(false);
  let [alertText, setAltertText] = useState("");
  let [alertType, setAlertType] = useState("");

  let forms = [
    ["email", "name@example.com", "Email", email],
    ["password", "Password", "Password", password],
  ];

  let sets = [setEmail, setPassword];

  const handleSubmit = () => {
    getUser();
    setEmail("");
    setPassword("");
  };

  const getUser = async () => {
    await fetch("http://localhost:8080/api/user/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        "email": email,
        "password": password,
      }),
    })
      .then((response) => {
        if (response.status === 200) {

        } else if (response.status === 403) {
          throw Error("Wrong login data");
        }
      })
      .then((data) => {
        setAlertType('alert-success')
        setAlertVisible(true)
        setAltertText("GIT")
      })
      .catch((err) => {
        setAlertType('alert-danger')
        setAlertVisible(true)
        setAltertText(err.message)
      });
  };

  return (
    <div className="container-fluid vh-100 bg-primary-subtle">
      <div className="position-absolute top-50 start-50 translate-middle bg-white p-4 w-50 rounded shadow">
        <form>
          <h1>Log In</h1>
          {forms.map((form, index) => (
            <AuthForm
              dynamicType={form[0]}
              dynamicPlaceHolder={form[1]}
              dynamicLabel={form[2]}
              dynamocId={index.toString()}
              value={form[3]}
              setValue={sets[index]}
            />
          ))}
          <div className="d-flex justify-content-evenly">
            <input
              className="btn btn-primary"
              type="button"
              value="Log In"
              onClick={handleSubmit}
            />
            <button type="button" className="btn btn-outline-success">
              Registry
            </button>
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
