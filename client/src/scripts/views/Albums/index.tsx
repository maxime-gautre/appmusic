import './Albums.css';

import * as React from 'react'
import { RouteComponentProps } from 'react-router'

import { AppRoutes } from '../../models/routes';

export const AlbumList = ({ match }: RouteComponentProps<AppRoutes>) => {
  return (
    <div className="albumPageContainer">
      <div className="header">

        <div className="search">
          <h1>MES ALBUMS & TITRES</h1>
          <p>### Albums - ### titres</p>
          <input type="text" placeholder="RECHERCHER"/>
        </div>

        <div className="synchronize">
          <div className="left">Synchroniser</div>
          <div className="right">{ '\u25BE' }</div>
        </div>

      </div>
    </div>
  )
}