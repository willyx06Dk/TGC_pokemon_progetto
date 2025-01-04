using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PockemonTCGp_Grafica
{
    public partial class FormSceltaMazzo : Form
    {
        public FormSceltaMazzo()
        {
            InitializeComponent();
            CaricaEnergiaImages();
            this.Size = new Size(900, 770);
            ImpostaDimensioniFisse();
        }

        private void CaricaEnergiaImages()
        {
            string percorsoImmagini = System.IO.Path.Combine(Application.StartupPath, "pokemonTCG_img", "energie");
            string[] energie = { "fuoco.png", "acqua.png", "erba.png", "lampo.png", "psico.png", "lotta.png", "oscurità.png" };

            int xPos = 50; //pos iniziale delle immagini
            foreach (string energia in energie)
            {
                string percorsoCompleto = System.IO.Path.Combine(percorsoImmagini, energia);
                if (System.IO.File.Exists(percorsoCompleto))
                {
                    PictureBox pictureBox = new PictureBox
                    {
                        Image = Image.FromFile(percorsoCompleto),
                        SizeMode = PictureBoxSizeMode.StretchImage,
                        Size = new Size(100, 100),
                        Location = new Point(xPos, 300),
                        Cursor = Cursors.Hand,
                        Tag = energia //salva il nome dell'immagine per identificare il tipo di mazzo
                    };

                    pictureBox.MouseEnter += (s, e) => { pictureBox.BackColor = Color.FromArgb(128, 255, 255, 255); };
                    pictureBox.MouseLeave += (s, e) => { pictureBox.BackColor = Color.Transparent; };
                    pictureBox.Click += PictureBox_Click;

                    this.Controls.Add(pictureBox);
                    xPos += 120; //sposta la prossima immagine più a destra
                }
                else
                {
                    MessageBox.Show($"Immagine non trovata: {energia}");
                }
            }
        }

        private void PictureBox_Click(object sender, EventArgs e)
        {
            if (sender is PictureBox pictureBox)
            {
                string tipoMazzo = pictureBox.Tag.ToString().Replace(".png", ""); 
                InviaMazzoAlServer(tipoMazzo);
                MessageBox.Show($"Mazzo scelto: {tipoMazzo}");
                // **** passa al form del gioco bla bla
            }
        }

        private void InviaMazzoAlServer(string tipoMazzo)
        {
            try
            {
                using (UdpClient udpClient = new UdpClient())
                {
                    string ipServer = "127.0.0.1"; 
                    int portaServer = 12345;      
                    byte[] dati = Encoding.UTF8.GetBytes(tipoMazzo);
                    udpClient.Send(dati, dati.Length, ipServer, portaServer);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Errore durante l'invio del mazzo: {ex.Message}", "Errore", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void ImpostaDimensioniFisse()
        {
            this.FormBorderStyle = FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
        }
    }
} //---Dzyubanov---
