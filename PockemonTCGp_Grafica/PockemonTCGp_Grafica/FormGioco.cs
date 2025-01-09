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

        public FormGioco(string energiaScelta, IPEndPoint serverEndPoint, UdpClient udpClient)
        {
            EnergiaScelta = energiaScelta;
            this.serverEndPoint = serverEndPoint;
            this.udpClient = udpClient;

            InitializeComponent();
            ImpostaDimensioniFisse();
            CaricaBackground();
            AggiungiImmagineEnergia();
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
            string percorsoImmagine = Path.Combine(Application.StartupPath, "pokemonTCG_img", "game_background.jpeg");

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

                        // Processa il messaggio ricevuto
                        this.Invoke(new Action(() =>
                        {
                            MessageBox.Show($"Messaggio ricevuto: {messaggio}");
                        }));
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show($"Errore durante la ricezione: {ex.Message}", "Errore", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        break;
                    }
                }
            });
            listenerThread.IsBackground = true;
            listenerThread.Start();
        }

        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            listenerThread?.Abort();
            base.OnFormClosing(e);
        }
    }
}