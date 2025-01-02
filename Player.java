import java.io.IOException;
import java.util.ArrayList;
class Player{
    private String nome;
    private boolean energia;
    private ArrayList<Carta> mano;
    private ArrayList<Carta> panchina;
    private Carta attivo;
    private ArrayList<Carta> scartate;
    private Mazzo mazzo;

    public Player(){
        this.nome="";
        this.energia=false;
        this.mano=new ArrayList<Carta>();
        this.panchina=new ArrayList<Carta>();
        this.attivo=null;
        this.scartate=new ArrayList<Carta>();
        this.mazzo=null;
    }

    public void setNome(String n){
        this.nome=n;
    }

    public void pesca(){
        Carta c=this.mazzo.random();
        this.mano.add(c);
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
 
}