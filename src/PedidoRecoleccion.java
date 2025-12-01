public class PedidoRecoleccion {
    private int idPedido;
    private String nombreCliente;
    private String celularCliente;
    private String tipoMaterial;
    private double pesoEstimadoKg;
    private String direccion;
    private String estado; // PENDIENTE, PROGRAMADO, COMPLETADO

    public PedidoRecoleccion(int idPedido, String nombreCliente, String celularCliente,
                             String tipoMaterial, double pesoEstimadoKg,
                             String direccion, String estado) {
        this.idPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.celularCliente = celularCliente;
        this.tipoMaterial = tipoMaterial;
        this.pesoEstimadoKg = pesoEstimadoKg;
        this.direccion = direccion;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public double getPesoEstimadoKg() {
        return pesoEstimadoKg;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido " +
                "ID: " + idPedido +
                ", Cliente: " + nombreCliente +
                ", Celular: " + celularCliente +
                ", Material: " + tipoMaterial +
                ", Peso: " + pesoEstimadoKg + " kg" +
                ", Dirección: " + direccion +
                ", Estado: " + estado;
    }
}
