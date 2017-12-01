import './Artists.css';

import * as React from 'react'
import { RouteComponentProps } from 'react-router'

import { AppRoutes } from '../../models/routes';

export const Artists = ({ match }: RouteComponentProps<AppRoutes>) => {
  return (
    <div className="artistPageContainer">
      <h1>ARTISTS</h1>
    </div>
  )
}