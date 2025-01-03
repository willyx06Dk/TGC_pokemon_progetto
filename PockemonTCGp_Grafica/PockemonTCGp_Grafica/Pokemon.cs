using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Generic;
using static System.Windows.Forms.VisualStyles.VisualStyleElement.TextBox;

namespace PockemonTCGp_Grafica
{
    public class Pokemon : Carta
    {
        private List<Attacco> attacchi;
        private int ritirata;
        private string debolezza;
        private string fase; //indica da chi si evolve
        private int energie;
        private int ex;
        private int vita;
        private string tipo;

        public Pokemon(int r, string d, string f, string nome, string immagine, int e, int v, string t)
            : base(nome, immagine, "pokemon")
        {
            this.attacchi = new List<Attacco>();
            this.ritirata = r;
            this.debolezza = d;
            this.fase = f;
            this.energie = 0;
            this.ex = e;
            this.vita = v;
            this.tipo = t;
        }

        public Attacco GetAttacco(int pos)
        {
            if (pos > -1 && pos < this.attacchi.Count)
            {
                return this.attacchi[pos];
            }
            return null;
        }

        public int GetRitirata()
        {
            return this.ritirata;
        }

        public string GetDebolezza()
        {
            return this.debolezza;
        }

        public string GetFase()
        {
            return this.fase;
        }

        public int GetEnergie()
        {
            return this.energie;
        }

        public void AddEnergia()
        {
            this.energie++;
        }

        public void RimuoviEnergie(int n)
        {
            this.energie -= n;
            if (this.energie < 0)
            {
                this.energie = 0;
            }
        }

        public int GetEx()
        {
            return this.ex;
        }

        public int GetVita()
        {
            return this.vita;
        }

        public string GetTipo()
        {
            return this.tipo;
        }

        public void ChangeVita(int val)
        {
            this.vita += val;
        }
    }
} //---Dzyubanov---
