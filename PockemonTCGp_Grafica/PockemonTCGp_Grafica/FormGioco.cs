using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PockemonTCGp_Grafica
{
    public partial class FormGioco : Form
    {
        public string NomeGiocatore1 { get; set; }
        public string NomeGiocatore2 { get; set; }
        public string EnergiaScelta { get; set; }
        private UdpClient udpClient;
        private Thread listenerThread;
        private IPEndPoint serverEndPoint;
        private PictureBox energiaPictureBox;
        private Label etichettaNomiGiocatori;

        public Player giocatore { get; set; }
        public Player nemico { get; set; }

        public FormGioco(string energiaScelta, IPEndPoint serverEndPoint, UdpClient udpClient)
        {
            EnergiaScelta = energiaScelta;
            this.serverEndPoint = serverEndPoint;
            this.udpClient = udpClient;

            InitializeComponent();
            ImpostaDimensioniFisse();
            CaricaBackground();
            AggiungiImmagineEnergia();
            AggiungiEtichettaNomiGiocatori();
            AvviaListenerUdp();
        }

        private void ImpostaDimensioniFisse()
        {
            this.Size = new Size(900, 770);
            this.FormBorderStyle = FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
        }

        private void CaricaBackground()
        {
            string percorsoImmagine = Path.Combine(Application.StartupPath, "pokemonTCG_img", "game_background.png");

            if (File.Exists(percorsoImmagine))
            {
                this.BackgroundImage = Image.FromFile(percorsoImmagine);
                this.BackgroundImageLayout = ImageLayout.Stretch;
            }
            else
            {
                MessageBox.Show("Sfondo di gioco non trovato!");
            }
        }

        private void AggiungiImmagineEnergia()
        {
            energiaPictureBox = new PictureBox
            {
                Size = new Size(50, 50),
                Location = new Point(this.ClientSize.Width - 60, this.ClientSize.Height - 60),
                BackColor = Color.Transparent,
                SizeMode = PictureBoxSizeMode.StretchImage
            };

            string percorsoEnergia = Path.Combine(Application.StartupPath, "pokemonTCG_img", "energie", EnergiaScelta + ".png");

            if (File.Exists(percorsoEnergia))
            {
                energiaPictureBox.Image = Image.FromFile(percorsoEnergia);
            }
            else
            {
                MessageBox.Show("Immagine dell'energia non trovata!");
            }

            energiaPictureBox.MouseDown += EnergiaPictureBox_MouseDown; //trascinamento
            this.Controls.Add(energiaPictureBox);
        }

        private void EnergiaPictureBox_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                energiaPictureBox.DoDragDrop(energiaPictureBox.Image, DragDropEffects.Move);
            }
        }

        //permette di posizionare l'immagine in un'altra area (es. la carta attiva in futuro)
        protected override void OnDragOver(DragEventArgs e)
        {
            e.Effect = DragDropEffects.Move;
        }

        //***
        private void AvviaListenerUdp()
        {
            listenerThread = new Thread(() =>
            {
                while (true)
                {
                    try
                    {
                        IPEndPoint remoteEndPoint = new IPEndPoint(IPAddress.Any, 0);
                        byte[] datiRicevuti = udpClient.Receive(ref remoteEndPoint);
                        string messaggio = Encoding.UTF8.GetString(datiRicevuti);

                        this.Invoke(new Action(() =>
                        {
                            if (messaggio.StartsWith("NOMI:"))
                            {
                                string[] nomi = messaggio.Substring(5).Split(',');
                                NomeGiocatore1 = nomi[0];
                                NomeGiocatore2 = nomi[1];
                                etichettaNomiGiocatori.Text = $"{NomeGiocatore1} vs {NomeGiocatore2}";
                            }
                            else if (messaggio.StartsWith("CARTE:"))
                            {
                                RiceviCarteIniziali(messaggio.Substring(6));
                            }
                        }));
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show($"Errore durante la ricezione: {ex.Message}");
                        break;
                    }
                }
            });
            listenerThread.IsBackground = true;
            listenerThread.Start();
        }

        private void RiceviCarteIniziali(string messaggio)
        {
            //pikachu|pokemon,erika|allenatore
            try
            {
                string[] carteDati = messaggio.Split(',');

                foreach (string cartaInfo in carteDati)
                {
                    string[] dettagliCarta = cartaInfo.Split('|');
                    string nomeCarta = dettagliCarta[0];
                    string tipoCarta = dettagliCarta[1];


                    string percorsoImmagine = TrovaImmagineCarta(nomeCarta);
                    if (!string.IsNullOrEmpty(percorsoImmagine) && File.Exists(percorsoImmagine))
                    {
                        Carta carta = new Carta(nomeCarta, percorsoImmagine, tipoCarta);

                        giocatore.AggiungiInMano(carta);
                    }
                }

                MessageBox.Show("Carte iniziali ricevute e caricate nel mazzo!");
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Errore durante la ricezione delle carte: {ex.Message}");
            }
        }

        /****
        //metodo per creare una carta in base al tipo
       /* private Carta CreaCarta(string nome, string tipo)
        {
            switch (tipo.ToLower())
            {
                case "pokemon":
                    return new Pokemon(nome);
                case "allenatore":
                    return new Allenatore(nome);
                case "strumento":
                    return new Strumento(nome);
                default:
                    throw new Exception($"Tipo di carta sconosciuto: {tipo}");
            }
        }*/

        private string TrovaImmagineCarta(string nomeCarta)
        {
            string percorsoMazzi = Path.Combine(Application.StartupPath, "mazzi", EnergiaScelta);
            string percorsoImmagine = Path.Combine(percorsoMazzi, nomeCarta + ".png");

            if (!File.Exists(percorsoImmagine))
            {
                MessageBox.Show($"Immagine per la carta '{nomeCarta}' non trovata in {percorsoImmagine}");
                return null;
            }

            return percorsoImmagine;
        }

        //per gestire il mes "player1 vs player2"
        private void AggiungiEtichettaNomiGiocatori()
        {
            etichettaNomiGiocatori = new Label
            {
                Text = "Caricamento nomi...",
                Location = new Point(350, 700),
                Size = new Size(200, 30),
                ForeColor = Color.White,
                TextAlign = ContentAlignment.MiddleCenter
            };
            this.Controls.Add(etichettaNomiGiocatori);
        }

        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            listenerThread?.Abort();
            udpClient?.Close();
            base.OnFormClosing(e);
        }
    }
}