class Strumento extends Carta{
    private String azione;

    public Strumento(String a, String nome, String immagine){
        super(nome, immagine, "strumento");
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