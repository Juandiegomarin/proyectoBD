/*
 * Clase para mapear los datos de la tabla Persona
 */

package modelo;

import java.time.LocalDate;

/**
 *
 * @author J. Carlos F. Vico <jcarlosvico@maralboran.es>
 */

public class FacturaVO {
    
    private int pk;
    private LocalDate fechaEmision; 
    private String descripcion;
    private double totalImporte;

    public FacturaVO(int pk, LocalDate fechaEmision, String descripcion, double totalImporte) {
        this.pk = pk;
        this.fechaEmision = fechaEmision;
        this.descripcion = descripcion;
        this.totalImporte = totalImporte;
    }

    public FacturaVO() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotalImporte() {
        return totalImporte;
    }

    public void setTotalImporte(double totalImporte) {
        this.totalImporte = totalImporte;
    }

    @Override
    public String toString() {
        return "FacturaVO{" + "pk=" + pk + ", fechaEmision=" + fechaEmision + ", descripcion=" + descripcion + ", totalImporte=" + totalImporte + '}';
    }
    
    
   

   
}
