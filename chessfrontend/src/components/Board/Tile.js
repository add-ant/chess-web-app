import React from "react";
import "./Tile.css";
import Piece from "../Pieces/Piece";
import { useState } from "react";

export default function Tile(props) {
  let initialPiece = null;
  if (props.piece !== null) {
    initialPiece = (
      <Piece
        type={props.piece.pieceType}
        color={props.piece.color}
        currentRow={props.row}
        currentColumn={props.column}
      />
    );
  }

  const [piece, setPiece] = useState(initialPiece);

  const getTileColor = (i, j) => {
    return (i + j) % 2 === 0 ? "tile-dark" : "tile-light";
  };

  const onDrop = (e) => {
    const [type, color] = e.dataTransfer.getData("text").split(",");
    console.log(type, color);
    setPiece(
      <Piece
        type={type}
        color={color}
        currentRow={props.row}
        currentColumn={props.column}
      />
    );
  };

  const onDragOver = (e) => e.preventDefault();

  return (
    <div
      onDrop={onDrop}
      onDragOver={onDragOver}
      className={getTileColor(props.row, props.column)}
    >
      {piece}
    </div>
  );
}
