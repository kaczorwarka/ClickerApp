interface Props {
  dynamicType: string;
  dynamicPlaceHolder: string;
  dynamocId: string;
  dynamicLabel: string;
}

function AuthForm({
  dynamicType,
  dynamicPlaceHolder,
  dynamocId,
  dynamicLabel,
}: Props) {
  return (
    <div className="form-floating mb-3">
      <input
        type={dynamicType}
        className="form-control"
        id={dynamocId}
        placeholder={dynamicPlaceHolder}
      />
      <label htmlFor={dynamocId}>{dynamicLabel}</label>
    </div>
  );
}

export default AuthForm;

// <div className="form-floating">
//         <input
//           type="password"
//           className="form-control"
//           id="floatingPassword"
//           placeholder="Password"
//         />
//         <label htmlFor="floatingPassword">{dynamicLabel}</label>
//       </div>
