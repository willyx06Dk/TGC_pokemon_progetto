import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Pokemon extends Carta{
    private ArrayList<Attacco> attacchi;
    private int ritirata;
    private String debolezza;
    //la fase dice da chi si evolve
    private String fase;
    private int energie;
    private int ex;
    private int vita;
    private String tipo;

    public Pokemon(int r, String d, String f, String nome, String immagine, int e, int vita, String t){
        super(nome, immagine, "pokemon");
        this.attacchi=new ArrayList<Attacco>();
        this.ritirata=r;
        this.debolezza=d;
        this.fase=f;
        this.energie=0;
        this.ex=e;
        this.vita=vita;
        this.tipo=t;
    }

    public Attacco getAttacco(int pos){
        if(pos>-1&&pos<this.attacchi.size()){
            return this.attacchi.get(pos);
        }
        return null;
    }

    public int getRitirata(){
        return this.ritirata;
    }

    public String getDebolezza(){
        return this.debolezza;
    }

    public String getFase(){
        return this.fase;
    }

    public int getEnergie(){
        return this.energie;
    }

    public void addEnergia(){
        this.energie++;
    }

    public void rimuoviEnergie(int n){
        this.energie=this.energie-n;
        if(this.energie<0){
            this.energie=0;
        }
    }

    public int getEx(){
        return this.ex;
    }

    public int getVita(){
        return this.vita;
    }

    public String getTipo(){
        return this.tipo;
    }

    public void changevita(int val){
        this.vita+=val;
    }

    public void carica() throws IOException{
        File file=new File(super.getNome());
        if(file.exists()){
            FileReader fr;
            try {
                fr = new FileReader("attacchi/"+file+".txt");
                BufferedReader bf=new BufferedReader(fr);
                String riga;
                while((riga=bf.readLine())!=null){
                    String[] elementi=riga.split(";");
                    int energie=Integer.parseInt(elementi[0]);
                    int abilita=Integer.parseInt(elementi[1]);
                    int danno=Integer.parseInt(elementi[2]);
                    String effetto=elementi[3];
                    int moneta=Integer.parseInt(elementi[4]);
                    Attacco a=new Attacco(energie, abilita, danno, effetto, moneta);
                    this.attacchi.add(a);
                }
                bf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}