class Allenatore extends Carta{
    private String azione;

    public Allenatore(String a, String nome, String immagine){
        super(nome, immagine, "allenatore");
        this.azione=a;
    }

    public String getAzione(){
        return this.azione;
    }

    public String toCSV(){
        String s=super.toCSV()+";"+this.azione;
        return s;
    }
}