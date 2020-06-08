import React from "react";
import Login from "./Login";
import Home from './Home';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

export default function App() {

  return (
    <Router>
      <div>
        <nav>
          <ul>
            <li>
              <Link to="/">Home</Link>
            </li>
            <li>
              <Link to="/about">About</Link>
            </li>
            <li>
              <Link to="/users">Users</Link>
            </li>
            <li>
              <Link to="/login">Login</Link>
            </li>
            <li>
              <Link to="/logout">LogOut</Link>
            </li>
          </ul>
        </nav>

        {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
        <Switch>
          <Route path="/about">
            <About />
          </Route>
          <Route path="/users">
            <Users />
          </Route>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/logout">
            <LogOut />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

function About() {

  function info() {
    const token = localStorage.token;
    console.log('>>>token<<<', token );
    if( !token){
        console.log('...indo para login')
        return;
    }

    fetch('/userInfo', {
        method: "GET",
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
          'Authorization': `Bearer ${token}`
        }
      })
    .then(response => {
        console.log( response.status)
        if( response.status === 200 ) {
            return response.json()
        }if( response.status === 401 ) {
           
            return response.statusText;
        }else{
            return response;
        }
    })
    .then(data => {console.log("situacao esperada>>", data)})
    .catch(error => {console.log("ERRO>>>", error)});
    console.log('>>>busca<<<');
}
  info();
  return <h2>About</h2>;
}

function Users() {
  return <h2>Users</h2>;
}

function LogOut(){

  localStorage.removeItem("token");

  return <h2>removido o token</h2>
}