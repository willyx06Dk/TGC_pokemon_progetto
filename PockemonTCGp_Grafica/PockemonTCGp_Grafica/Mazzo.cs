using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PockemonTCGp_Grafica
{
    internal class Mazzo
    {
        private string tipo;
        private List<Carta> carte;

        public Mazzo(string t)
        {
            this.tipo = t;
            this.carte = new List<Carta>();
        }
    }
} //---Dzyubanov---
