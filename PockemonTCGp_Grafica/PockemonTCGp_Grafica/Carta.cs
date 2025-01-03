﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PockemonTCGp_Grafica
{
    public class Carta
    {
        protected string Nome { get; set; }
        protected string Immagine { get; set; }
        protected string TipoCarta { get; set; }

        public Carta(string nome, string immagine, string tipoCarta)
        {
            this.Nome = nome;
            this.Immagine = immagine;
            this.TipoCarta = tipoCarta;
        }

        public string GetNome()
        {
            return this.Nome;
        }

        public string GetImg()
        {
            return this.Immagine;
        }

        public string GetCategoria()
        {
            return this.TipoCarta;
        }
    }
} //---Dzyubanov---
