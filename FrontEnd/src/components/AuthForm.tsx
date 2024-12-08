interface Props {
  dynamicType: string;
  dynamicPlaceHolder: string;
  dynamocId: string;
  dynamicLabel: string;
  value: string;
  setValue: React.Dispatch<React.SetStateAction<string>>;
}

function AuthForm({
  dynamicType,
  dynamicPlaceHolder,
  dynamocId,
  dynamicLabel,
  value,
  setValue,
}: Props) {
  return (
    <div className="form-floating mb-3">
      <input
        type={dynamicType}
        className="form-control"
        id={dynamocId}
        placeholder={dynamicPlaceHolder}
        value={value}
        onChange={(e) => {
          setValue(e.target.value);
        }}
      />
      <label htmlFor={dynamocId}>{dynamicLabel}</label>
    </div>
  );
}

export default AuthForm;
