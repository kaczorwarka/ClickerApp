interface Props {
  buttonType: String;
  buttonValue: String;
  buttonAction: () => void;
}

// "btn btn-outline-success"
function Button({ buttonType, buttonValue, buttonAction }: Props) {
  return (
    <button
      type="button"
      className={"btn " + buttonType}
      onClick={buttonAction}
    >
      {buttonValue}
    </button>
  );
}

export default Button;
