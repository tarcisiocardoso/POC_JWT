import React from 'react';
import TextField from '@material-ui/core/TextField';
import { createStyles, makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import { useHistory } from "react-router-dom";

const useStyles = makeStyles((theme) =>
  createStyles({
    root: {
      '& .MuiTextField-root': {
        margin: theme.spacing(1),
        
        width: '25ch',
      },
    },
    rootBtn: {
        '& > *': {
          margin: theme.spacing(1),
        },
      },
  }),
);

export default function Login() {
  const classes = useStyles();
  const history = useHistory();

  function login(username, password) {

    console.log('logando.');
    
    // let body = {
    //     username:'springuser',
    //     password:'password'
    // }
    let body = {
      username:'ben',
      password:'benspassword'
    }
    // const token ='??.??.??';

    fetch('/login', 
    {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify(body)
    }).then(response => response.json())
      .then(user => {
            localStorage.setItem("token", user.token);
            history.push("/home");
        })
      .catch(error => {console.log("...>", error)});
}


  return (
    <form className={classes.root} noValidate autoComplete="off">
      <div>
        <TextField disabled={false} id="standard-disabled" label="User" />
        <TextField
          id="standard-password-input"
          label="Password"
          type="password"
          autoComplete="current-password"
        />
      </div>

      <div className={classes.rootBtn}>
        <Button variant="contained" color="primary" onClick={login}>
            Logar
        </Button>
        
        </div>
        
    </form>
  );
}
