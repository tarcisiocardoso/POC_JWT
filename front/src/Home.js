import React, { useEffect } from 'react';
import { useHistory } from "react-router-dom";

function Home() {
    const history = useHistory();


    //   useEffect(()=>{
    //     console.log('>>Home<<');

    //   })

    function busca() {
        const token = localStorage.token;
        if( !token){
            history.push("/login");
            return <h2>nao encontrado</h2>;
        }
        fetch('/dashboard', {
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
                history.push("/login");
                return response.statusText;
            }else{
                return response;
            }
        })
        .then(data => {console.log("situacao esperada>>", data)})
        .catch(error => {console.log("ERRO>>>", error)});
    }

    busca();

    return <h2>Vc esta na pagina home</h2>;
}

export default Home;