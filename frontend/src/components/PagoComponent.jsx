import React, { Component } from "react";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import NavbarComponent3 from "./NavbarComponent3";
import PagoService from "../services/PagoService";
import styled from "styled-components";
import swal from 'sweetalert';

class PagoComponent extends Component{
    constructor(props) {
        super(props);
        this.state = {
          pago: [],
          year: "",
          month: "",
          quin: ""
        };
    }

    changeYearHandler = (event) => {
        this.setState({ year: event.target.value });
        console.log(this.state.year);
      };
    
      changeMonthHandler = (event) => {
        this.setState({ month: event.target.value });
        console.log(this.state.month);
      };
    
      changeQuincenaHandler = (event) => {
        this.setState({ quin: event.target.value });
        console.log(this.state.quin);
      };

    componentDidMount() {
        fetch("http://localhost:8080/pago")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Error al obtener los datos de pago.");
                }
                return response.json();
            })
            .then((data) => {
                this.setState({ pago: data });
            })
            .catch((error) => {
                console.error(error);
                // Manejar el error, por ejemplo, mostrar un mensaje de error en la interfaz de usuario.
            });
    }


    calcularPlantilla = async (e) => {
        e.preventDefault();
        swal({
            title: "¿Está seguro de que desea calcular esta planilla?",
            icon: "warning",
            buttons: ["Volver", "Calcular"],
            dangerMode: true,
        }).then(async (respuesta) => {
            if (respuesta) {
                swal("Platilla calculada!", { icon: "success", timer: "3000" });
                    let datos = {
                    year: this.state.year,
                    month: this.state.month,
                    quin: this.state.quin,
                };
                console.log(this.state.year);
                console.log(this.state.month);
                console.log(this.state.quin);
                console.log("datos => " + JSON.stringify(datos));
                try {
                    const res = await PagoService.getSueldos(datos);
                    // Hacer algo con la respuesta (res)
                } catch (error) {
                    console.error(error);
                    // Manejar el error, por ejemplo, mostrar un mensaje de error en la interfaz de usuario.
                }
            } else {
                swal({ text: "Plantilla no calculada.", icon: "error" });
            }
        });
    };


      render(){
        return(
            <div className="home">
                <NavbarComponent3 />
                <Styles>
                    <h1 className="text-center"> <b>Reporte de Planilla de pago ($CLP)</b></h1>
                    <Form.Group className="mb-3" controlId="year" value={this.state.year} onChange={this.changeYearHandler}>
                        <Form.Label>Año:</Form.Label>
                        <Form.Control type="text" pattern="[0-9]{4}" required />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="month" value={this.state.month} onChange={this.changeMonthHandler}>
                        <Form.Label>Mes:</Form.Label>
                        <Form.Control as="select" required>
                        <option value="">Seleccione un Mes</option>
                        <option value="1">Enero</option>
                        <option value="2">Febrero</option>
                        <option value="3">Marzo</option>
                        <option value="4">Abril</option>
                        <option value="5">Mayo</option>
                        <option value="6">Junio</option>
                        <option value="7">Julio</option>
                        <option value="8">Agosto</option>
                        <option value="9">Septiembre</option>
                        <option value="10">Octubre</option>
                        <option value="11">Noviembre</option>
                        <option value="12">Diciembre</option>
                        </Form.Control>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="quin" value={this.state.quin} onChange={this.changeQuincenaHandler}>
                        <Form.Label>Quincena:</Form.Label>
                        <Form.Control as="select" required>
                        <option value="">Seleccione una Quincena</option>
                        <option value="Q1">Q1</option>
                        <option value="Q2">Q2</option>
                        </Form.Control>
                    </Form.Group>
                    <Button varian="primary" onClick={this.calcularPlantilla}>
                    Calcular Planilla</Button>
                    <div className="f">
                        
                        <table border="1" class="content-table">
                            <thead>

                                <tr><th width="10%">Quincena (AAAA/MM/Q)</th>
                                <th>Código proveedor</th>
                                <th>Nombre proveedor</th>
                                <th>TOTAL KLS leche</th>
                                <th>Nro. días que envío leche</th>
                                <th>Promedio diario KLS leche</th>
                                <th>%Variación Leche</th>
                                <th>%Grasa</th>
                                <th>%Variación Grasa</th>
                                <th>%Solidos Totales</th>
                                <th>%Variación ST</th>
                                <th>Pago por Leche</th>
                                <th>Pago por Grasa</th>
                                <th>Pago por Solidos Totales</th>
                                <th>Bonificación por Frecuencia</th>
                                <th>Dcto. Variación Leche</th>
                                <th>Dcto. Variación Grasa</th>
                                <th>Dcto. Variación ST</th>
                                <th>Pago TOTAL</th>
                                <th>Monto Retención</th>
                                <th>Monto FINAL</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.pago.map((pago) => (
                                    <tr key={pago.id}>
                                        <td>{pago.quincena}</td>
                                        <td>{pago.codigoProveedor}</td>
                                        <td>{pago.nombreProveedor}</td>
                                        <td>{pago.totalKlsLeche}</td>
                                        <td>{pago.numDiasEnvioLeche}</td>
                                        <td>{pago.promedioDiarioKlsLeche}</td>
                                        <td>{pago.porcentajeVariacionLeche}</td>
                                        <td>{pago.porcentajeGrasa}</td>
                                        <td>{pago.porcentajeVariacionGrasa}</td>
                                        <td>{pago.porcentajeSolidosTotales}</td>
                                        <td>{pago.porcentajeVariacionST}</td>
                                        <td>{pago.pagoPorLeche}</td>
                                        <td>{pago.pagoPorGrasa}</td>
                                        <td>{pago.pagoPorSolidosTotales}</td>
                                        <td>{pago.bonificacionPorFrecuencia}</td>
                                        <td>{pago.descuentoVariacionLeche}</td>
                                        <td>{pago.descuentoVariacionGrasa}</td>
                                        <td>{pago.descuentoVariacionST}</td>
                                        <td>{pago.pagoTotal}</td>
                                        <td>{pago.montoRetencion}</td>
                                        <td>{pago.montoFinal}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </Styles>
            </div>
        )
    }
}

export default PagoComponent;

const Styles = styled.div`


.text-center {
    text-align: center;
    justify-content: center;
    padding-top: 8px;
    font-family: "Arial Black", Gadget, sans-serif;
    font-size: 30px;
    letter-spacing: 0px;
    word-spacing: 2px;
    color: #000000;
    font-weight: 700;
    text-decoration: none solid rgb(68, 68, 68);
    font-style: normal;
    font-variant: normal;
    text-transform: uppercase;
}

.f{
    justify-content: center;
    align-items: center;
    display: flex;
}
*{
    font-family: sans-serif;
    box-sizing: content-box;
    margin: 0;
    padding: 0;
}
.content-table{
    border-collapse: collapse;
    margin: 25px 0;
    font-size: 0.8em;
    min-width: 200px;
    border-radius: 5px 5px 0 0;
    overflow: hidden;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    margin-left: 4%;
    margin-right: 4%;
}
.content-table thead tr{
    background-color: #009879;
    color: #ffffff;
    text-align: center;
    font-weight: bold;
}
.content-table th,
.content-table td{
    padding: 12px 15px;
    text-align: center;
}
.content-table tbody tr{
    border-bottom: 1px solid #dddddd;
}
.content-table tbody tr:nth-of-type(even){
    background-color: #f3f3f3;
}
.content-table tbody tr:last-of-type{
    border-bottom: 2px solid #009879;
}
.content-table tbody tr.active-row{
    font-weight: bold;
    color: #009879;
}
`