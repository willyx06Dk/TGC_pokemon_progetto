import java.util.ArrayList;

class Pokemon extends Carta{
    private ArrayList<Attacco> attacchi;
    private int ritirata;
    private String debolezza;
    //la fase dice da chi si evolve
    private String fase;
    private int energie;

    public Pokemon(ArrayList<Attacco> a, int r, String d, String f, String nome, String immagine){
        super(nome, immagine, "pokemon");
        this.attacchi=new ArrayList<Attacco>();
        for (int index = 0; index < a.size(); index++) {
            this.attacchi.add(a.get(index));
        }
        this.ritirata=r;
        this.debolezza=d;
        this.fase=f;
        this.energie=0;
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
}