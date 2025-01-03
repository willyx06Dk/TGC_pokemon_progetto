using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PockemonTCGp_Grafica
{
    internal class Allenatore : Carta
    {
        private string azione;

        public Allenatore(string a, string nome, string immagine) : base(nome, immagine, "allenatore")
        {
            this.azione = a;
        }

        public string GetAzione()
        {
            return this.azione;
        }
    }
} //---Dzyubanov---
