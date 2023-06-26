import React, {Component, useState} from "react";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import FileUploadService from "../services/ValorLecheService";
import styled from "styled-components";
import swal from 'sweetalert';
import NavbarValorLecheListComponent from "./NavbarValorLecheAddComponent";

class ValorLecheComponent extends Component{
  constructor(props) {
    super(props);
    this.state = {
      file: null,
      year: "",
      month: "",
      quincena: ""

    };
    this.onFileChange = this.onFileChange.bind(this);
  }

  onFileChange(event) {
    this.setState({ file: event.target.files[0] });
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
    this.setState({ quincena: event.target.value });
    console.log(this.state.quincena);
  };

  onFileUpload = () => {
    swal({
      title: "¿Está seguro de que desea cargar el archivo csv?",
      text: "Tenga en cuenta que el archivo solo será cargado si su nombre es 'valorleche.csv' y si su formato es correcto.",
      icon: "warning",
      buttons: ["Cancelar", "Cargar"],
      dangerMode: true
    }).then(respuesta=>{
      if(respuesta){
        swal("Archivo cargado correctamente!", {icon: "success", timer: "3000"});
        const formData = new FormData();
        formData.append("file", this.state.file);
        formData.append("year", this.state.year);
        formData.append("month", this.state.month);
        formData.append("quincena", this.state.quincena);
        FileUploadService.CargarArchivo(formData).then((res) => {
        });
      }
      else{
        swal({text: "Archivo no cargado.", icon: "error"});
      }
    });
  };

  render() {
    return (
      <div className="home">
        <NavbarValorLecheListComponent/>
        <Styles>
          <div class="f">
            <div class="container">
              <h1><b>Cargar el archivo de Valores de Leche</b></h1>
              <Form.Group className="mb-3" controlId="year" value={this.state.year} onChange={this.changeYearHandler}>
                <Form.Label>Año:</Form.Label>
                <Form.Control type="text" pattern="[0-9]{4}" required />
              </Form.Group>

              <Form.Group className="mb-3" controlId="month" value={this.state.month} onChange={this.changeMonthHandler}>
                <Form.Label>Mes:</Form.Label>
                <Form.Control as="select" required>
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

              <Form.Group className="mb-3" controlId="quincena" value={this.state.quincena} onChange={this.changeQuincenaHandler}>
                <Form.Label>Quincena:</Form.Label>
                <Form.Control as="select" required>
                  <option value="Q1">Q1</option>
                  <option value="Q2">Q2</option>
                </Form.Control>
              </Form.Group>
              <Row className="mt-4">
                <Col col="12">
                  <Form.Group className="mb-3" controlId="formFileLg">
                    <Form.Control type="file" size="lg" onChange={this.onFileChange} />
                  </Form.Group>
                  <Button varian="primary" onClick={this.onFileUpload}>
                    Cargar el archivo a la Base de Datos</Button>
                </Col>
              </Row>
            </div>
          </div>
          <br>
          </br>
          <hr>
          </hr>
          <div class="form1">
            <h5><b>Recuerde que el nombre del archivo debe ser "valorleche.csv"!</b></h5>
          </div>
        </Styles>
      </div>
    );
  }
}

export default ValorLecheComponent;


const Styles = styled.div`
.container{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 2%;
}
.f{
  margin-top: 40px;
  border: 3px solid rgb(162, 161, 161);
  padding: 40px;
  padding-top: 10px;
  border-radius: 40px;
  margin-left: 300px;
  margin-right: 300px;
}
@media(max-width: 1200px){
  .f{margin-left: 200px;
    margin-right: 200px;}
  
}
.form1{
  border: 1px solid rgb(82, 82, 173);
  padding: 30px;
  border-radius: 30px;
  margin-left: 300px;
  margin-right: 300px;
}
`