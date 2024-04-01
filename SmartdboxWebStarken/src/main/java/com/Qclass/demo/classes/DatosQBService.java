package com.Qclass.demo.classes;

import org.springframework.stereotype.Service;

@Service
public class DatosQBService {

	private static DatosQB datosQB;

    public void guardarDatosQB(DatosQB payload) {
        this.datosQB = payload;
    }

    public static DatosQB obtenerDatosQB() {
        return datosQB;
    }
}
