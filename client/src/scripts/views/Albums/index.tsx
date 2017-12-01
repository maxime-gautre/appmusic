import './Albums.css';

import * as React from 'react'
import { RouteComponentProps } from 'react-router'

interface Origin {
  id: string,
  service: string
}

interface Artist {
  id: string,
  name: string
}

interface Track {
  id: string,
  title: string,
  uri: string,
  duration: number,
  artists: Array<Artist>
}

interface Album {
  id: string,
  title: string,
  uri: string,
  cover: string,
  nbTracks: number,
  tracks: Array<Track>,
  artist: Artist,
  origin: Origin
}

interface State {
  albums: Array<Album>
}

export class AlbumList extends React.Component<RouteComponentProps<Album>, State> {

  constructor(props: RouteComponentProps<Album>) {
    super(props);
    this.state = {albums: []}
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
    const albums = this.state.albums;
    console.log(albums)
    if(!albums || albums.length == 0) return null;
    const currentAlbum = this.state.albums[0];
    return (
      <div className="albumPageContainer">
        <div className="header">

          <div className="search">
            <h1>MES ALBUMS & TITRES</h1>
            <p>### Albums - 11 titres</p>
            <input type="text" placeholder="RECHERCHER"/>
          </div>

          <div className="synchronize" onClick={ this.synchronize }>
            <h3 className="left">Synchroniser</h3>
            <div className="right" onClick={ this.changePlatform }>{'\u25BE'}</div>
          </div>

        </div>
        <div className="album-detail">
          <div className="left">
            <img src="https://e-cdns-images.dzcdn.net/images/cover/762492bbd649c33c63e8e0c1ba3cce4d/500x500-000000-80-0-0.jpg "/>
            <div className="metadata">
              <h3 className="title">
                {currentAlbum.title}
              </h3>
              <div className="author">
                Par {currentAlbum.artist.name}
              </div>
              <div className="infos">
                2016 - {currentAlbum.nbTracks} titres, 41min
              </div>
              <div className="origin">
                Importé de {currentAlbum.origin.service}
              </div>
            </div>
          </div>

          <div className="right">
            <div className="covers">
              {albums.slice(1, 6).map((album: Album) => {
                return (
                  <img key={album.id} src={album.cover}/>
                )
              })
              }
            </div>

            <div className="tracks">
              <ul>
              { currentAlbum.tracks.map((track: Track) => {
                return (
                  <li key={track.id}><span>{track.title}</span></li>
                )
              })
              }
              </ul>
            </div>
          </div>
        </div>
      </div>
    )
  }

  componentDidMount() {
    fetch("http://localhost:9000/api/albums", {
      method: 'GET',
    }).then( data => data.json()).then(data => this.setState({albums: data}))
  }
}