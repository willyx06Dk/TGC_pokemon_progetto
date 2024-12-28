import java.util.ArrayList;
class Player{
    private String nome;
    private boolean energia;
    private ArrayList<Carta> mano;
    private ArrayList<Carta> panchina;
    private Carta attivo;
    private ArrayList<Carta> scartate;

    public Player(){
        this.nome="";
        this.energia=false;
        this.mano=new ArrayList<Carta>();
        this.panchina=new ArrayList<Carta>();
        this.attivo=null;
        this.scartate=new ArrayList<Carta>();
    }

    public void setNome(String n){
        this.nome=n;
    }

    public void pesca(Carta c){
        this.mano.add(c);
    }

    public int cerca(Carta c){
        for(int i=0; i<this.panchina.lenght; ){
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
            this.panchina.get(i).assegna(attivo);
            this.attivo.assegna(temp);
        }
    }
}