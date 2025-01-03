using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PockemonTCGp_Grafica
{
    public class Attacco
    {
        private int energie;
        private int abilita;
        private int danno;
        private string effetto;
        private int moneta;

        public Attacco(int e, int a, int d, string ef, int m)
        {
            this.energie = e;
            this.abilita = a;
            this.danno = d;
            this.effetto = ef;
            this.moneta = m;
        }

        public int GetEnergie()
        {
            return this.energie;
        }

        public int IsAbilita()
        {
            return this.abilita;
        }

        public int GetDanno()
        {
            return this.danno;
        }

        public string GetEffetto()
        {
            return this.effetto;
        }

        public int GetMoneta()
        {
            return this.moneta;
        }
    }
} //---Dzyubanov---
