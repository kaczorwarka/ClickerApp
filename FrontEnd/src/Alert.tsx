interface Props {
  alertType: string;
  alertText: string;
  setVisible: React.Dispatch<React.SetStateAction<boolean>>;
}
// alert-danger
function Alert({ alertType, alertText, setVisible }: Props) {
  return (
    <div className={"alert " + alertType} role="alert">
      {alertText}
      <button
        type="button"
        className="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        onClick={() => setVisible(false)}
      ></button>
    </div>
  );
}

export default Alert;
