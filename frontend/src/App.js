import './App.module.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomeComponent from './components/HomeComponent';
import AcopioComponent from './components/AcopioComponent';
import AcopioInformationComponent from './components/AcopioInformationComponent';
import ProveedoresComponent from './components/ProveedoresComponent';
import JustificativoComponent from './components/JustificativoComponent';
import AutorizacionComponent from './components/AutorizacionComponent';
import SueldosComponent from './components/SueldosComponent';
function App() {
  return (
    <div>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomeComponent />} />
        <Route path= "/subir-archivo" element={<AcopioComponent />} />
        <Route path= "/informacion-archivo" element={<AcopioInformationComponent />} />
        <Route path= "/lista-empleados" element={<ProveedoresComponent />} />
        <Route path= "/justificativo" element={<JustificativoComponent />} />
        <Route path= "/autorizacion" element={<AutorizacionComponent />} />
        <Route path= "/planilla-sueldos" element={<SueldosComponent />} />

      </Routes>
    </BrowserRouter>
  </div>
  );
}

export default App;
