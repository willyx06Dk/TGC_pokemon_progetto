using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Windows.Forms.VisualStyles.VisualStyleElement.TextBox;

namespace PockemonTCGp_Grafica
{
    public class Player
    {
        public string Nome { get; set; }
        public int Punti { get; set; }
        /*public List<Carta> CarteInMano { get; private set; }
        public Pokemon PokemonAttivo { get; set; }
        public List<Pokemon> Panchina { get; private set; }
        public List<Carta> CarteScartate { get; private set; }
        public Energia EnergiaAssegnata { get; set; }*/

        public Player(string nome)
        {
            Nome = nome;
            Punti = 0;
            /*CarteInMano = new List<Carta>();
            Panchina = new List<Pokemon>(3); // max 3 Pokémon in panchina
            CarteScartate = new List<Carta>();*/
        }

       /* public void PescaCarta(Carta carta)
        {
            CarteInMano.Add(carta);
        }

        public void ScartaCarta(Carta carta)
        {
            CarteInMano.Remove(carta);
            CarteScartate.Add(carta);
        }

        public bool AggiungiInPanchina(Pokemon pokemon)
        {
            if (Panchina.Count < 3)
            {
                Panchina.Add(pokemon);
                return true;
            }
            return false; // panchina piena
        }*/
    }
}
