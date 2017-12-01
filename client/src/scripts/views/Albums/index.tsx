import './Albums.css';

import * as React from 'react'
import { RouteComponentProps } from 'react-router'

import { AppRoutes } from '../../models/routes';

export class AlbumList extends React.Component<RouteComponentProps<AppRoutes>, {}> {
  constructor(props: RouteComponentProps<AppRoutes>) {
    super(props);
    this.state = {}
  }

  synchronize = () => {
    const body = { id: '1234', service: 'deezer' }

    fetch('http://localhost:9000/api/synchronize', {
      method: 'POST',
      body
    }).then(r => console.log(r))
  }

  changePlatform = (e: React.MouseEvent<{}>) => {
    alert('change')
    e.stopPropagation();
  }

  render() {
    return (
      <div className="albumPageContainer">
        <div className="header">

          <div className="search">
            <h1>MES ALBUMS & TITRES</h1>
            <p>### Albums - ### titres</p>
            <input type="text" placeholder="RECHERCHER"/>
          </div>

          <div className="synchronize" onClick={ this.synchronize }>
            <h3 className="left">Synchroniser</h3>
            <div className="right" onClick={ this.changePlatform }>{ '\u25BE' }</div>
          </div>

        </div>
      </div>
    )
  }
}