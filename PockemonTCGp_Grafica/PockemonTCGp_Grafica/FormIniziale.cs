using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Sockets;

namespace PockemonTCGp_Grafica
{
    public partial class FormIniziale : Form
    {
        private TextBox casellaNome;
        private Button pulsanteInizia;
        private Label etichettaErrore;
        private Player giocatore;

        public FormIniziale()
        {
            InitializeComponent();

            menu();
            CaricaBackground();
            this.Size = new System.Drawing.Size(900, 770);  //imposta le dimensioni della finestra
            ImpostaDimensioniFisse();
        }

        private void menu()
        {
            //---casella di testo per il nome---
            casellaNome = new TextBox();
            casellaNome.Location = new System.Drawing.Point(350, 520);
            casellaNome.Size = new System.Drawing.Size(200, 30);
            //casellaNome.Text = "Inserisci nome";
            //casellaNome.ForeColor = Color.Gray;

            SetRoundedCorners(casellaNome, 10);  //bordi arrotondati alla casella di testo

            //---pulsante "Inizia"---
            pulsanteInizia = new Button();
            pulsanteInizia.Text = "Inizia";
            pulsanteInizia.Location = new System.Drawing.Point(400, 570);
            pulsanteInizia.Size = new System.Drawing.Size(100, 40);
            pulsanteInizia.Click += PulsanteInizia_Click;

            //nero & trasparente
            pulsanteInizia.BackColor = Color.FromArgb(128, 0, 0, 0);
            pulsanteInizia.FlatStyle = FlatStyle.Flat;
            pulsanteInizia.FlatAppearance.BorderSize = 0; 

            //bordi arrotondati per il pulsante
            SetRoundedCorners(pulsanteInizia, 10);

            //gestisce gli eventi del mouse per cambiare il colore del pulsante
            pulsanteInizia.MouseEnter += (sender, e) =>
            {
                pulsanteInizia.BackColor = Color.FromArgb(128, 169, 169, 169);  
            };
            pulsanteInizia.MouseLeave += (sender, e) =>
            {
                pulsanteInizia.BackColor = Color.FromArgb(128, 0, 0, 0);  
            };

            //---etichetta per l'errore (nascosta inizialmente)---
            etichettaErrore = new Label();
            etichettaErrore.Text = "Il nome è obbligatorio!";
            etichettaErrore.ForeColor = System.Drawing.Color.Red;
            etichettaErrore.Location = new System.Drawing.Point(350, 550);  
            etichettaErrore.Size = new System.Drawing.Size(200, 20);
            etichettaErrore.Visible = false;  

            this.Controls.Add(casellaNome);
            this.Controls.Add(pulsanteInizia);
            this.Controls.Add(etichettaErrore);
        }

        //angoli arrotondati per il controllo
        private void SetRoundedCorners(Control control, int radius)
        {
            using (GraphicsPath path = new GraphicsPath())
            {
                path.StartFigure();
                path.AddArc(0, 0, radius, radius, 180, 90); 
                path.AddArc(control.Width - radius - 1, 0, radius, radius, 270, 90); 
                path.AddArc(control.Width - radius - 1, control.Height - radius - 1, radius, radius, 0, 90); 
                path.AddArc(0, control.Height - radius - 1, radius, radius, 90, 90);
                path.CloseFigure();

                control.Region = new Region(path);
            }
        }

        //metodo per caricare il background
        private void CaricaBackground()
        {
            string percorsoImmagine = Path.Combine(Application.StartupPath, "pokemonTCG_img", "background.jpg");

            if (File.Exists(percorsoImmagine))
            {
                this.BackgroundImage = System.Drawing.Image.FromFile(percorsoImmagine);
                this.BackgroundImageLayout = ImageLayout.Stretch;  // Si adatta al form
            }
            else
            {
                MessageBox.Show("Sfondo non trovato!");
            }
        }

        private void PulsanteInizia_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(casellaNome.Text) || casellaNome.Text == "Inserisci nome")
            {
                etichettaErrore.Visible = true;
            }
            else
            {
                giocatore = new Player(casellaNome.Text);

                //nascondi l'errore e avvia il gioco
                etichettaErrore.Visible = false;
                MessageBox.Show($"Benvenuto, {giocatore.Nome}!");

                //invio del nome al server
                InviaNomeAlServer(giocatore.Nome);

                //passa alla schermata di gioco successiva *****
                this.Hide();
            }
        }

        private void InviaNomeAlServer(string nomeGiocatore)
        {
            try
            {
                string ipServer = "127.0.0.1";  
                int portaServer = 12345;         

                using (Socket clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp))
                {
                    clientSocket.Connect(ipServer, portaServer);
                    byte[] byteNome = Encoding.UTF8.GetBytes(nomeGiocatore);
                    clientSocket.Send(byteNome);

                    clientSocket.Close();
                }

                MessageBox.Show("Nome inviato al server!");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Errore durante la connessione al server: " + ex.Message);
            }
        }

        //impedisce la modifica delle dimensioni del form iniziale
        private void ImpostaDimensioniFisse()
        {
            this.FormBorderStyle = FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.StartPosition = FormStartPosition.CenterScreen;
        }
    }
}