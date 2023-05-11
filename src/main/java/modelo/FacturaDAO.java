/*
 * Clase que implementa la interface IPersona
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class FacturaDAO implements IFactura {

    private Connection con = null;

    public FacturaDAO() {
        con = Conexion.getInstance();
    }

    @Override
    public List<FacturaVO> getAll() throws SQLException {
        List<FacturaVO> lista = new ArrayList<>();

        // Preparamos la consulta de datos mediante un objeto Statement
        // ya que no necesitamos parametrizar la sentencia SQL
        try (Statement st = con.createStatement()) {
            // Ejecutamos la sentencia y obtenemos las filas en el objeto ResultSet
            ResultSet res = st.executeQuery("select * from factura");
            // Ahora construimos la lista, recorriendo el ResultSet y mapeando los datos
            while (res.next()) {
                FacturaVO f = new FacturaVO();
                // Recogemos los datos de la factura, guardamos en un objeto
                f.setPk(res.getInt("pk"));
                f.setFechaEmision(res.getDate("fecha_emision").toLocalDate());
                f.setDescripcion(res.getString("descripcion"));
                f.setTotalImporte(res.getDouble("totalImporte"));
                

                //Añadimos el objeto a la lista
                lista.add(f);
            }
        }

        return lista;
    }

    @Override
    public FacturaVO findByPk(int pk) throws SQLException {

        ResultSet res = null;
        FacturaVO f = new FacturaVO();

        String sql = "select * from factura where pk=?";

        try (PreparedStatement prest = con.prepareStatement(sql)) {
            // Preparamos la sentencia parametrizada
            prest.setInt(1, pk);

            // Ejecutamos la sentencia y obtenemos las filas en el objeto ResultSet
            res = prest.executeQuery();

            // Nos posicionamos en el primer registro del Resultset. Sólo debe haber una fila
            // si existe esa pk
            if (res.next()) {
                // Recogemos los datos de la factura, guardamos en un objeto
                f.setPk(res.getInt("pk"));
                f.setFechaEmision(res.getDate("fecha_emision").toLocalDate());
                f.setDescripcion(res.getString("descripcion"));
                f.setTotalImporte(res.getDouble("totalImporte"));
                return f;
            }

            return null;
        }
    }

    @Override
    public int insertFactura(FacturaVO f) throws SQLException {

        int numFilas = 0;
        String sql = "insert into factura values (?,?,?,?)";

        if (findByPk(f.getPk()) != null) {
            // Existe un registro con esa pk
            // No se hace la inserción
            return numFilas;
        } else {
            // Instanciamos el objeto PreparedStatement para inserción
            // de datos. Sentencia parametrizada
            try (PreparedStatement prest = con.prepareStatement(sql)) {

                // Establecemos los parámetros de la sentencia
                prest.setInt(1, f.getPk());
                prest.setDate(2, Date.valueOf(f.getFechaEmision()));
                prest.setString(3, f.getDescripcion());
                prest.setDouble(4, f.getTotalImporte());
                

                numFilas = prest.executeUpdate();
            }
            return numFilas;
        }

    }

    @Override
    public int insertFactura(List<FacturaVO> lista) throws SQLException {
        int numFilas = 0;

        for (FacturaVO tmp : lista) {
            numFilas += insertFactura(tmp);
        }

        return numFilas;
    }

    @Override
    public int deleteFactura() throws SQLException {

        String sql = "delete from factura";

        int nfilas = 0;

        // Preparamos el borrado de datos  mediante un Statement
        // No hay parámetros en la sentencia SQL
        try (Statement st = con.createStatement()) {
            // Ejecución de la sentencia
            nfilas = st.executeUpdate(sql);
        }

        // El borrado se realizó con éxito, devolvemos filas afectadas
        return nfilas;

    }

    @Override
    public int deleteFactura(FacturaVO persona) throws SQLException {
        int numFilas = 0;

        String sql = "delete from factura where pk = ?";

        // Sentencia parametrizada
        try (PreparedStatement prest = con.prepareStatement(sql)) {

            // Establecemos los parámetros de la sentencia
            prest.setInt(1, persona.getPk());
            // Ejecutamos la sentencia
            numFilas = prest.executeUpdate();
        }
        return numFilas;
    }

    @Override
    public int updateFactura(int pk, FacturaVO nuevosDatos) throws SQLException {

        int numFilas = 0;
        String sql = "update factura set descripcion = ?, fecha_emision = ?, totalImporte = ? where pk=?";

        if (findByPk(pk) == null) {
            // La persona a actualizar no existe
            return numFilas;
        } else {
            // Instanciamos el objeto PreparedStatement para inserción
            // de datos. Sentencia parametrizada
            try (PreparedStatement prest = con.prepareStatement(sql)) {

                // Establecemos los parámetros de la sentencia
                prest.setString(1, nuevosDatos.getDescripcion());
                prest.setDate(2, Date.valueOf(nuevosDatos.getFechaEmision()));
                prest.setDouble(3, nuevosDatos.getTotalImporte());
                prest.setInt(4, pk);

                numFilas = prest.executeUpdate();
            }
            return numFilas;
        }
    }

    
}

