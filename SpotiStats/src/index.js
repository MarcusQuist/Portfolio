import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Spotify from './util/Spotify';
import Artist from './Components/Artist/Artist';
import * as serviceWorker from './serviceWorker';

import PopupMenu from './Components/PopupMenu/PopupMenu';

ReactDOM.render(<Spotify />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
