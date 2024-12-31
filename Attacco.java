class Attacco {
    private int energie;
    private boolean abilita;
    private int danno;
    private String effetto;
    private int moneta;

    public Attacco(int e, boolean a, int d, String ef, int m){
        this.energie=e;
        this.abilita=a;
        this.danno=d;
        this.effetto=ef;
        this.moneta=m;
    }

    public int getEnergie(){
        return this.energie;
    }

    public boolean isAbilita(){
        return this.abilita;
    }

    public int getDanno(){
        return this.danno;
    }

    public String getEffetto(){
        return this.effetto;
    }

    public int getMoneta(){
        return this.moneta;
    }
}
