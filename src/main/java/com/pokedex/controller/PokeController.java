package com.pokedex.controller;

import com.pokedex.models.Pokemon;
import com.pokedex.models.Result;
import com.pokedex.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RequestMapping("/api/poke")
public class PokeController {

	@Autowired
	private RestTemplate clienteRest;

	@RequestMapping("/all")
	public List<Pokemon> getPokes() {
		List<Pokemon> pokes = new ArrayList<>();
		Result result = clienteRest.getForObject("https://pokeapi.co/api/v2/pokemon/?offset=0&limit=20", Result.class);
		int i = 1;
		for (Pokemon poke : result.getLisPokemon()) {
			poke = clienteRest.getForObject("https://pokeapi.co/api/v2/pokemon/" + i + "/", Pokemon.class);
			pokes.add(poke);
			i++;
		}
		return pokes;
	}

	@RequestMapping("{id}/info")
	public PokemonJson getPokemon(@PathVariable("id") String id) {
		RestTemplate clienteRest = new RestTemplate();
		ArrayList<String> data = new ArrayList<String>();
		Pokemon p;
		p = clienteRest.getForObject("https://pokeapi.co/api/v2/pokemon/" + id, Pokemon.class);
		PokemonJson pokemon = new PokemonJson();
		pokemon.setId(p.getId());
		pokemon.setName(p.getName());
		pokemon.setWeight(p.getWeight());
		pokemon.setImage(p.getSprites().getFront_default());

		for (int i = 0; i < p.getAbilities().size(); i += 1) {
			pokemon.addAbility(p.getAbilities().get(i).getAbility().getName());
		}
		for (int i = 0; i < p.getTypes().size(); i += 1) {
			pokemon.addType(p.getTypes().get(i).getType().getName());
		}
		return pokemon;
	}

	@RequestMapping("/{id}/evoluciones")
	public EvolutionJson getEvolutions(@PathVariable("id") int id) {
		RestTemplate clienteRest = new RestTemplate();
		if ((id) > ((id) / 3)) {
			id = (int) Math.ceil((id) / (3.0));
		}

		Chain p = clienteRest.getForObject("https://pokeapi.co/api/v2/evolution-chain/" + id, Chain.class);
		EvolutionJson evoluciones = new EvolutionJson();
		assert p != null;

		Evolution evo = p.getChain();

		while (evo != null) {

			evoluciones.addPokemon(evo.getSpecies().getName());

			if (!evo.getEvolves_to().isEmpty())
				evo = evo.getEvolves_to().get(0);
			else
				evo = null;

		}

		return evoluciones;
	}

}
