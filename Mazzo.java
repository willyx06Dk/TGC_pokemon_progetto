import java.util.ArrayList;
import java.util.Random;

class Mazzo {
    private String tipo;
    private ArrayList<Carta> carte;

    public Mazzo(String t){
        this.tipo=t;
        this.carte=new ArrayList<Carta>();
    }

    public Carta random(){
        Random r=new Random();
        int index=r.nextInt(this.carte.size());
        Carta c=this.carte.get(index);
        this.carte.remove(c);
        return c;
    }
}
