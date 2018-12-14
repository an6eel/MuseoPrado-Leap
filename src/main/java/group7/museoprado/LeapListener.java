package group7.museoprado;


import com.leapmotion.leap.*;

class LeapListener extends Listener implements Runnable {

    private Frame lastFrame = null;
    private Gesture lastGesture = null;
    private boolean connect = false;


    public boolean isConnected(){
        return connect;
    }


    public void onInit(Controller controller) {
        System.out.println("Iniciando...");
    }

    public void onConnect(Controller controller) {
        System.out.println("Conectado");
        connect = true;
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Desconectado...");
        connect = false;
    }

    public void onExit(Controller controller) {
        System.out.println("Saliendo... ");
        connect = false;
    }

    public void onFrame(Controller controller){

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

    public Frame getUltimoFrame()throws NullPointerException{
        if(lastFrame == null)
            throw new NullPointerException();
        return lastFrame;
    }

    public Gesture getUltimoGesto() {
        return lastGesture;
    }

    public Gesture.Type getTipoUltimoGesto() {
        return lastGesture.type();
    }

    public void setUltimoGesto(Gesture ultimoGesto) {
        this.lastGesture = ultimoGesto;
    }


    public void run() {

    }
}
