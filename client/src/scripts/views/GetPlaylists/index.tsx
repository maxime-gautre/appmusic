import { RouteComponentProps } from 'react-router'
import * as React from 'react'
import {  Route, Link } from 'react-router-dom'

import { AppRoutes } from '../../models/routes';

export const GetPlaylists = ({ match }: RouteComponentProps<AppRoutes>) => {
  return(
    <div>
      <Route
        exact
        path={ match.url }
        render={ () => (
          <h3>Please select a platform.</h3>
        )}
      />

      <h2>Get playlist from:</h2>
          <Link to={`/spotify`}>
            Spotify
          </Link>
          <Link to={`/deezer`}>
            Deezer
          </Link>

      <Route path={`${match.url}/:platform`} component={ Topic } />
    </div>
  )
}

const Topic = ({ match }: RouteComponentProps<AppRoutes>) => (
  <div>
    <h3>{ match.params.platform }</h3>
  </div>
)