public class Camion {
    private int idCamion;
    private String placa;
    private int capacidadKg;
    private int anio;
    private String estado;
    private String chofer;
    private String celularChofer;

    public Camion(int idCamion, String placa, int capacidadKg,
                  int anio, String estado, String chofer, String celularChofer) {
        this.idCamion = idCamion;
        this.placa = placa;
        this.capacidadKg = capacidadKg;
        this.anio = anio;
        this.estado = estado;
        this.chofer = chofer;
        this.celularChofer = celularChofer;
    }

    public int getIdCamion() {
        return idCamion;
    }

    public String getPlaca() {
        return placa;
    }

    public int getCapacidadKg() {
        return capacidadKg;
    }

    public int getAnio() {
        return anio;
    }

    public String getEstado() {
        return estado;
    }

    public String getChofer() {
        return chofer;
    }

    public String getCelularChofer() {
        return celularChofer;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
    }

    public void setCelularChofer(String celularChofer) {
        this.celularChofer = celularChofer;
    }

    @Override
    public String toString() {
        return "Camión " +
                "ID: " + idCamion +
                ", Placa: " + placa +
                ", Capacidad: " + capacidadKg + " kg" +
                ", Año: " + anio +
                ", Estado: " + estado +
                ", Chofer: " + chofer +
                ", Celular: " + celularChofer;
    }
}
