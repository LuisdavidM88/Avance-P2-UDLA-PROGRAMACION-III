import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SpinnerNumberModel;
import java.util.List;

public class Ventana {
    private JPanel Principal;
    private JTabbedPane Modulos;

    // TAB MODULO A - INGRESAR PEDIDO
    private JTabbedPane ModuloA;
    private JTextField txtPedidoId;
    private JTextField txtNombreCliente;
    private JTextField txtCelularCliente;
    private JTextArea txtDireccion;
    private JComboBox cboMaterial;
    private JSpinner spiPeso;
    private JComboBox cboEstado;
    private JButton btnAgregarPedido;
    private JButton btnEditarPedido;
    private JLabel lblSiguienteEnCola;

    // TAB MODULO A - LISTADO
    private JList lstPedidos;
    private JButton btnMostrarPedidos;
    private JLabel lblCantidadPedidos;
    private JLabel lblPesoPapelCarton;
    private JLabel lblPesoPlastico;
    private JLabel lblPesoVidrio;
    private JLabel lblPesoMetal;
    private JLabel lblPesoMixto;

    // TAB MODULO B - REGISTRO
    private JTabbedPane ModuloB;
    private JTextField txtIdCamion;
    private JTextField txtPlaca;
    private JTextField txtChofer;
    private JTextField txtCelularChofer;
    private JSpinner spiCapacidadVehiculo;
    private JTextField txtAnioVehiculo;
    private JComboBox cboEstadoVehiculo;
    private JButton btnRegistrarCamion;
    private JButton btnEditarCamion;

    // TAB MODULO B - FLOTA / COLA
    private JList lstFlota;
    private JButton btnOrdenarPorID;
    private JButton btnOrdenarPorCapacidad;
    private JButton btnOrdenarPorAnio;
    private JList lstColaSalida;
    private JButton btnEnviarARuta;
    private JButton btnAtenderSiguiente;
    private JLabel lblCamionActual;

    // LÓGICA
    private GestorPedidos gestorPedidos = new GestorPedidos();
    private GestorCamiones gestorCamiones = new GestorCamiones();
    private int indicePedidoSeleccionado = -1;

    public Ventana() {
        spiPeso.setModel(new SpinnerNumberModel(10.0, 1.0, 10000.0, 1.0));
        spiCapacidadVehiculo.setModel(new SpinnerNumberModel(1000, 500, 30000, 500));
        configurarEventos();
        actualizarFlota();
        actualizarColaCamiones();
    }

    // ================= VALIDACIÓN PEDIDOS =================

