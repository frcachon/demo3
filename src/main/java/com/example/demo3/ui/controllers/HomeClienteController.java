package com.example.demo3.ui.controllers;

import com.example.demo3.Demo3Application;
import com.example.demo3.entities.Actividad;
import com.example.demo3.entities.Cliente;
import com.example.demo3.entities.Gustos;
import com.example.demo3.entities.Tags;
import com.example.demo3.managers.*;
import com.example.demo3.persistence.GustosRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.HTML;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

@Component
public class HomeClienteController implements Initializable {

    @Autowired
    private ActividadMgr actividadMgr;

    @Autowired
    private GustosMgr gustosMgr;

    @Autowired
    private InteresMgr interesMgr;

    @Autowired
    private TagsMgr tagsMgr;


    @Autowired
    private ItinerarioClienteController itinerarioClienteController;

    @Autowired
    private OperadorMgr operadorMgr;

    @Autowired
    private ActividadViewController actividadViewController;

    @Autowired
    private ActividadThumbController actividadThumbController;

    @Autowired
    private EditarInteresesClienteController editarInteresesClienteController;

    Cliente cliente;
    void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Autowired
    private EditarPerfilClienteController editarPerfilClienteController;

    @FXML
    private AnchorPane home_pane;

    @FXML
    private Label username_label;

    @FXML
    private TextField search_field;

    @FXML
    private ImageView perfil_view;

    @FXML
    private GridPane actividadesGrid;
    private List<Actividad> actividades;

    @FXML
    private void mouseClick(MouseEvent event) throws IOException {
        Node source = (Node) event.getSource();

        //Bounds b = source.getBoundsInLocal();
        //Bounds c = source.getBoundsInParent();

        //Node temp =

        //boolean a = source.getBoundsInLocal().contains(x,y);
        //int colIndex = GridPane.getColumnIndex(source);
        //int rowIndex = GridPane.getRowIndex(source);

/*        for( Node node: actividadesGrid.getChildren()) {
            if( node instanceof HBox) {
                if(source.getBoundsInParent().contains(event.getX(), event.getY())) {
                    rowIndex =  GridPane.getRowIndex(node);
                    colIndex = GridPane.getColumnIndex(node);
                }
            }
        }*/

        int rowIndex = 0;
        for(Node node: actividadesGrid.getChildren()) {
            if(node instanceof HBox) {
                if(node.getBoundsInParent().contains(event.getX(), event.getY())) {
                    rowIndex =  GridPane.getRowIndex(node) - 1;
                    //System.out.println(rowIndex);
                }
            }
        }
        Actividad act = actividadThumbController.actividades.get(rowIndex);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
        actividadViewController.setData(act, cliente);
        AnchorPane pane = fxmlLoader.load(MainController.class.getResourceAsStream("ActividadView.fxml"));
        home_pane.getChildren().setAll(pane);
    }

    @FXML
    void busquedaDinamica(KeyEvent event) {
        actividadesGrid.getChildren().clear();
        List<Actividad> q = actividadMgr.getActividadesFromTituloContaining(search_field.getText());
        for (int i = 0; i < q.size(); i++) {
            if (operadorMgr.getOperadorFromId(q.get(i).getIdoperador()).getBloqueado()) {
                q.remove(i);
            }
        }

        int row = 1;

        try {
            for (Actividad actividad : q) {
                if (actividad.getValidada()) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("ActividadThumb.fxml"));
                    fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
                    HBox box = fxmlLoader.load();
                    actividadThumbController = fxmlLoader.getController();
                    actividadThumbController.setData(actividad);
                    actividadThumbController.setPane(home_pane);
                    actividadesGrid.addRow(row, box);
                    row++;
                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }



        //ObservableList<Actividad> lista;
        //lista = FXCollections.observableArrayList();
        //lista.removeAll();
        //lista.addAll(q);

        //tabla_actividades.setItems(lista);
        //nombre_actividad.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        //nombre_operador.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        //nombre_operador.setCellValueFactory(cellData -> {
        //    Integer idoperador = cellData.getValue().getIdoperador();
        //    String nombre_operador = operadorRepository.findOperadorById(idoperador).getEmpresa();
        //    return new ReadOnlyStringWrapper(nombre_operador);
        //});
    }

