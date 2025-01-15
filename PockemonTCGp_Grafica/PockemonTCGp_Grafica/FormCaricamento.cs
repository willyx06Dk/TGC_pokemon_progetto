using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;

namespace PockemonTCGp_Grafica
{
    public partial class FormCaricamento : Form
    {
        private UdpClient udpClient;
        private IPEndPoint serverEndpoint;
        private Label labelCaricamento;
        private bool isCaricamentoAttivo;
        private string tipoMazzo;
        public FormCaricamento(string tipoMazzo, UdpClient udpClient, IPEndPoint serverEndpoint)
        {
            InitializeComponent();

            this.tipoMazzo = tipoMazzo;
            this.udpClient = udpClient;
            this.serverEndpoint = serverEndpoint;

            ConfiguraInterfaccia();
            ImpostaDimensioniFisse();

            IniziaCaricamento();
            RiceviMessaggiDalServer();
        }

        private void ConfiguraInterfaccia()
        {
            this.Size = new Size(900, 770);
            this.Text = "Caricamento...";
            this.BackColor = Color.Black;

            labelCaricamento = new Label();
            labelCaricamento.Text = "Caricamento.";
            labelCaricamento.Font = new Font("Arial", 24, FontStyle.Bold);
            labelCaricamento.ForeColor = Color.White;
            labelCaricamento.AutoSize = true;
            labelCaricamento.Location = new Point(350, 350);

            this.Controls.Add(labelCaricamento);
        }

        //funzione per simalare un animazione
        private void IniziaCaricamento()
        {
            isCaricamentoAttivo = true;
            Task.Run(() =>
            {
                string[] statiCaricamento = { "Caricamento.", "Caricamento..", "Caricamento..." };
                int indice = 0;

                while (isCaricamentoAttivo)
                {
                    this.Invoke((MethodInvoker)(() =>
                    {
                        labelCaricamento.Text = statiCaricamento[indice];
                    }));

                    indice = (indice + 1) % statiCaricamento.Length;
                    Thread.Sleep(1000);
                }
            });
        }

        private async void RiceviMessaggiDalServer()
        {
            while (true)
            {
                UdpReceiveResult result = await udpClient.ReceiveAsync();
                string messaggio = Encoding.UTF8.GetString(result.Buffer);

                if (messaggio == "inizio gioco")
                {
                    isCaricamentoAttivo = false;

                    this.Invoke((MethodInvoker)(() =>
                    {
                        FormGioco formGioco = new FormGioco(tipoMazzo, serverEndpoint, udpClient);
                        formGioco.Show();
                        this.Hide();
                        //break;
                    }));
                    break;
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
}//Dzyubanov
