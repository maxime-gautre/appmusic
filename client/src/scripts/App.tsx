import './App.css';

import * as React from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom'

import { GetPlaylists } from './views/GetPlaylists'

export const App = () => (
  <Router>
    <div>
      <ul>
        <Link to="/">Home</Link>
        <Link to="/get">Get playlists</Link>
        <Link to="/about">About</Link>
      </ul>

      <Route exact path="/get" component={ GetPlaylists }/>
    </div>
  </Router>
)
