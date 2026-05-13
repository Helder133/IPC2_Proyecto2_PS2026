package org.proyecto2.proyecto2.services.usuario.cartera;

import org.proyecto2.proyecto2.db.usuario.cartera.CarteraPlataformaDAO;
import org.proyecto2.proyecto2.models.usuario.cartera.CarteraPlataforma;
import org.proyecto2.proyecto2.models.usuario.cartera.TransaccionPlataforma;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class CarteraPlataformaService {
    public CarteraPlataforma getCarteraPlataforma() throws SQLException {
        CarteraPlataformaDAO carteraPlataformaDAO = new CarteraPlataformaDAO();
        Optional<CarteraPlataforma> carteraPlataformaOptional = carteraPlataformaDAO.getCarteraPlataforma();
        if (carteraPlataformaOptional.isPresent()) {
            return carteraPlataformaOptional.get();
        } else {
            CarteraPlataforma carteraPlataforma = new CarteraPlataforma(0);
            carteraPlataforma.setPlataformaId(carteraPlataformaDAO.insert(carteraPlataforma));
            return carteraPlataforma;
        }
    }

    public void updateCarteraPlataforma(Connection connection, CarteraPlataforma carteraPlataforma, int contratoId, double porcentajeAplicado, double montoComision) throws SQLException {
        CarteraPlataformaDAO carteraPlataformaDAO = new CarteraPlataformaDAO();
        carteraPlataformaDAO.updateCarteraPlataforma(connection, carteraPlataforma);
        TransaccionPlataformaService transaccionPlataformaService = new TransaccionPlataformaService();
        TransaccionPlataforma transaccionPlataforma = new TransaccionPlataforma(carteraPlataforma.getPlataformaId(), contratoId, porcentajeAplicado, montoComision);
        transaccionPlataformaService.createTransaccion(transaccionPlataforma, connection);
    }

}