    @FXML
    void editar_perfil(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
        editarPerfilClienteController.setCliente(cliente);
        AnchorPane pane = fxmlLoader.load(MainController.class.getResourceAsStream("EditarPerfilCliente.fxml"));
        home_pane.getChildren().setAll(pane);

    }

    @FXML
    void editar_preferencia(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
        editarInteresesClienteController.setCliente(cliente);
        AnchorPane pane = fxmlLoader.load(MainController.class.getResourceAsStream("EditarInteresesCliente.fxml"));
        home_pane.getChildren().setAll(pane);
    }

    @FXML
    void ver_itinerario(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
        itinerarioClienteController.setCliente(cliente);
        AnchorPane pane = fxmlLoader.load(MainController.class.getResourceAsStream("ItinerarioCliente.fxml"));
        home_pane.getChildren().setAll(pane);
    }

    @FXML
    void goBack(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
        AnchorPane pane = fxmlLoader.load(MainController.class.getResourceAsStream("Login.fxml"));
        home_pane.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        username_label.setText(cliente.getMail());
        actividadesGrid.getChildren().clear();


        if(cliente.getImagencliente() != null) {
            InputStream img = new ByteArrayInputStream(cliente.getImagencliente());
            Image foto = new Image(img);
            perfil_view.setImage(foto);

            double w = 0;
            double h = 0;

            double ratioX = perfil_view.getFitWidth() / foto.getWidth();
            double ratioY = perfil_view.getFitHeight() / foto.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = foto.getWidth() * reducCoeff;
            h = foto.getHeight() * reducCoeff;

            perfil_view.setX((perfil_view.getFitWidth() - w) / 2);
            perfil_view.setY((perfil_view.getFitHeight() - h) / 2);
        }


        actividades = actividadMgr.getAll();
        String usuario = this.cliente.getMail();
        List<Gustos> gustos_cliente = gustosMgr.getGustosUsuario(usuario);

        ArrayList<Actividad> actividades_mostrar = new ArrayList<>(100);

        for (Actividad actividad : actividades) { //para cada actividad de las existentes

            int puntaje = 0;

            List<Tags> tags_actividad = tagsMgr.getAllFromActividad(actividad.getId());

            for (Gustos gusto : gustos_cliente) {
                for (Tags tag : tags_actividad) {

                    if(gusto.getId().getIdgustos() == tag.getId().getIdtags()) {

                    puntaje ++;

                    }

                }


            }

            if(puntaje > 0) {
                if (actividades_mostrar.size() < puntaje) {
                    actividades_mostrar.add(actividad);
                } else {
                    actividades_mostrar.add(puntaje - 1, actividad);
                }
            }

        }

        Collections.reverse(actividades_mostrar);


        int acts_size = actividades_mostrar.size();
        int i = 0;
        while (i < acts_size) {
            try {
                if (operadorMgr.getOperadorFromId(actividades_mostrar.get(i).getIdoperador()).getBloqueado()) {
                    actividades_mostrar.remove(i);
                    continue;
                }
                i++;
            }
            catch (Exception e) {
                break;
            }
        }

/*        for (int i = 0; i < acts_size; i++) {
            if (operadorMgr.getOperadorFromId(actividades.get(i).getIdoperador()).getBloqueado()) {
                actividades.remove(i);
            }
        }*/

        int row = 1;

        try {
            if (actividades_mostrar.size() > 0) {
                for (Actividad actividad : actividades_mostrar) {
                    if (actividad.getValidada()) {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("ActividadThumb.fxml"));
                        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
                        HBox box = fxmlLoader.load();
                        actividadThumbController = fxmlLoader.getController();
                        actividadThumbController.setData(actividad);
                        actividadThumbController.setPane(home_pane);
                        actividadesGrid.addRow(row, box);
                        row++;
                    }
                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

/*    EventHandler<ActionEvent> buttonEventHandler(){
        return event -> {
            Node node = (Node) event.getTarget();
            int row = GridPane.getRowIndex(node);
            int column = GridPane.getColumnIndex(node);
        };
    }*/

}