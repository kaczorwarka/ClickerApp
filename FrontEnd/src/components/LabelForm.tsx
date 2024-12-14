interface Props {
  dynamocId: string;
  value: string;
}

function LabelForm({ dynamocId, value }: Props) {
  return (
    <div className="mb-3">
      <label className="form-control-plaintext" id={dynamocId}>
        {value}
      </label>
    </div>
  );
}

export default LabelForm;
