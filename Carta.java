class Carta{
    private String nome;
    private String img;
    private String categoria;

    public Carta(Strng n, String i, String c){
        this.nome=n;
        this.img=i;
        this.categoria=c;
    }

    public String getNome(){
        return this.nome;
    }

    public String getImg(){
        return this.img;
    }

    public String getCategoria(){
        return this.categoria;
    }
}