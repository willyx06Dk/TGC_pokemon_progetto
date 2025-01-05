import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
class Player{
    private String nome;
    private boolean energia;
    private ArrayList<Carta> mano;
    private ArrayList<Carta> panchina;
    private Carta attivo;
    private ArrayList<Carta> scartate;
    private Mazzo mazzo;
    private DatagramPacket pacchetto;
    private int punti;

    public Player(String n, DatagramPacket p){
        this.nome=n;
        this.energia=false;
        this.mano=new ArrayList<Carta>();
        this.panchina=new ArrayList<Carta>();
        this.attivo=null;
        this.scartate=new ArrayList<Carta>();
        this.mazzo=null;
        this.pacchetto=p;
        this.punti=0;
    }

    public void setNome(String n){
        this.nome=n;
    }

    public Carta pesca(){
        Carta c=this.mazzo.random();
        this.mano.add(c);
        return c;
    }

    public int cerca(Carta c){
        for(int i=0; i<this.panchina.size(); ){
            if(this.panchina.get(i).corrisponde(c)){
                return i;
            }
        }
        return -1;
    }

    public void scambia(Carta a, Carta p){
        int i=this.cerca(p);
        if(i!=-1){
            Carta temp=this.panchina.get(i);
            this.panchina.remove(temp);
            this.panchina.add(this.attivo);
            this.attivo=temp;
        }
    }

    public String getNome(){
        return this.nome;
    }

    public void inizioTurno(){
        this.energia=true;
    }

    public void assegna(){
        this.energia=false;
    }

    public ArrayList<Carta> getScartate(){
        return this.scartate;
    }

    public Carta getattivo(){
        return this.attivo;
    }

    public ArrayList<Carta> getPanchina(){
        return this.panchina;
    }

    public void leggiMazzo(String tipo) throws IOException{
        this.mazzo=new Mazzo(tipo);
        this.mazzo.leggi();
    }

    public DatagramPacket getPacket(){
        return this.pacchetto;
    }
 
    public void addPunti(int val){
        this.punti+=val;
    }

    public int getPunti(){
        return this.punti;
    }

    public void setAttivo(int i){
        if(this.attivo==null){
            this.attivo=this.mano.get(i);
            this.mano.remove(i);
        }
    }

    public int aggiungiPanchina(int i){
        int indice=-1;
        if(this.panchina.size()<4){
            for (int index = 0; index < this.panchina.size(); index++) {
                if(this.panchina.get(index)==null){
                    indice=index;
                    break;
                }
            }
            this.panchina.add(this.mano.get(i));
        }
        return indice;
    }
}