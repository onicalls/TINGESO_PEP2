import axios from "axios";

const API_URL = "http://localhost:8080/valorleche/";

class ValorLecheService{

    CargarArchivo(valorleche){
        return axios.post(API_URL, valorleche);
    }
}

export default new ValorLecheService()
