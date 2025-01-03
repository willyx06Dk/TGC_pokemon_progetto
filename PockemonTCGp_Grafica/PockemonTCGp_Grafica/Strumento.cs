using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PockemonTCGp_Grafica
{
    public class Strumento : Carta
    {
        private string azione;

        public Strumento(string a, string nome, string immagine) : base(nome, immagine, "strumento")
        {
            this.azione = a;
        }

        public string GetAzione()
        {
            return this.azione;
        }
    }
} //---Dzyubanov---
