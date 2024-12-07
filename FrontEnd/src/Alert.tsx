interface Props {
  alertType: string;
  alertText: string;
  setVisible: React.Dispatch<React.SetStateAction<boolean>>;
}

function Alert({ alertType, alertText, setVisible }: Props) {
  return (
    <div className={"alert " + alertType + ' d-flex justify-content-between'} role="alert">
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
