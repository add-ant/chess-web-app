import React from 'react';
import './Tile.css'

export default function Tile(props){
    const getTileColor = (i, j) => {
        return (i + j) % 2 === 0 ? 'tile-dark': 'tile-light';
    }
    
    return <div className={getTileColor(props.row , props.column)}>
        {props.row}
        {props.column}
    </div>
}