using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PockemonTCGp_Grafica
{
    public partial class FormSceltaMazzo : Form
    {
        private UdpClient udpClient;
        private IPEndPoint serverEndpoint;
        private string[] mazzoPaths;
        private PictureBox[] mazziImages;

        public FormSceltaMazzo(UdpClient udpClient, IPEndPoint serverEndpoint)
        {
            InitializeComponent();

            this.udpClient = udpClient;
            this.serverEndpoint = serverEndpoint;

            this.Size = new Size(900, 770);
            CaricaBackground();
            MostraMazzi();
            ImpostaDimensioniFisse();
        }

        private void CaricaBackground()
        {
            string percorsoImmagine = Path.Combine(Application.StartupPath, "pokemonTCG_img", "background.jpg");
            if (File.Exists(percorsoImmagine))
            {
                this.BackgroundImage = Image.FromFile(percorsoImmagine);
                this.BackgroundImageLayout = ImageLayout.Stretch;
            }
            else
            {
                MessageBox.Show("Sfondo non trovato!");
            }
        }

        private void MostraMazzi()
        {
            string energieFolder = Path.Combine(Application.StartupPath, "pokemonTCG_img", "energie");
            if (!Directory.Exists(energieFolder))
            {
                MessageBox.Show("Cartella delle energie non trovata!");
                return;
            }

            mazzoPaths = Directory.GetFiles(energieFolder, "*.png");
            mazziImages = new PictureBox[mazzoPaths.Length];

            int startX = 50, startY = 200, spacing = 20;
            for (int i = 0; i < mazzoPaths.Length; i++)
            {
                PictureBox mazzoImg = new PictureBox();
                mazzoImg.Image = Image.FromFile(mazzoPaths[i]);
                mazzoImg.Size = new Size(120, 120);
                mazzoImg.SizeMode = PictureBoxSizeMode.StretchImage;
                mazzoImg.Location = new Point(startX + i * (120 + spacing), startY);

                mazzoImg.MouseEnter += (sender, e) =>
                {
                    mazzoImg.BackColor = Color.FromArgb(128, 0, 0, 0);
                };
                mazzoImg.MouseLeave += (sender, e) =>
                {
                    mazzoImg.BackColor = Color.Transparent;
                };

                mazzoImg.Click += (sender, e) => InviaMazzoScelto(mazzoPaths[i]);

                this.Controls.Add(mazzoImg);
                mazziImages[i] = mazzoImg;
            }
        }

        private async void InviaMazzoScelto(string mazzo)
        {
            string nomeMazzo = Path.GetFileNameWithoutExtension(mazzo);
            byte[] data = Encoding.UTF8.GetBytes(nomeMazzo);

            await udpClient.SendAsync(data, data.Length, serverEndpoint);

            RiceviMessaggiDalServer(nomeMazzo);
        }

        private async void RiceviMessaggiDalServer(string nomeMazzo)
        {
            while (true)
            {
                UdpReceiveResult result = await udpClient.ReceiveAsync();
                string messaggio = Encoding.UTF8.GetString(result.Buffer);

                if (messaggio == "attendi")
                {
                    FormCaricamento formCaricamento = new FormCaricamento(udpClient, serverEndpoint);
                    this.Hide();
                    formCaricamento.ShowDialog();
                    this.Close();
                    break;
                }
                else if (messaggio == "inizio gioco")
                {
                    //****FormGioco formGioco = new FormGioco(udpClient, serverEndpoint, nomeMazzo);
                    //formGioco.Show();
                    //this.Hide();
                   // break;


                    /*FormGioco formGioco = new FormGioco();
                    this.Hide();
                    formGioco.ShowDialog();
                    this.Close();
                    break;*/
                }
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
