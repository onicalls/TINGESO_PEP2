import axios from "axios";

class ProveedorService {
    
    IngresarProveedor(proveedor){
        return axios.post(`http://localhost:8080/proveedor`, proveedor);
    }
}

export default new ProveedorService();