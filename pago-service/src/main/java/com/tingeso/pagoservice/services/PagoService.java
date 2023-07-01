package com.tingeso.pagoservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tingeso.pagoservice.entities.PagoEntity;
import com.tingeso.pagoservice.models.ValorLecheModel;
import com.tingeso.pagoservice.models.ProveedorModel;
import com.tingeso.pagoservice.repositories.PagoRepository;
import com.tingeso.pagoservice.variables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    public List<PagoEntity> obtenerPagos() {
        return pagoRepository.findAll();
    }

    private String formatoQuincena(int year, int month, String quincena) {
        return String.format("%d-%02d-%s", year, month, quincena);
    }

    public List<ValorLecheModel> obtenerListaPorQuincena(String quincena) {
        String url = "http://valorleche-service/valorleche/" + quincena;
        ResponseEntity<List<ValorLecheModel>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ValorLecheModel>>() {}
        );
        List<ValorLecheModel> valorleche = response.getBody();
        return valorleche;
    }

    public ProveedorModel obtenerPorCodigo(String proveedorCode){
        ProveedorModel proveedorModel = restTemplate.getForObject("http://proveedor-service/proveedor/" + proveedorCode, ProveedorModel.class);
        System.out.println(proveedorModel);
        return proveedorModel;
    }

    public void generarPagos(int year, int month, String quin) {
        System.out.println("check 1\n");
        String[] quincenas = new String[]{"Q1", "Q2"};
        System.out.println("check 2\n");
        for (String quincena : quincenas) {
            System.out.println("check 3\n");
            String quincenaAux = formatoQuincena(year, month, quincena);

            List<ValorLecheModel> valorLecheEntities = obtenerListaPorQuincena(quincena);
            System.out.println(valorLecheEntities);

            for (ValorLecheModel valorLecheEntity : valorLecheEntities) {

                String proveedorCode = valorLecheEntity.getProveedor();
                ProveedorModel proveedorEntity = obtenerPorCodigo(proveedorCode);

                double pagoProveedor = calcularPagoProveedor(valorLecheEntity, proveedorEntity);

                double descuentos = calcularDescuentos(proveedorCode, quincenaAux);
                PagoEntity pagosEntity = new PagoEntity();
                pagosEntity.setQuincena(quincenaAux);
                pagosEntity.setCodigoProveedor(proveedorEntity.getCodigo());
                pagosEntity.setNombreProveedor(proveedorEntity.getNombre());
                pagosEntity.setTotalKlsLeche(valorLecheEntity.getKilos());
                pagosEntity.setNumDiasEnvioLeche((int) valorLecheEntity.getDiasTotalesAcopio());
                pagosEntity.setPromedioDiarioKlsLeche(valorLecheEntity.getPromedioKilosAcopio());
                pagosEntity.setPorcentajeVariacionLeche(calcularVariacionLeche(valorLecheEntity, valorLecheEntity.getQuincena(), valorLecheEntity.getProveedor()));
                pagosEntity.setPorcentajeGrasa(valorLecheEntity.getGrasa());
                pagosEntity.setPorcentajeVariacionGrasa(calcularVariacionGrasa(valorLecheEntity, valorLecheEntity.getQuincena(), valorLecheEntity.getProveedor()));
                pagosEntity.setPorcentajeSolidosTotales(valorLecheEntity.getSolido());
                pagosEntity.setPorcentajeVariacionST(calcularVariacionSolidos(valorLecheEntity, valorLecheEntity.getQuincena(), valorLecheEntity.getProveedor()));
                pagosEntity.setPagoPorLeche(calcularPagoKiloLeche(proveedorEntity.getCategoria()));
                pagosEntity.setPagoPorGrasa(calcularPagoGrasa(valorLecheEntity.getGrasa()));
                pagosEntity.setPagoPorSolidosTotales(calcularPagoSolidos(valorLecheEntity.getSolido()));
                pagosEntity.setBonificacionPorFrecuencia(calcularBonificacionFrecuencia(valorLecheEntity.getConstancia(), valorLecheEntity.getKilos(), pagoProveedor));
                pagosEntity.setDescuentoVariacionLeche(calcularDescuentoVariacionLeche(calcularVariacionLeche(valorLecheEntity, valorLecheEntity.getQuincena(), valorLecheEntity.getProveedor())) );
                pagosEntity.setDescuentoVariacionGrasa(calcularDescuentoVariacionGrasa(calcularVariacionGrasa(valorLecheEntity, valorLecheEntity.getQuincena(), valorLecheEntity.getProveedor()))); // Asigna el valor correcto según tu lógica de negocio
                pagosEntity.setDescuentoVariacionST(calcularDescuentosSolidosTotales(calcularVariacionSolidos(valorLecheEntity, valorLecheEntity.getQuincena(), valorLecheEntity.getProveedor()))); // Asigna el valor correcto según tu lógica de negocio
                pagosEntity.setPagoTotal(pagoProveedor - descuentos);
                pagosEntity.setMontoRetencion(calcularRetencion(proveedorEntity.getCodigo(), pagoProveedor - descuentos));
                pagosEntity.setMontoFinal(pagosEntity.getPagoTotal() - pagosEntity.getMontoRetencion());
                System.out.println(pagosEntity);

                if (quincena.equals(quin)) {
                    System.out.println("FINAL UWU");
                    pagoRepository.save(pagosEntity);
                }
            }
        }
    }


    public double calcularPagoProveedor(ValorLecheModel valorLecheEntity, ProveedorModel proveedorEntity) {
        double kilosLeche = valorLecheEntity.getKilos();
        double porcentajeGrasa = valorLecheEntity.getGrasa();
        double porcentajeSolidos = valorLecheEntity.getSolido();
        String constancia = valorLecheEntity.getConstancia();

        String categoria = proveedorEntity.getCategoria();

        double pagoKiloLeche = calcularPagoKiloLeche(categoria);
        double pagoGrasa = calcularPagoGrasa(porcentajeGrasa);
        double pagoSolidos = calcularPagoSolidos(porcentajeSolidos);
        double bonificacionFrecuencia = calcularBonificacionFrecuencia(constancia, kilosLeche, pagoKiloLeche);

        return calcularPagoTotal(kilosLeche, pagoKiloLeche, pagoGrasa, pagoSolidos, bonificacionFrecuencia);
    }

    private double calcularPagoKiloLeche(String categoria) {
        return switch (categoria) {
            case "A" -> Categorias.CATEGORIA_A;
            case "B" -> Categorias.CATEGORIA_B;
            case "C" -> Categorias.CATEGORIA_C;
            case "D" -> Categorias.CATEGORIA_D;
            default -> throw new IllegalArgumentException("Invalid categoria: " + categoria);
        };
    }


    private double calcularPagoGrasa(double porcentajeGrasa) {
        double pagoGrasa;
        if (porcentajeGrasa <= PorcentajeGrasa.CASO_1) {
            pagoGrasa = PorcentajeGrasa.PAGO_CASO_1;
        } else if (porcentajeGrasa <= PorcentajeGrasa.CASO_2) {
            pagoGrasa = PorcentajeGrasa.PAGO_CASO_2;
        } else {
            pagoGrasa = PorcentajeGrasa.PAGO_CASO_EXTREMO;
        }
        return pagoGrasa;
    }

    private double calcularPagoSolidos(double porcentajeSolidos) {
        double pagoSolidos;
        if (porcentajeSolidos <= PorcentajeSolidosTotales.CASO_1) {
            pagoSolidos = PorcentajeSolidosTotales.PAGO_CASO_1;
        } else if (porcentajeSolidos <= PorcentajeSolidosTotales.CASO_2) {
            pagoSolidos = PorcentajeSolidosTotales.PAGO_CASO_2;
        } else if (porcentajeSolidos <= PorcentajeSolidosTotales.CASO_3) {
            pagoSolidos = PorcentajeSolidosTotales.PAGO_CASO_3;
        } else {
            pagoSolidos = PorcentajeSolidosTotales.PAGO_CASO_EXTREMO;
        }
        return pagoSolidos;
    }

    private double calcularBonificacionFrecuencia(String constancia, double kilosLeche, double pagoKiloLeche) {
        double bonificacionFrecuencia;
        if (constancia == null) {
            constancia = "NO";
        }
        bonificacionFrecuencia = switch (constancia) {
            case "M" -> BonoFrecuencia.MANANA * kilosLeche * pagoKiloLeche;
            case "T" -> BonoFrecuencia.TARDE * kilosLeche * pagoKiloLeche;
            case "MT" -> BonoFrecuencia.MANANA_Y_TARDE * kilosLeche * pagoKiloLeche;
            default -> 0;
        };
        return bonificacionFrecuencia;
    }


    private double calcularPagoTotal(double kilosLeche, double pagoKiloLeche, double pagoGrasa, double pagoSolidos, double bonificacionFrecuencia) {
        return kilosLeche * (pagoKiloLeche + pagoGrasa + pagoSolidos) + bonificacionFrecuencia;
    }

    public ValorLecheModel findByProveedorAndQuincena(String proveedor, String quincenaActual){
        String url = "http://valorleche-service/valorleche/" + proveedor + "/" + quincenaActual;
        ResponseEntity<ValorLecheModel> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ValorLecheModel>() {}
        );
        ValorLecheModel valorLeche = response.getBody();
        return valorLeche;
    }

    public double calcularDescuentos(String proveedor, String quincenaActual) {

        ValorLecheModel valorLecheActual = findByProveedorAndQuincena(proveedor, quincenaActual);

        double variacionKilos = calcularVariacionLeche(valorLecheActual, quincenaActual, proveedor);
        double variacionGrasa = calcularVariacionGrasa(valorLecheActual, quincenaActual, proveedor);
        double variacionSolidos = calcularVariacionSolidos(valorLecheActual, quincenaActual, proveedor);

        double dctoVariacionLeche = calcularDescuentoVariacionLeche(variacionKilos);
        double dctoVariacionGrasa = calcularDescuentoVariacionGrasa(variacionGrasa);
        double dctoVariacionST = calcularDescuentosSolidosTotales(variacionSolidos);

        return dctoVariacionLeche + dctoVariacionGrasa + dctoVariacionST;
    }

    private double calcularVariacionLeche(ValorLecheModel valorLecheActual, String quincenaActual, String proveedor) {
        String quincenaAnterior = calcularQuincenaAnterior(quincenaActual);
        ValorLecheModel valorLecheAnterior = findByProveedorAndQuincena(proveedor, quincenaAnterior);
        double kilosAnterior = (valorLecheAnterior != null) ? valorLecheAnterior.getKilos() : 0.0;
        if (kilosAnterior == 0.0) {return 0.0;}
        return (kilosAnterior - valorLecheActual.getKilos()) / kilosAnterior * 100;
    }

    private double calcularVariacionGrasa(ValorLecheModel valorLecheActual, String quincenaActual, String proveedor) {
        String quincenaAnterior = calcularQuincenaAnterior(quincenaActual);
        ValorLecheModel valorLecheAnterior = findByProveedorAndQuincena(proveedor, quincenaAnterior);
        double grasaAnterior = (valorLecheAnterior != null) ? valorLecheAnterior.getGrasa() : 0.0;
        if (grasaAnterior == 0.0) {return 0.0;}
        return (grasaAnterior - valorLecheActual.getGrasa()) / grasaAnterior * 100;
    }

    private double calcularVariacionSolidos(ValorLecheModel valorLecheActual, String quincenaActual, String proveedor) {
        String quincenaAnterior = calcularQuincenaAnterior(quincenaActual);
        ValorLecheModel valorLecheAnterior = findByProveedorAndQuincena(proveedor, quincenaAnterior);
        double solidosAnterior = (valorLecheAnterior != null) ? valorLecheAnterior.getSolido() : 0.0;
        if (solidosAnterior == 0.0) {return 0.0;}
        return (solidosAnterior - valorLecheActual.getSolido()) / solidosAnterior * 100;
    }

    private String calcularQuincenaAnterior(String quincenaActual) {
        String[] parts = quincenaActual.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        String quincena = parts[2];

        if (quincena.equals("Q2")) {
            // Si es la segunda quincena del mes, la quincena anterior es la primera quincena del mismo mes.
            return year + "-" + String.format("%02d", month) + "-Q1";
        } else if (quincena.equals("Q1")) {
            // Si es la primera quincena del mes y el mes es enero, la quincena anterior es la segunda quincena del año anterior.
            if (month == 1) {
                return (year - 1) + "-12-Q2";
            } else {
                // Si no es enero, la quincena anterior es la segunda quincena del mes anterior.
                return year + "-" + String.format("%02d", month - 1) + "-Q2";
            }
        } else {
            throw new IllegalArgumentException("Invalid quincena: " + quincena);
        }
    }


    private double calcularDescuentoVariacionLeche(double variacion) {
        if (variacion <= VariacionKLSLeche.CASO_1) {
            return VariacionKLSLeche.PAGO_CASO_1;
        } else if (variacion <= VariacionKLSLeche.CASO_2) {
            return VariacionKLSLeche.PAGO_CASO_2;
        } else if (variacion <= VariacionKLSLeche.CASO_3) {
            return VariacionKLSLeche.PAGO_CASO_3;
        } else {
            return VariacionKLSLeche.PAGO_CASO_EXTREMO;
        }
    }

    private double calcularDescuentoVariacionGrasa(double variacion) {
        if (variacion <= VariacionGrasa.CASO_1) {
            return VariacionGrasa.PAGO_CASO_1;
        } else if (variacion <= VariacionGrasa.CASO_2) {
            return VariacionGrasa.PAGO_CASO_2;
        } else if (variacion <= VariacionGrasa.CASO_3) {
            return VariacionGrasa.PAGO_CASO_3;
        } else {
            return VariacionGrasa.PAGO_CASO_EXTREMO;
        }
    }

    private double calcularDescuentosSolidosTotales(double variacion) {
        if (variacion <= VariacionSolidosTotales.CASO_1) {
            return VariacionSolidosTotales.PAGO_CASO_1;
        } else if (variacion <= VariacionSolidosTotales.CASO_2) {
            return VariacionSolidosTotales.PAGO_CASO_2;
        } else if (variacion <= VariacionSolidosTotales.CASO_3) {
            return VariacionSolidosTotales.PAGO_CASO_3;
        } else {
            return VariacionSolidosTotales.PAGO_CASO_EXTREMO;
        }
    }

    private double calcularRetencion(String proveedor, double pagoTotal) {
        if (getRetencionForProveedor(proveedor) && pagoTotal > Retenciones.MONTO_MAYOR) {
            return Retenciones.IMPUESTO * pagoTotal;
        } else {
            return 0;
        }
    }

    private boolean getRetencionForProveedor(String proveedor) {
        ProveedorModel proveedorEntity = obtenerPorCodigo(proveedor);
        return proveedorEntity != null && proveedorEntity.isRetencion();
    }

}
