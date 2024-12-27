class Player{
    private String nome;
    private boolean energia;
    private Carte[] mano;
    private Carta attivo;

    public Player(){
        this.nome="";
        this.energia=false;
        this.mano=new Carte[7];
        this.attivo=null;
    }
}