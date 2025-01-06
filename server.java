import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

class server{
    public void main(String[] args) throws IOException {
        Player p1=null;
        Player p2=null;
        int giocatori=0;
        boolean fine=false;
        int turno=0;
        while(giocatori<2){
            DatagramSocket socket = new DatagramSocket(12345);
            byte[] buffer= new byte[1500];
            DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String messaggio = new String(packet.getData(), 0, packet.getLength());
            if(!messaggio.equals("")){
                if(p1==null){
                    p1=new Player(messaggio, packet);
                }
                else{
                    p2=new Player(messaggio, packet);
                }
                giocatori++;
                String ok="ok";
                buffer=ok.getBytes();
                DatagramPacket inserito=new DatagramPacket(buffer, buffer.length);
                inserito.setAddress(packet.getAddress());
                inserito.setPort(packet.getPort());
                socket.send(inserito);
            }
        }
        boolean scelta1=false;
        boolean scelta2=false;
        while(scelta1==false && scelta2==false){
            DatagramSocket socket = new DatagramSocket(12345);
            byte[] buffer= new byte[1500];
            DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String messaggio = new String(packet.getData(), 0, packet.getLength());
            if(!messaggio.equals("")){
                if(packet.getAddress().equals(p1.getPacket().getAddress()) && packet.getPort()==p1.getPacket().getPort()){
                    if(scelta1==false){
                        p1.leggiMazzo(messaggio);
                        scelta1=true;
                    }
                }
                else if(packet.getAddress().equals(p2.getPacket().getAddress()) && packet.getPort()==p2.getPacket().getPort()){
                    if(scelta2==false){
                        p2.leggiMazzo(messaggio);
                        scelta2=true;
                    }
                }
                String aspetta="attendi";
                buffer=aspetta.getBytes();
                DatagramPacket attesa=new DatagramPacket(buffer, buffer.length);
                packet.setAddress(packet.getAddress());
                packet.setPort(packet.getPort());
                socket.send(attesa);
            }
        }
        Random r=new Random();
        turno=r.nextInt(1)+1;
        DatagramSocket socket = new DatagramSocket(12345);
        String risp1="inizia";
        String risp2="secondo";
        byte[] b1;
        b1=risp1.getBytes();
        byte[] b2;
        b2=risp2.getBytes();
        if(turno==1){
            DatagramPacket inizia=new DatagramPacket(b1, b1.length);
            inizia.setAddress(p1.getPacket().getAddress());
            inizia.setPort(p1.getPacket().getPort());
            socket.send(inizia);
            DatagramPacket aspetta=new DatagramPacket(b2, b2.length);
            aspetta.setAddress(p2.getPacket().getAddress());
            aspetta.setPort(p2.getPacket().getPort());
            socket.send(aspetta);
        }else{
            DatagramPacket inizia=new DatagramPacket(b1, b1.length);
            inizia.setAddress(p2.getPacket().getAddress());
            inizia.setPort(p2.getPacket().getPort());
            socket.send(inizia);
            DatagramPacket aspetta=new DatagramPacket(b2, b2.length);
            aspetta.setAddress(p1.getPacket().getAddress());
            aspetta.setPort(p1.getPacket().getPort());
            socket.send(aspetta);
        }
        for (int i = 0; i < 5; i++) {
            inviaCarta(p1, socket);
            inviaCarta(p2, socket);
        }
        boolean iniziale=true;
        while(fine==false){
            if(turno==1){
                Turno(turno, p1, p2,socket, iniziale);
            }
            else{
                Turno(turno, p2, p1,socket, iniziale);
            }
            if(p1.getPunti()>=3 || p2.getPunti()>=3){
                fine=true;
            }
        }
        risp1="vince";
        risp2="perde";
        b1=risp1.getBytes();
        b2=risp2.getBytes();
        DatagramPacket vince=new DatagramPacket(b1, b1.length);
        DatagramPacket perde=new DatagramPacket(b2, b2.length);
        if(p1.getPunti()>=3){
            vince.setAddress(p1.getPacket().getAddress());
            vince.setPort(p1.getPacket().getPort());
            socket.send(vince);
            perde.setAddress(p2.getPacket().getAddress());
            perde.setPort(p2.getPacket().getPort());
            socket.send(perde);
        }
        else{
            vince.setAddress(p2.getPacket().getAddress());
            vince.setPort(p2.getPacket().getPort());
            socket.send(vince);
            perde.setAddress(p1.getPacket().getAddress());
            perde.setPort(p1.getPacket().getPort());
            socket.send(perde);
        }
    }

