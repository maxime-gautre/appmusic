import * as React from 'react';
import './App.css';

interface Props {
  test?: boolean
}

export default class App extends React.Component<Props, {}> {
  render() {
    return (
      <div className="app">
        Hello World
      </div>
    );
  }
}
