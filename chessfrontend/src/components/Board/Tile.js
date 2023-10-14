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
        id={props.piece.id}
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
    const [type, color, originRow, originColumn, id] = e.dataTransfer
      .getData("text")
      .split(",");
    const sourceTarget = document.getElementById(id);
    console.log(sourceTarget);
    console.log(e);

    const fetchFun = () => {
      fetch(
        `http://localhost:8080/match/moves?ox=${originRow}&oy=${originColumn}&tx=${props.row}&ty=${props.column}`
      )
        .then((res) => res.json())
        .then((data) => {
          if (data.type !== "ILLEGAL") {
            setPiece(
              <Piece
                type={type}
                color={color}
                id={id}
                currentRow={props.row}
                currentColumn={props.column}
              />
            );
          } else {
            sourceTarget.style.display = "block";
          }
        })
        .catch((err) => {
          console.log(err.message);
        });
    };

    fetchFun();
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
