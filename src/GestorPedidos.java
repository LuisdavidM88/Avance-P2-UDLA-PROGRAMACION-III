import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GestorPedidos {
    private List<PedidoRecoleccion> pedidos;          // Lista principal
    private Queue<PedidoRecoleccion> colaPendientes;  // Cola de atención
    private int ultimoId;

    public GestorPedidos() {
        pedidos = new ArrayList<>();
        colaPendientes = new LinkedList<>();
        ultimoId = 0;
    }

    public boolean agregar(PedidoRecoleccion p) {
        if (p == null) return false;
        if (p.getIdPedido() <= ultimoId) return false;
        pedidos.add(p);
        colaPendientes.add(p);
        ultimoId = p.getIdPedido();
        return true;
    }

    // Búsqueda binaria para editar por ID
    public boolean editar(int id, PedidoRecoleccion dato) {
        if (dato == null) return false;
        int i = 0;
        int s = pedidos.size() - 1;
        while (i <= s) {
            int c = (i + s) / 2;
            int idC = pedidos.get(c).getIdPedido();
            if (id == idC) {
                pedidos.set(c, dato);
                return true;
            } else if (id < idC) {
                s = c - 1;
            } else {
                i = c + 1;
            }
        }
        return false;
    }

    public List<PedidoRecoleccion> todos() {
        return pedidos;
    }

    // Recursividad: suma de pesos
    public double sumarPeso(int i) {
        if (i == pedidos.size()) return 0;
        return pedidos.get(i).getPesoEstimadoKg() + sumarPeso(i + 1);
    }

    // Recursividad: conteo de pedidos
    public int conteo(int i) {
        if (i == pedidos.size()) return 0;
        return 1 + conteo(i + 1);
    }

    public PedidoRecoleccion siguienteEnCola() {
        return colaPendientes.peek();
    }

    // NUEVO: suma de peso por tipo de material (recursivo)
    public double sumarPesoPorMaterial(String tipoMaterial) {
        return sumarPesoPorMaterial(0, tipoMaterial);
    }

    private double sumarPesoPorMaterial(int i, String tipoMaterial) {
        if (i == pedidos.size()) {
            return 0;
        }
        PedidoRecoleccion p = pedidos.get(i);
        double aporte = 0;
        if (p.getTipoMaterial().equalsIgnoreCase(tipoMaterial)) {
            aporte = p.getPesoEstimadoKg();
        }
        return aporte + sumarPesoPorMaterial(i + 1, tipoMaterial);
    }
}
