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
    public partial class FormGioco : Form
    {
        public string EnergiaScelta { get; set; }
        private PictureBox energiaPictureBox;

        public FormGioco(string energiaSelezionata)
        {
            InitializeComponent();
            EnergiaScelta = energiaSelezionata;
            ImpostaDimensioniFisse();
            CaricaBackground();
            AggiungiImmagineEnergia();
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
    }
}