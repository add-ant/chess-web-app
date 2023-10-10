import React, { createElement } from 'react';
import './Chessboard.css';
import Tile from './Tile';
import Files from './Files';
import Ranks from './Ranks';

export default function Chessboard(){
    let board = [];

    let rows = Array.from({length: 8}, (x, i) => 8 - i);
    let columns = Array.from({length: 8}, (x, i) => String.fromCharCode(i + 97));
    

    for(let i = 7; i >= 0; i--){
        for(let j = 0; j < 8; j++){
            board.push(createElement(Tile, {row: i, column: j}, null));
        }
    }

    return <div className='board'>
            <Files files={rows}/>
            <div className='board-grid'>
                {board}
            </div>
            <Ranks ranks={columns} />
        </div>
}