package group7.museoprado;

import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;


/**
 * Clase LeapListener
 * Clase principal que gestiona los eventos relacionados
 * con el sensor LeapMotion. Esta clase contiene todos los metodos
 * necesarios para gestionar todos los eventos del sensor.
 * Implementa la interfaz Runnable para ejecutarse en una hebra secundaria
 * (Hija de la hebra principal) de forma que no ralentice la ejecucion de la
 * aplicacion principal
 */
class LeapListener extends Listener implements Runnable {

    private Frame lastFrame = null;
    private Gesture lastGesture = null;
    private String sentidoGiro;
    private boolean connect = false;


    /**
     * Getter del dato miembro conectado
     * @return conectado
     */
    public boolean isConnected(){
        return connect;
    }

    /**
     * Metodo onInit
     * Notifica que el LeapMotion se ha iniciado satisfactoriamente
     * @param controller Objeto Contoller que representa el dispositivo en sí
     */
    public void onInit(Controller controller) {
        System.out.println("Iniciando...");
    }

    /**
     * Metodo onConnect
     * Notifica que el dispositivo esta conectado y listo para utilizarse
     * @param controller Objeto Contoller que representa el dispositivo en sí
     */
    public void onConnect(Controller controller) {
        System.out.println("Conectado");
        connect = true;
    }

    /**
     * Metodo onDisconnect
     * Notifica que el dispositivo se ha desconectado
     * @param controller Objeto Contoller que representa el dispositivo en sí
     */
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Desconectado...");
        connect = false;
    }

    /**
     * Metodo onExit
     * Notifica que el dispositivo ya no esta disponible
     * @param controller Objeto Contoller que representa el dispositivo en sí
     */
    public void onExit(Controller controller) {
        System.out.println("Saliendo... ");
        connect = false;
    }

    /**
     * Metodo onFrame
     * Gestiona la recepcion de los paquetes de datos del Leap.
     * Actualiza los dos ultimos frames recibidos y el ultimo gesto (y su tipo)
     * @param controller Objeto Contoller que representa el dispositivo en sí
     */
    public void onFrame(Controller controller){

        ///Obtiene los frames mas recientes en caso de que el ultimo no sea nulo
        if(controller.frame() != null) {
            lastFrame = controller.frame();

            for (Gesture gesto : lastFrame.gestures()){
                lastGesture = gesto;
            }

            if(lastFrame.gestures().count()==0)
                lastGesture=null;
        }
        else
            lastFrame = null;

    }

    /**
     * Getter del dato miembro ultimoFrame
     * @return ultimoFrame
     * @throws NullPointerException
     */
    public Frame getUltimoFrame()throws NullPointerException{
        if(lastFrame == null)
            throw new NullPointerException();
        return lastFrame;
    }



    /**
     * Getter del dato miembro ultimoGesto
     * @return ultimoGesto
     */
    public Gesture getUltimoGesto() {
        return lastGesture;
    }

    /**
     * Getter del dato miembro tipoUltimoGesto
     * @return tipoUltimoGesto
     */
    public Gesture.Type getTipoUltimoGesto() {
        return lastGesture.type();
    }

    /**
     * Setter del dato miembro ultimoGesto
     * @param ultimoGesto
     */
    public void setUltimoGesto(Gesture ultimoGesto) {
        this.lastGesture = ultimoGesto;
    }

    /**
     *Metodo run que permite a la hebra ejecutarse
     * y capturar los eventos que relacionados con el Leap
     */
    public void run() {

    }
}
