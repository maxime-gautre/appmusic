import './Playlists.css';

import * as React from 'react'
import { RouteComponentProps } from 'react-router'

import { AppRoutes } from '../../models/routes';

export const Playlists = ({ match }: RouteComponentProps<AppRoutes>) => {
  return (
    <div className="playlistPageContainer">
      <h1>PLAYLISTS</h1>
    </div>
  )
}