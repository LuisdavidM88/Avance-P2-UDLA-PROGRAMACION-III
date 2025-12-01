import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GestorCamiones {
    private List<Camion> flota;
    private Deque<Camion> colaSalida;

    public GestorCamiones() {
        flota = new ArrayList<>();
        colaSalida = new ArrayDeque<>();
    }

    public boolean agregar(Camion c) {
        if (c == null) return false;

        if (c.getIdCamion() <= 0 || c.getCapacidadKg() <= 0 || c.getAnio() <= 0) {
            return false;
        }

        // No permitir ID ni placa repetidos
        for (Camion existente : flota) {
            if (existente.getIdCamion() == c.getIdCamion()) {
                return false;
            }
            if (existente.getPlaca().equalsIgnoreCase(c.getPlaca())) {
                return false;
            }
        }
        flota.add(c);
        return true;
    }

    public boolean editar(int id, Camion dato) {
        if (dato == null) return false;
        for (int i = 0; i < flota.size(); i++) {
            if (flota.get(i).getIdCamion() == id) {
                flota.set(i, dato);
                return true;
            }
        }
        return false;
    }

    public List<Camion> getFlota() {
        return flota;
    }

    // Bubble sort por ID
    public void ordenarId() {
        Camion aux;
        for (int i = 0; i < flota.size(); i++) {
            for (int j = i + 1; j < flota.size(); j++) {
                if (flota.get(i).getIdCamion() > flota.get(j).getIdCamion()) {
                    aux = flota.get(i);
                    flota.set(i, flota.get(j));
                    flota.set(j, aux);
                }
            }
        }
    }

    // Insertion sort por capacidad ascendente
    public void ordenarCapacidad() {
        Camion aux;
        int j;
        for (int i = 1; i < flota.size(); i++) {
            aux = flota.get(i);
            j = i - 1;
            while (j >= 0 && aux.getCapacidadKg() < flota.get(j).getCapacidadKg()) {
                flota.set(j + 1, flota.get(j));
                j--;
            }
            flota.set(j + 1, aux);
        }
    }

    // Insertion sort por año descendente
    public void ordenarAnioDesc() {
        Camion aux;
        int j;
        for (int i = 1; i < flota.size(); i++) {
            aux = flota.get(i);
            j = i - 1;
            while (j >= 0 && aux.getAnio() > flota.get(j).getAnio()) {
                flota.set(j + 1, flota.get(j));
                j--;
            }
            flota.set(j + 1, aux);
        }
    }

    public void enviarARuta(Camion c) {
        if (c != null) {
            c.setEstado("EN RUTA");
            colaSalida.addLast(c);
        }
    }

    public Camion atenderSiguiente() {
        return colaSalida.pollFirst();
    }

    public List<Camion> getColaSalida() {
        return new ArrayList<>(colaSalida);
    }
}
