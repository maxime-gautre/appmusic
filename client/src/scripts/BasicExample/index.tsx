//import * as React from 'react'
//import { RouteComponentProps } from 'react-router'
//import { BrowserRouter as Router, Route, Link } from 'react-router-dom'
//
//type HomeRoute = '/'
//type TopicIDs = 'spotify' | 'deezer' | HomeRoute
//
//interface AppRoutes {
//  topicId: TopicIDs
//}
//
//export const App = () => (
//  <Router>
//    <div>
//      <ul>
//        <Link to="/">Home</Link>
//        <Link to="/spotify">Spotify</Link>
//        <Link to="/deezer">Deezer</Link>
//        {/*<Link to="/test">Test</Link>*/}
//      </ul>
//
//      <Route exact path="/" component={ Home }/>
//      <Route path="/spotify" component={ Spotify }/>
//      <Route path="/deezer" component={ Deezer }/>
//      <Route path="/test" component={ Test }/>
//    </div>
//  </Router>
//)
//
//const Home = () => (
//  <div>
//    <h2>Home</h2>
//  </div>
//)
//
//const Spotify = () => (
//  <div>
//    <h2>Spotify</h2>
//  </div>
//)
//
//const Deezer = () => (
//  <div>
//    <h2>Deezer</h2>
//  </div>
//)
//
//const Test = ({ match }: RouteComponentProps<{}>) => (
//  <div>
//    <h2>Topics</h2>
//    <ul>
//      <li>
//        <Link to={`${match.url}/rendering`}>
//          Rendering with React
//        </Link>
//      </li>
//      <li>
//        <Link to={`${match.url}/components`}>
//          Components
//        </Link>
//      </li>
//      <li>
//        <Link to={`${match.url}/props-v-state`}>
//          Props v. State
//        </Link>
//      </li>
//    </ul>
//
//    <Route path={`${match.url}/:topicId`} />
//
//    <Route
//      exact
//      path={ match.url }
//      render={ () => (
//        <h3>Please select a topic.</h3>
//      )}
//    />
//  </div>
//)

//const Topic = ({ match }: RouteComponentProps<AppRoutes>) => (
//  <div>
//    <h3>{ match.params.topicId }</h3>
//  </div>
//)