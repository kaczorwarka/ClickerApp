import AuthForm from "./AuthForm";

function AuthCard() {
  let forms = [
    ["email", "name@example.com", "Email"],
    ["password", "Password", "Password"],
  ];

  return (
          <form className="border">
            <h1>Log In</h1>
            {forms.map((form, index) => (
              <AuthForm
                dynamicType={form[0]}
                dynamicPlaceHolder={form[1]}
                dynamicLabel={form[2]}
                dynamocId={index.toString()}
              />
            ))}

            <div className="d-flex justify-content-evenly">
              <input className="btn btn-primary" type="button" value="Log In" />
              <button type="button" className="btn btn-outline-success">
                Registry
              </button>
            </div>
          </form>
  );
}

export default AuthCard;
