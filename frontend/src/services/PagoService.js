import axios from "axios";

const API_URL = "http://localhost:8080/pago";

class PagoService {

    getSueldos(datos) {
        return axios.post(API_URL, datos);
    }
}

export default new PagoService();