using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PockemonTCGp_Grafica
{
    public partial class FormSceltaMazzo : Form
    {
        private FlowLayoutPanel pannelloEnergie;

        public FormSceltaMazzo()
        {
            InitializeComponent();

            this.Text = "Scelta del Mazzo";
            this.Size = new Size(900, 770);
            this.StartPosition = FormStartPosition.CenterScreen;
            this.BackColor = Color.White;

            ImpostaDimensioniFisse();
            CaricaEnergie();
        }

        private void CaricaEnergie()
        {
            string percorsoCartella = Path.Combine(Application.StartupPath, "pokemonTCG_img", "energie");

            if (!Directory.Exists(percorsoCartella))
            {
                MessageBox.Show("La cartella delle immagini non è stata trovata.", "Errore", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            //creazione del pannello per contenere le immagini
            pannelloEnergie = new FlowLayoutPanel();
            pannelloEnergie.Dock = DockStyle.Fill;
            pannelloEnergie.AutoScroll = true;
            pannelloEnergie.FlowDirection = FlowDirection.LeftToRight;
            pannelloEnergie.WrapContents = true;

            //caricamento immagini
            var fileImmagini = Directory.GetFiles(percorsoCartella, "*.png");
            foreach (var file in fileImmagini)
            {
                PictureBox immagine = new PictureBox
                {
                    Image = Image.FromFile(file),
                    Size = new Size(100, 100), // Dimensioni di ogni immagine
                    SizeMode = PictureBoxSizeMode.Zoom, // Adatta l'immagine all'area
                    Cursor = Cursors.Hand,
                    Tag = Path.GetFileNameWithoutExtension(file) // Nome del file senza estensione
                };

                // Aggiungi eventi per il mouse
                immagine.MouseEnter += (s, e) => immagine.BackColor = Color.FromArgb(128, Color.Gray); // Effetto trasparenza
                immagine.MouseLeave += (s, e) => immagine.BackColor = Color.Transparent; // Rimuovi trasparenza
                immagine.Click += (s, e) =>
                {
                    // Invia il nome del mazzo scelto al server
                    string nomeMazzo = immagine.Tag.ToString();
                    MessageBox.Show($"Hai scelto il mazzo: {nomeMazzo}", "Scelta del Mazzo", MessageBoxButtons.OK, MessageBoxIcon.Information);

                    InviaNomeMazzoAlServer(nomeMazzo);
                };

                pannelloEnergie.Controls.Add(immagine);
            }

            this.Controls.Add(pannelloEnergie);
        }

        private void InviaNomeMazzoAlServer(string nomeMazzo)
        {
            try
            {
                using (System.Net.Sockets.TcpClient client = new System.Net.Sockets.TcpClient("127.0.0.1", 12345))
                using (StreamWriter writer = new StreamWriter(client.GetStream()))
                {
                    writer.WriteLine(nomeMazzo);
                    writer.Flush();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Errore durante l'invio del mazzo al server: {ex.Message}", "Errore", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
} //---Dzyubanov---
