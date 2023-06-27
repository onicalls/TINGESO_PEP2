import axios from "axios";

const API_URL = "http://localhost:8080/pago";

class PagoService {

    getSueldos() {
        return axios.post(API_URL);
    }
}

export default new PagoService();