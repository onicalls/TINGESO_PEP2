import './App.module.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomeComponent from './components/HomeComponent';
import AcopioComponent from './components/AcopioComponent';
import AcopioInformationComponent from './components/AcopioInformationComponent';
import ProveedoresComponent from './components/ProveedoresComponent';
import SueldosComponent from './components/SueldosComponent';
import ValorLecheComponent from "./components/ValorLecheComponent";
function App() {
  return (
    <div>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomeComponent />} />
        <Route path= "/subir-acopio" element={<AcopioComponent />} />
        <Route path= "/informacion-acopio" element={<AcopioInformationComponent />} />
        <Route path= "/lista-proveedores" element={<ProveedoresComponent />} />
        <Route path= "/subir-valorleche" element={<ValorLecheComponent />} />
        <Route path= "/informacion-valorleche" element={<AcopioInformationComponent />} />
        <Route path= "/planilla-sueldos" element={<SueldosComponent />} />

      </Routes>
    </BrowserRouter>
  </div>
  );
}

export default App;
