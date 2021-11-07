package com.pokedex.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokedex.models.Pokemon;

public class Result {
	@JsonProperty("results")
	public List<Pokemon> listPokemon;
	
	public List<Pokemon> getLisPokemon() {
		return listPokemon;
	}

	public void setLisPokemon(List<Pokemon> listPokemon) {
		this.listPokemon = listPokemon;
	}
}