    public static void inviaCarta(Player p, DatagramSocket s) throws IOException{
        Carta c=p.pesca();
        byte[] vett;
        vett=c.toCSV().getBytes();
        DatagramPacket carta=new DatagramPacket(vett, vett.length);
        carta.setAddress(p.getPacket().getAddress());
        carta.setPort(p.getPacket().getPort());
        s.send(carta);
    }

    public static void inviaRisposta(Player p, DatagramSocket s, String m) throws IOException{
        byte[] vett;
        vett=m.getBytes();
        DatagramPacket risposta=new DatagramPacket(vett, vett.length);
        risposta.setAddress(p.getPacket().getAddress());
        risposta.setPort(p.getPacket().getPort());
        s.send(risposta);
    }

    public static boolean eseguiEffetto(String effetto, String n, Player p, DatagramSocket s){
        String[] informazioni=effetto.split("_");
        if(informazioni[0].equals("ritirata")){
            if(p.diminuisciRitirata()){
                p.giocata("Velocita X");
                return true;
            }
            else{
                return false;
            }
        }
        if(informazioni[0].equals("prendi")){
            Carta c=p.cercaBase();
            if(c!=null){
                byte[] vett;
                vett=c.toCSV().getBytes();
                DatagramPacket carta=new DatagramPacket(vett, vett.length);
                carta.setAddress(p.getPacket().getAddress());
                carta.setPort(p.getPacket().getPort());
                try {
                    s.send(carta);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                p.giocata("Poke Ball");
                return true;
            }
            else{
                return false;
            }
        }
        if(informazioni[0].equals("cura") && informazioni.length<3){
            if(p.cura(n, 20)){
                p.giocata("Pozione");
                return true;
            }
            else{
                return false;
            }
        }
        if(informazioni[0].equals("cura") && informazioni.length>=3){
            if(p.curaTipo(n, 50, "erba")){
                p.giocata("Erika");
                return true;
            }
            else{
                return false;
            }
        }
        if(informazioni[0].equals("tira")){
            int energie=0;
            int moneta=0;
            while(moneta==0){
                Random r1=new Random();
                moneta=r1.nextInt(2);
                if(moneta==0){
                    energie++;
                }
            }
            String en="energie;"+energie;
            byte[] vett;
            vett=en.getBytes();
            DatagramPacket energ=new DatagramPacket(vett, vett.length);
            energ.setAddress(p.getPacket().getAddress());
            energ.setPort(p.getPacket().getPort());
            try {
                s.send(energ);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            p.giocata("Misty");
            p.addEnergie(energie);
            return true;
        }
        if(informazioni[0].equals("pesca")){
            for (int index = 0; index < Integer.parseInt(informazioni[1]); index++) {
                try {
                    inviaCarta(p, s);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            p.giocata("Ricerca Accademica");
            return true;
        }
        return false;
    }

    public static void controllaAtacco(Player p1, Player p2,DatagramSocket s, int i){
        String[] dettagli=p1.getAttacco(i).getEffetto().split("_");
        int danni=0;
        String effetto="";
        String target="attivo";
        boolean ok=true;
        if(dettagli[0].equals("addormantato")){
            effetto="addormantato";
        }
        else if(dettagli[0].equals("energie")){
            if(((Pokemon)p1.getattivo()).getEnergie()>p1.getAttacco(i).getEnergie()+Integer.parseInt(dettagli[1])){
                danni+=Integer.parseInt(dettagli[2]);
            }
        }
        else if(dettagli[0].equals("togli")){
            ((Pokemon)p1.getattivo()).addEnergia(Integer.parseInt(dettagli[1]));
        }
        else if(dettagli[0].equals("avversario")){
            effetto=dettagli[1];
        }
        else if(dettagli[0].equals("paralizzato")){
            effetto="paralizzato";
        }
        else if(dettagli[0].equals("testa")){
            int testa=0;
            Random r=new Random();
            for (int j = 0; j < p1.getAttacco(i).getMoneta(); j++) {
                if(r.nextInt(2)==0){
                    testa++;
                }
            }
            danni+=testa*(Integer.parseInt(dettagli[1]));
        }
        else if(dettagli[0].equals("no")){
            effetto="allenatore";
        }
        else if(dettagli[0].equals("panchina") && dettagli[1].equals("avversario")){
            Random r=new Random();
            int scelto=r.nextInt(p2.getPanchina().size());
            target=scelto+"";
        }
        else if(dettagli[0].equals("croce")){
            Random r=new Random();
            int scelto=r.nextInt(2);
            if(scelto==1){
                ok=false;
            }
        }
        else if(dettagli[0].equals("avvelenato")){
            effetto="avvelenato";
        }
        else if(dettagli[0].equals("panchina") && dettagli[1].equals("nome")){
            int volte=0;
            volte=p1.numeroV(dettagli[1], "");
            danni=volte*(Integer.parseInt(dettagli[2]));
        }
        else if(dettagli[0].equals("panchina") && dettagli[1].equals("tipo")){
            int volte=0;
            volte=p1.numeroV("", dettagli[2]);
            danni=volte*(Integer.parseInt(dettagli[2]));
        }
        else if(dettagli[0].equals("mazzo")){
            Carta c=p1.find(dettagli[1]);
            if(c!=null){
                p1.addPanchina(c);
            }
        }
        else if(dettagli[0].equals("danneggiato")){
            if(((Pokemon)p1.getattivo()).getVita()>90){
                danni+=60;
            }
        }
        else if(dettagli[0].equals("vita")){
            ((Pokemon)p1.getattivo()).changevita(30);
        }
        if(ok){
            danni+=p1.getAttacco(i).getDanno();
        }
        if(((Pokemon)p1.getattivo()).getStato().equals("-20")){
            danni-=20;
            ((Pokemon)p1.getattivo()).setStato("");
        }
        int vita=0;
        if(target.equals("attivo")){
            vita=p2.danneggiato(danni, effetto);
        }
        else{
            vita=p2.danniPanchina(danni, Integer.parseInt(target));
        }
        //pokemon;vita rimasta;effetto
        target=p2.ottieniNome(Integer.parseInt(target));
        String situazione=target+";"+vita+";"+effetto;
        byte[] vett;
        vett=situazione.getBytes();
        DatagramPacket ritorno=new DatagramPacket(vett, vett.length);
        ritorno.setAddress(p2.getPacket().getAddress());
        ritorno.setPort(p2.getPacket().getPort());
        try {
            s.send(ritorno);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        target=p2.ottieniNome(Integer.parseInt(target));
        situazione="inflitti;"+target+";"+vita+";"+effetto;
        vett=situazione.getBytes();
        ritorno=new DatagramPacket(vett, vett.length);
        ritorno.setAddress(p1.getPacket().getAddress());
        ritorno.setPort(p1.getPacket().getPort());
        try {
            s.send(ritorno);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void controlloStato(Player p1, Player p2,DatagramSocket s) throws IOException{
        if(((Pokemon)p1.getattivo()).getVita()<=0){
            int punti=0;
            if(((Pokemon)p1.getattivo()).getEx()==1){
                punti=2;
            }
            else{
                punti=1;
            }
            p2.addPunti(punti);
            String situazione="ko;"+p2.getPunti();
            byte[] vett;
            vett=situazione.getBytes();
            DatagramPacket ko=new DatagramPacket(vett, vett.length);
            ko.setAddress(p2.getPacket().getAddress());
            ko.setPort(p2.getPacket().getPort());
            try {
                s.send(ko);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            p1.scarta();
            situazione="sconfitto";
            vett=situazione.getBytes();
            DatagramPacket sonfitto=new DatagramPacket(vett, vett.length);
            sonfitto.setAddress(p1.getPacket().getAddress());
            sonfitto.setPort(p1.getPacket().getPort());
            try {
                s.send(sonfitto);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (true) {
                byte[] buffer= new byte[1500];
                DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
                s.receive(packet);
                String messaggio = new String(packet.getData(), 0, packet.getLength());
                String[] codice=messaggio.split(";");
                if(codice[0].equals("sostituisci")){
                    if(p1.sostituisci(codice[1])){
                        inviaRisposta(p1, s, "ok");
                        break;
                    }
                    else{
                        inviaRisposta(p1, s, "no");
                    }
                }
            }
        }
    }

    public static void Turno(int turno, Player p1, Player p2,DatagramSocket s, boolean iniziale) throws IOException{
        if(iniziale==false){
            p1.inizioTurno();
        }
        else{
            iniziale=false;
        }
        inviaCarta(p1, s);
        if(((Pokemon)p1.getattivo()).getStato().equals("avvelenato")){
            int v=p1.danneggiato(10, "avvelenato");
            String situazione=p1.getattivo().getNome()+";"+v+";"+"";
            byte[] vett;
            vett=situazione.getBytes();
            DatagramPacket ritorno=new DatagramPacket(vett, vett.length);
            ritorno.setAddress(p1.getPacket().getAddress());
            ritorno.setPort(p1.getPacket().getPort());
            try {
                s.send(ritorno);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            situazione="inflitti;"+p1.getattivo()+";"+v+";"+"";
            vett=situazione.getBytes();
            ritorno=new DatagramPacket(vett, vett.length);
            ritorno.setAddress(p1.getPacket().getAddress());
            ritorno.setPort(p1.getPacket().getPort());
            try {
                s.send(ritorno);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            controlloStato(p1, p2, s);
        }
        while(true && p2.getPunti()<3){
            boolean allenatore=false;
            s = new DatagramSocket(12345);
            byte[] buffer= new byte[1500];
            DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
            s.receive(packet);
            String messaggio = new String(packet.getData(), 0, packet.getLength());
            String[] codice=messaggio.split(";");
            if(codice[0].equals("attivo")){
                if(p1.setAttivo(codice[1])){
                    inviaRisposta(p1, s, "ok");
                }
                else{
                    inviaRisposta(p1, s, "no");
                }
            }
            else if(codice[0].equals("panchina")){
                if(p1.aggiungiPanchina(codice[1])){
                    inviaRisposta(p1, s, "ok");
                }
                else{
                    inviaRisposta(p1, s, "no");
                }
            }
            else if(codice[0].equals("giocata")){
                if(codice[1].equals("strumento")){
                    if(eseguiEffetto(codice[2], codice[3], p1, s)){
                        inviaRisposta(p1, s, "ok");
                    }
                    else{
                        inviaRisposta(p1, s, "no");
                    }
                }
                else if(codice[1].equals("strumento") && allenatore==false){
                    if(eseguiEffetto(codice[2], codice[3], p1, s)){
                        inviaRisposta(p1, s, "ok");
                        allenatore=true;
                    }
                    else{
                        inviaRisposta(p1, s, "no");
                    }
                }
                else{
                    inviaRisposta(p1, s, "no");
                }
            }
            else if(codice[0].equals("sostituisci")){
                if(p1.sostituisci(codice[1])){
                    inviaRisposta(p1, s, "ok");
                }
                else{
                    inviaRisposta(p1, s, "no");
                }
            }
            else if(codice[0].equals("evolvi")){
                if(p1.evolvi(codice[1], codice[2], codice[3])){
                    inviaRisposta(p1, s, "ok");
                }
                else{
                    inviaRisposta(p1, s, "no");
                }
            }
            else if(codice[0].equals("energie")){
                if(p1.addEnergie(1)){
                    inviaRisposta(p1, s, "ok");
                }
                else{
                    inviaRisposta(p1, s, "no");
                }
            }
            else if(codice[0].equals("attacco")){
                if(!((Pokemon)p1.getattivo()).getStato().equals("paralizzato")||!((Pokemon)p1.getattivo()).getStato().equals("addormentato")){
                    controllaAtacco(p1, p2, s, Integer.parseInt(codice[1]));
                }
                else{
                    ((Pokemon)p1.getattivo()).setStato("");
                }
                controlloStato(p1, p2, s);
                turno=2;
                break;
            }
        }
    }
}

