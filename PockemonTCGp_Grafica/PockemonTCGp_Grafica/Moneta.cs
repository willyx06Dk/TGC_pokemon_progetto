using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PockemonTCGp_Grafica
{
    public class Moneta
    {
        public Image Faccia { get; private set; }
        public Image Retro { get; private set; }
        private Random random;

        public Moneta(Image faccia, Image retro)
        {
            Faccia = faccia;
            Retro = retro;
            random = new Random();
        }

        public string Lancia()
        {
            return random.Next(0, 2) == 0 ? "Faccia" : "Retro";
        }
    }
}
