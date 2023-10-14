import "./Piece.css";

export default function Piece(props) {
  let pieceClass = "";

  switch (props.type) {
    case "KING":
      props.color === "WHITE"
        ? (pieceClass = "piece wk")
        : (pieceClass = "piece bk");
      break;
    case "QUEEN":
      props.color === "WHITE"
        ? (pieceClass = "piece wq")
        : (pieceClass = "piece bq");
      break;
    case "ROOK":
      props.color === "WHITE"
        ? (pieceClass = "piece wr")
        : (pieceClass = "piece br");
      break;
    case "KNIGHT":
      props.color === "WHITE"
        ? (pieceClass = "piece wn")
        : (pieceClass = "piece bn");
      break;
    case "BISHOP":
      props.color === "WHITE"
        ? (pieceClass = "piece wb")
        : (pieceClass = "piece bb");
      break;
    case "PAWN":
      props.color === "WHITE"
        ? (pieceClass = "piece wp")
        : (pieceClass = "piece bp");
      break;
    default:
  }

  const onDragEnd = (e) => {
    console.log(e); //TODO implement the display block for a piece dragged outside the board
  };

  const onDragStart = (e) => {
    e.dataTransfer.effectAllowed = "move";
    console.log(e.target);
    e.dataTransfer.setData(
      "text/plain",
      `${props.type},${props.color},${props.currentRow},${props.currentColumn},${props.id}`
    );
    setTimeout(() => {
      e.target.style.display = "none";
    }, 0);
  };

  return (
    <div
      className={pieceClass}
      id={props.id}
      draggable={true}
      onDragStart={onDragStart}
      onDragEnd={onDragEnd}
    ></div>
  );
}
