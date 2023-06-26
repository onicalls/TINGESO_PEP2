import './App.module.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomeComponent from './components/HomeComponent';
import AcopioComponent from './components/AcopioComponent';
import AcopioInformationComponent from './components/AcopioInformationComponent';
import ProveedoresComponent from './components/ProveedoresComponent';
import SueldosComponent from './components/SueldosComponent';
import ValorLecheComponent from "./components/ValorLecheComponent";
import ProveedoresAddComponent from "./components/ProveedoresAddComponent";
import ValorLecheInformationComponent from "./components/ValorLecheInformationComponent";
function App() {
  return (
    <div>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomeComponent />} />
        <Route path= "/subir-acopio" element={<AcopioComponent />} />
        <Route path= "/lista-acopio" element={<AcopioInformationComponent />} />
        <Route path= "/lista-proveedores" element={<ProveedoresComponent />} />
        <Route path= "/agregar-proveedores" element={<ProveedoresAddComponent />} />
        <Route path= "/subir-valorleche" element={<ValorLecheComponent />} />
        <Route path= "/lista-valorleche" element={<ValorLecheInformationComponent />} />
        <Route path= "/planilla-sueldos" element={<SueldosComponent />} />

      </Routes>
    </BrowserRouter>
  </div>
  );
}

export default App;
