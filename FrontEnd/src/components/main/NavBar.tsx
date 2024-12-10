interface Props {
  amountOfLives: number;
  handleLogOut: () => void;
  handlePayment: () => void;
}

function NavBar({ amountOfLives, handleLogOut, handlePayment }: Props) {
  return (
    <nav className="navbar navbar-ligth bg-body-evenly-light p-2">
      <div className="d-inline-flex p-2">
        <i
          className="bi bi-heart-fill"
          style={{ fontSize: "2rem", color: "red" }}
        ></i>
        <span className="ms-2" style={{ fontSize: "2rem" }}>
          {`x` + amountOfLives}
        </span>
      </div>
      <div className="d-inline-flex">
        <button type="button" className="btn" onClick={handleLogOut}>
          <i
            className="bi bi-box-arrow-left"
            style={{ fontSize: "2rem", color: "black" }}
          ></i>
        </button>
        <button type="button" className="btn" onClick={handlePayment}>
          <i
            className="bi bi-currency-dollar"
            style={{ fontSize: "2rem", color: "green" }}
          ></i>
        </button>
      </div>
    </nav>
  );
}

//<i class="bi-alarm" style="font-size: 2rem; color: cornflowerblue;"></i>
export default NavBar;
