import './Artists.css';

import * as React from 'react'
import { RouteComponentProps } from 'react-router'

import { AppRoutes } from '../../models/routes';

export const Artists = ({ match }: RouteComponentProps<AppRoutes>) => {
  return (
    <div className="artistPageContainer">
      <div className="header">

        <div className="search">
          <h1>ARTISTES</h1>
        </div>

      </div>
    </div>
  )
}