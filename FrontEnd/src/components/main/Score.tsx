import Button from "../Button";

interface Props {
  date: string;
  user: string;
  score: number;
  buttonType: String;
  buttonValue: String;
  isButtonEnabled: boolean;
  buttonAction: () => void;
}

function Score({
  date,
  user,
  score,
  buttonType,
  buttonValue,
  isButtonEnabled,
  buttonAction,
}: Props) {
  const getButton = () => {
    if (isButtonEnabled)
      return (
        <div className="text-center mt-3">
          <Button
            buttonType={buttonType}
            buttonValue={buttonValue}
            buttonAction={buttonAction}
          />
        </div>
      );
    else return <></>;
  };

  return (
    <div className="bg-white p-4 w-90 rounded shadow">
      <div className="d-flex justify-content-between">
        <div>{date}</div>
        <div>{user}</div>
        <div>
          <strong>{score}</strong>
        </div>
      </div>
      {getButton()}
    </div>
  );
}

export default Score;