    private PedidoRecoleccion leerYValidarPedido(boolean esEdicion) {
        // ID
        String idTexto = txtPedidoId.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarError("El ID del pedido es obligatorio.");
            return null;
        }
        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarError("El ID del pedido debe ser un número entero.");
            return null;
        }
        if (id <= 0) {
            mostrarError("El ID del pedido debe ser mayor que 0.");
            return null;
        }

        // Nombre
        String nombre = txtNombreCliente.getText().trim();
        if (nombre.isEmpty()) {
            mostrarError("El nombre del cliente es obligatorio.");
            return null;
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            mostrarError("El nombre del cliente solo debe contener letras y espacios.");
            return null;
        }
        if (nombre.length() < 3) {
            mostrarError("El nombre del cliente debe tener al menos 3 caracteres.");
            return null;
        }

        // Celular
        String celular = txtCelularCliente.getText().trim();
        if (!celular.matches("\\d{10}")) {
            mostrarError("El celular del cliente debe tener exactamente 10 dígitos.");
            return null;
        }

        // Dirección
        String direccion = txtDireccion.getText().trim();
        if (direccion.isEmpty()) {
            mostrarError("La dirección es obligatoria.");
            return null;
        }
        if (direccion.length() < 5) {
            mostrarError("La dirección debe tener al menos 5 caracteres.");
            return null;
        }

        // Tipo material / estado
        String tipo = cboMaterial.getSelectedItem().toString();
        String estado = cboEstado.getSelectedItem().toString();

        // Peso
        double peso;
        try {
            peso = Double.parseDouble(spiPeso.getValue().toString());
        } catch (NumberFormatException ex) {
            mostrarError("El peso estimado debe ser un número.");
            return null;
        }
        if (peso <= 0) {
            mostrarError("El peso estimado debe ser mayor que 0.");
            return null;
        }
        if (peso > 10000) {
            mostrarError("El peso estimado es demasiado alto para un solo pedido.");
            return null;
        }

        return new PedidoRecoleccion(id, nombre, celular, tipo, peso, direccion, estado);
    }

    // ================= VALIDACIÓN CAMIONES =================

    private Camion leerYValidarCamion() {
        // ID
        String idTexto = txtIdCamion.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarError("El ID del camión es obligatorio.");
            return null;
        }
        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            mostrarError("El ID del camión debe ser un número entero.");
            return null;
        }
        if (id <= 0) {
            mostrarError("El ID del camión debe ser mayor que 0.");
            return null;
        }

        // Placa
        String placa = txtPlaca.getText().trim().toUpperCase();
        if (placa.isEmpty()) {
            mostrarError("La placa es obligatoria.");
            return null;
        }
        if (placa.length() < 5) {
            mostrarError("La placa debe tener al menos 5 caracteres.");
            return null;
        }
        if (!placa.matches("[A-Z0-9-]+")) {
            mostrarError("La placa solo puede contener letras, números y guion (-).");
            return null;
        }

        // Capacidad
        int capacidad;
        try {
            capacidad = Integer.parseInt(spiCapacidadVehiculo.getValue().toString());
        } catch (NumberFormatException ex) {
            mostrarError("La capacidad debe ser un número entero.");
            return null;
        }
        if (capacidad <= 0) {
            mostrarError("La capacidad debe ser mayor que 0.");
            return null;
        }
        if (capacidad > 30000) {
            mostrarError("La capacidad es demasiado alta para un camión común.");
            return null;
        }

        // Año
        String anioTexto = txtAnioVehiculo.getText().trim();
        if (anioTexto.isEmpty()) {
            mostrarError("El año es obligatorio.");
            return null;
        }
        int anio;
        try {
            anio = Integer.parseInt(anioTexto);
        } catch (NumberFormatException ex) {
            mostrarError("El año debe ser un número entero.");
            return null;
        }
        if (anio < 1980 || anio > 2100) {
            mostrarError("El año del camión debe estar entre 1980 y 2100.");
            return null;
        }

        // Estado
        String estado = cboEstadoVehiculo.getSelectedItem().toString();

        // Chofer
        String chofer = txtChofer.getText().trim();
        if (chofer.isEmpty()) {
            mostrarError("El nombre del chofer es obligatorio.");
            return null;
        }
        if (!chofer.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            mostrarError("El nombre del chofer solo debe contener letras y espacios.");
            return null;
        }

        // Celular chofer
        String celularChofer = txtCelularChofer.getText().trim();
        if (!celularChofer.matches("\\d{10}")) {
            mostrarError("El celular del chofer debe tener exactamente 10 dígitos.");
            return null;
        }

        return new Camion(id, placa, capacidad, anio, estado, chofer, celularChofer);
    }

    // ================= EVENTOS =================

    private void configurarEventos() {
        // --- PEDIDOS ---
        btnAgregarPedido.addActionListener(e -> {
            PedidoRecoleccion p = leerYValidarPedido(false);
            if (p == null) return;

            if (gestorPedidos.agregar(p)) {
                JOptionPane.showMessageDialog(Principal, "Pedido registrado");
                actualizarPedidoSiguienteEnCola();
                limpiarCamposPedido();
            } else {
                mostrarError("ID inválido. Debe ser mayor que el último ID registrado y no repetirse.");
            }
        });

        btnMostrarPedidos.addActionListener(e -> {
            actualizarListaPedidos();
            actualizarTotalesPedidos();
        });

        lstPedidos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && lstPedidos.getSelectedIndex() != -1) {
                    indicePedidoSeleccionado = lstPedidos.getSelectedIndex();
                    List<PedidoRecoleccion> lista = gestorPedidos.todos();
                    PedidoRecoleccion p = lista.get(indicePedidoSeleccionado);

                    txtPedidoId.setText(String.valueOf(p.getIdPedido()));
                    txtNombreCliente.setText(p.getNombreCliente());
                    txtCelularCliente.setText(p.getCelularCliente());
                    txtDireccion.setText(p.getDireccion());
                    cboMaterial.setSelectedItem(p.getTipoMaterial());
                    spiPeso.setValue(p.getPesoEstimadoKg());
                    cboEstado.setSelectedItem(p.getEstado());

                    JOptionPane.showMessageDialog(Principal,
                            "Revise la pestaña de ingreso para editar el pedido");
                }
            }
        });

        btnEditarPedido.addActionListener(e -> {
            PedidoRecoleccion p = leerYValidarPedido(true);
            if (p == null) return;

            int id = p.getIdPedido();
            if (gestorPedidos.editar(id, p)) {
                JOptionPane.showMessageDialog(Principal, "Pedido editado");
                actualizarListaPedidos();
                actualizarTotalesPedidos();
            } else {
                mostrarError("No existe un pedido con ese ID.");
            }
        });

        // --- CAMIONES ---
        btnRegistrarCamion.addActionListener(e -> {
            Camion c = leerYValidarCamion();
            if (c == null) return;

            if (gestorCamiones.agregar(c)) {
                JOptionPane.showMessageDialog(Principal, "Camión registrado");
                limpiarCamposCamion();
                actualizarFlota();
            } else {
                mostrarError("No se pudo registrar el camión.\n" +
                        "Revise que el ID y la placa no se repitan y que los datos sean válidos.");
            }
        });

        btnOrdenarPorID.addActionListener(e -> {
            gestorCamiones.ordenarId();
            actualizarFlota();
        });

        btnOrdenarPorCapacidad.addActionListener(e -> {
            gestorCamiones.ordenarCapacidad();
            actualizarFlota();
        });

        btnOrdenarPorAnio.addActionListener(e -> {
            gestorCamiones.ordenarAnioDesc();
            actualizarFlota();
        });

        lstFlota.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lstFlota.getSelectedIndex() != -1) {
                int idx = lstFlota.getSelectedIndex();
                Camion c = gestorCamiones.getFlota().get(idx);

                txtIdCamion.setText(String.valueOf(c.getIdCamion()));
                txtPlaca.setText(c.getPlaca());
                spiCapacidadVehiculo.setValue(c.getCapacidadKg());
                txtAnioVehiculo.setText(String.valueOf(c.getAnio()));
                cboEstadoVehiculo.setSelectedItem(c.getEstado());
                txtChofer.setText(c.getChofer());
                txtCelularChofer.setText(c.getCelularChofer());

                JOptionPane.showMessageDialog(Principal,
                        "Revise la pestaña de registro para editar el camión");
            }
        });

        btnEditarCamion.addActionListener(e -> {
            Camion c = leerYValidarCamion();
            if (c == null) return;

            int id = c.getIdCamion();
            if (gestorCamiones.editar(id, c)) {
                JOptionPane.showMessageDialog(Principal, "Camión editado");
                actualizarFlota();
                actualizarColaCamiones();
            } else {
                mostrarError("No existe un camión con ese ID.");
            }
        });

        btnEnviarARuta.addActionListener(e -> {
            int idx = lstFlota.getSelectedIndex();
            if (idx != -1) {
                Camion c = gestorCamiones.getFlota().get(idx);
                gestorCamiones.enviarARuta(c);
                actualizarFlota();
                actualizarColaCamiones();
                JOptionPane.showMessageDialog(Principal,
                        "Camión enviado a ruta (estado: EN RUTA).");
            } else {
                JOptionPane.showMessageDialog(Principal,
                        "Seleccione un camión de la flota.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAtenderSiguiente.addActionListener(e -> {
            Camion c = gestorCamiones.atenderSiguiente();
            if (c == null) {
                lblCamionActual.setText("Actual: ninguno");
                JOptionPane.showMessageDialog(Principal,
                        "No hay camiones en la cola de salida.");
            } else {
                lblCamionActual.setText("Camión despachado: " + c.getIdCamion() + " - " + c.getPlaca());
                actualizarColaCamiones();
            }
        });
    }

    // ================= AUXILIARES PEDIDOS =================

    private void actualizarListaPedidos() {
        DefaultListModel<String> dlm = new DefaultListModel<>();
        for (PedidoRecoleccion p : gestorPedidos.todos()) {
            dlm.addElement(p.toString());
        }
        lstPedidos.setModel(dlm);
    }
    private void actualizarTotalesPedidos() {
        // Totales generales (solo para lógica; no hay etiqueta de peso total en pantalla)
        double totalPeso = gestorPedidos.sumarPeso(0);
        int cantidad = gestorPedidos.conteo(0);
        lblCantidadPedidos.setText("Cantidad de pedidos: " + cantidad);

        // Tomar los nombres EXACTOS del combo
        String matPapel    = cboMaterial.getItemAt(0).toString();
        String matPlastico = cboMaterial.getItemAt(1).toString();
        String matVidrio   = cboMaterial.getItemAt(2).toString();
        String matMetal    = cboMaterial.getItemAt(3).toString();
        String matMixto    = cboMaterial.getItemAt(4).toString();

        double pesoPapel    = gestorPedidos.sumarPesoPorMaterial(matPapel);
        double pesoPlastico = gestorPedidos.sumarPesoPorMaterial(matPlastico);
        double pesoVidrio   = gestorPedidos.sumarPesoPorMaterial(matVidrio);
        double pesoMetal    = gestorPedidos.sumarPesoPorMaterial(matMetal);
        double pesoMixto    = gestorPedidos.sumarPesoPorMaterial(matMixto);

        lblPesoPapelCarton.setText("Papel/Cartón: " + pesoPapel + " kg");
        lblPesoPlastico.setText("Plástico: " + pesoPlastico + " kg");
        lblPesoVidrio.setText("Vidrio: " + pesoVidrio + " kg");
        lblPesoMetal.setText("Metal: " + pesoMetal + " kg");
        lblPesoMixto.setText("Mixto: " + pesoMixto + " kg");
    }


    private void actualizarPedidoSiguienteEnCola() {
        PedidoRecoleccion sig = gestorPedidos.siguienteEnCola();
        if (sig == null) {
            lblSiguienteEnCola.setText("Ninguno");
        } else {
            lblSiguienteEnCola.setText(sig.getIdPedido() + " - " + sig.getNombreCliente());
        }
    }

    private void limpiarCamposPedido() {
        txtPedidoId.setText("");
        txtNombreCliente.setText("");
        txtCelularCliente.setText("");
        txtDireccion.setText("");
        spiPeso.setValue(10.0);
        if (cboMaterial.getItemCount() > 0) cboMaterial.setSelectedIndex(0);
        if (cboEstado.getItemCount() > 0) cboEstado.setSelectedIndex(0);
    }

    // ================= AUXILIARES CAMIONES =================

    private void actualizarFlota() {
        DefaultListModel<String> dlm = new DefaultListModel<>();
        for (Camion c : gestorCamiones.getFlota()) {
            dlm.addElement(c.toString());
        }
        lstFlota.setModel(dlm);
    }

    private void actualizarColaCamiones() {
        DefaultListModel<String> dlm = new DefaultListModel<>();
        for (Camion c : gestorCamiones.getColaSalida()) {
            dlm.addElement(c.toString());
        }
        lstColaSalida.setModel(dlm);
    }

    private void limpiarCamposCamion() {
        txtIdCamion.setText("");
        txtPlaca.setText("");
        txtChofer.setText("");
        txtCelularChofer.setText("");
        spiCapacidadVehiculo.setValue(1000);
        txtAnioVehiculo.setText("");
        if (cboEstadoVehiculo.getItemCount() > 0) cboEstadoVehiculo.setSelectedIndex(0);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(Principal, mensaje,
                "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getPrincipal() {
        return Principal;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Graham Reciclaje - Pedidos y Camiones");
            frame.setContentPane(new Ventana().getPrincipal());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
