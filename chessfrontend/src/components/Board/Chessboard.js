import { useEffect, useState } from "react";
import "./Chessboard.css";
import Tile from "./Tile";
import Files from "./Files";
import Ranks from "./Ranks";

export default function Chessboard() {
  const [board, setBoard] = useState([]);
  let tiles = [];

  useEffect(() => {
    fetch("http://localhost:8080/match")
      .then((res) => res.json())
      .then((data) =>
        data.forEach((a) =>
          a.forEach((elem) => setBoard((board) => [...board, elem]))
        )
      )
      .catch((err) => {
        console.log(err.message);
      });
  }, []);

  let rows = Array.from({ length: 8 }, (x, i) => 8 - i);
  let columns = Array.from({ length: 8 }, (x, i) =>
    String.fromCharCode(i + 97)
  );

  console.log(board);
  board.map(
    (elem) =>
      (tiles[56 - elem.row * 8 + elem.column] = (
        <Tile
          key={elem.row + "-" + elem.column}
          row={elem.row}
          column={elem.column}
          piece={elem.piece}
        />
      ))
  );

  return (
    <div className="board">
      <Files files={rows} />
      <div className="board-grid">{tiles}</div>
      <Ranks ranks={columns} />
    </div>
  );
}
