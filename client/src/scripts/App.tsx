import './App.css';

import * as React from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom'
import { AnimatedSwitch } from 'react-router-transition';

import { AlbumList } from './views/Albums'
import { Artists } from './views/Artists'
import { Playlists } from './views/Playlists'

export const App = () => (
  <Router>
    <div className="app">
      <SideBar/>
      <AnimatedSwitch
        atEnter={{ opacity: 0 }}
        atLeave={{ opacity: 1 }}
        atActive={{ opacity: 1 }}
        runOnMount={ false }
        className="children-routes"
      >
        <Route exact path="/albums" component={ AlbumList }>
        </Route>
        <Route exact path="/artistes" component={ Artists }/>
        <Route exact path="/playlists" component={ Playlists }/>
      </AnimatedSwitch>
    </div>
  </Router>
)

const SideBar = () => {
  return (
    <ul className="main-sidebar">
      <div className="logo"/>

      <li><Link to="/"> <h3>TABLEAU DE BORD </h3></Link></li>
      <li><h3> MA MUSIQUE </h3></li>
      <li><Link to="/albums">Albums / Titres</Link></li>
      <li><Link to="/artistes">Artistes</Link></li>
      <li><Link to="/playlists">Playlists</Link></li>
      <li>
        <User/>
      </li>
    </ul>
)}

const User = () => (
  <div className="userDisplay">
    <div className="avatar"/>
    <div className="infos">
      <p className="userName">Username</p>
      <p className="userEmail">user@email.com</p>
    </div>
  </div>
)