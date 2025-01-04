import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

class server{
    public static void main(String[] args) throws IOException {
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
            }
        }
        Random r=new Random();
        turno=r.nextInt(1)+1;
        DatagramSocket socket = new DatagramSocket(12345);
        String risp1="inizia";
        String risp2="aspetta";
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
        while(fine==false){
            if(turno==1){

            }
            else{

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
}