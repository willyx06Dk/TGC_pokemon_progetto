class Attacco {
    private int energie;
    private boolean abilita;
    private int danno;
    private String effetto;

    public Attacco(int e, boolean a, int d, String ef){
        this.energie=e;
        this.abilita=a;
        this.danno=d;
        this.effetto=ef;
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
}
