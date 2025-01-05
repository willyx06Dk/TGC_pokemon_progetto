import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public Carta cercaFase(String f){
        for (int index = 0; index < this.carte.size(); index++) {
            String fase=((Pokemon)this.carte.get(index)).getFase();
            if(fase.equals(f)){
                this.carte.remove(this.carte.get(index));
                return this.carte.get(index);
            }
        }
        return null;
    }

    public Carta cercaPokemon(String f){
        for (int index = 0; index < this.carte.size(); index++) {
            String fase=((Pokemon)this.carte.get(index)).getNome();
            if(fase.equals(f)){
                this.carte.remove(this.carte.get(index));
                return this.carte.get(index);
            }
        }
        return null;
    }

    public void leggi() throws IOException{
        File file=new File(this.tipo);
        if(file.exists()){
            FileReader fr;
            try {
                fr = new FileReader("mazzi/"+file+".txt");
                BufferedReader bf=new BufferedReader(fr);
                for (int index = 0; index < 20; index++) {
                    String riga=bf.readLine();
                    String[] elementi=riga.split(";");
                    if(elementi[0].equals("pokemon")){
                        String nome=elementi[1];
                        String immagine=elementi[2];
                        int ritirata=Integer.parseInt(elementi[3]);
                        String debolezza=elementi[4];
                        String fase=elementi[5];
                        int ex=Integer.parseInt(elementi[6]);
                        int vita=Integer.parseInt(elementi[7]);
                        String tipo=elementi[8];
                        Pokemon p=new Pokemon(ritirata, debolezza, fase, nome, immagine, ex, vita, tipo);
                        p.carica();
                        this.carte.add(p);
                    }
                    else if(elementi[0].equals("strumento")){
                        String nome=elementi[1];
                        String immagine=elementi[2];
                        String azione=elementi[3];
                        Strumento s=new Strumento(azione, nome, immagine);
                        this.carte.add(s);
                    }
                    else{
                        String nome=elementi[1];
                        String immagine=elementi[2];
                        String azione=elementi[3];
                        Allenatore s=new Allenatore(azione, nome, immagine);
                        this.carte.add(s);
                    }
                }
                bf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Carta c){
        this.carte.add(c);
    }
}
