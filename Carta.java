class Carta{
    private String nome;
    private String img;
    private String categoria;

    public Carta(String n, String i, String c){
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

    public boolean corrisponde(Carta c){
        boolean si=false;
        if(this.nome.equals(c.getNome())){
            if(this.img.equals(c.getImg())){
                if(this.categoria.equals(c.getCategoria())){
                    si=true;
                }
            }
        }
        return si;
    }
}