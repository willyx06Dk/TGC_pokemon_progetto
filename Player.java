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

    public boolean setAttivo(String p){
        int i=this.cercaMano(p);
        if(this.attivo==null && i!=1){
            this.attivo=this.mano.get(i);
            this.mano.remove(i);
            return true;
        }
        return false;
    }

    public boolean sostituisci(String p){
        int i=this.cercaPanchina(p);
        if(this.attivo==null && i!=1){
            this.attivo=this.panchina.get(i);
            this.panchina.remove(i);
            return true;
        }
        return false;
    }

    public boolean aggiungiPanchina(String p){
        int indice=this.cercaMano(p);
        if(this.panchina.size()<4){
            this.panchina.add(this.mano.get(indice));
            return true;
        }
        return false;
    }

    public int cercaMano(String n){
        for (int index = 0; index < this.mano.size(); index++) {
            if(this.mano.get(index).getNome().equals(n)){
                return index;
            }
        }
        return -1;
    }

    public int cercaPanchina(String n){
        for (int index = 0; index < this.panchina.size(); index++) {
            if(this.panchina.get(index).getNome().equals(n)){
                return index;
            }
        }
        return -1;
    }

    public boolean evolvi(String p, String posizione, String f){
        int i=this.cercaMano(p);
        if(posizione.equals("attivo")){
            if(this.attivo.getNome().equals(f) && i!=1){
                this.attivo=this.mano.get(i);
                this.mano.remove(i);
                return true;
            }
        }
        else{
            for (int index = 0; index < this.panchina.size(); index++) {
                if(this.panchina.get(index).getNome().equals(f) && i!=1){
                    this.attivo=this.mano.get(i);
                    this.mano.remove(i);
                    return true;
                }
            }
            
        }
        return false;
    }

    public boolean diminuisciRitirata(){
        if(((Pokemon) this.attivo).setRitirata(-1)){
            return true;
        }
        return false;
    }

    public Carta cercaBase(){
        Carta c=this.mazzo.cercaFase("base");
        if(c!=null){
            this.mano.add(c);
            return c;
        }
        return null;
    }

    public boolean cura(String n, int val){
        if(this.attivo.getNome().equals(n)){
            ((Pokemon)this.attivo).changevita(val);
            return true;
        }
        else{
            for (int index = 0; index < this.panchina.size(); index++) {
                if(this.panchina.get(index).equals(n)){
                    ((Pokemon)this.panchina.get(index)).changevita(val);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean curaTipo(String n, int val, String t){
        if(this.attivo.getNome().equals(n) && ((Pokemon)this.attivo).getTipo().equals(t)){
            ((Pokemon)this.attivo).changevita(val);
            return true;
        }
        else{
            for (int index = 0; index < this.panchina.size(); index++) {
                if(this.panchina.get(index).equals(n) && ((Pokemon)this.panchina.get(index)).getTipo().equals(t)){
                    ((Pokemon)this.panchina.get(index)).changevita(val);
                    return true;
                }
            }
            return false;
        }
    }

    public Attacco getAttacco(int i){
        return ((Pokemon)this.attivo).getAttacco(i);
    }

    public boolean addEnergie(int i){
        ((Pokemon)this.attivo).addEnergia(i);
        return true;
    }

    public int danneggiato(int d, String stato){
        ((Pokemon)this.attivo).changevita(-d);
        ((Pokemon)this.attivo).setStato(stato);
        return ((Pokemon)this.attivo).getVita();
    }

    public int numeroV(String nome, String tipo){
        int volte=0;
        if(!nome.equals("")){
            for (int index = 0; index < this.panchina.size(); index++) {
                if(((Pokemon)this.panchina.get(index)).getNome().equals(nome)){
                    volte++;
                }
            }
        }
        else if(!tipo.equals("")){
            for (int index = 0; index < this.panchina.size(); index++) {
                if(((Pokemon)this.panchina.get(index)).getTipo().equals(tipo)){
                    volte++;
                }
            }
        }
        return volte;
    }

    public Carta find(String nome){
        Carta c=this.mazzo.cercaPokemon(nome);
        return c;
    }

    public void addPanchina(Carta c){
        if(this.panchina.size()<2){
            this.panchina.add(c);
        }
        else{
            this.mazzo.add(c);
        }
    }

    public int danniPanchina(int d, int indice){
        ((Pokemon)this.panchina.get(indice)).changevita(-d);
        return ((Pokemon)this.panchina.get(indice)).getVita();
    }

    public String ottieniNome(int i){
        return ((Pokemon)this.panchina.get(i)).getNome();
    }
}