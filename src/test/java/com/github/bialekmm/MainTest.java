package com.github.bialekmm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testBalancingGenetic() {
            List<Individual> individuals = new ArrayList<>();
            individuals.add(new Individual("Johnny",8));
            individuals.add(new Individual("Robbie",5));
            individuals.add(new Individual("Juliet",3));
            individuals.add(new Individual("Scarlet",5));
            individuals.add(new Individual("Jude",9));
            individuals.add(new Individual("Deborah",6));

            int teamsNum = 3;
            List<List<Individual>> teams = Main.balancingGenetic(individuals, teamsNum, 1000);

            assertNotNull(teams);
            assertEquals(teamsNum, teams.size());
    }

    @Test
    public void testBalancingGeneticWithOneIndividual() {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual("Johnny", 8));

        List<List<Individual>> teams = Main.balancingGenetic(individuals, 3, 1000);

        assertNotNull("Teams should not be null", teams);
        assertEquals( "Expected 1 team", 1, teams.size());
        assertEquals("Expected 1 player in the team", 1, teams.get(0).size());
    }

    @Test
    public void testBalancingGeneticWithTooFewIndividuals() {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual("Johnny", 8));
        individuals.add(new Individual("Robbie", 5));

        List<List<Individual>> teams = Main.balancingGenetic(individuals, 3, 1000);

        assertNotNull("Teams should not be null", teams);
        assertEquals( "Expected 2 teams", 2, teams.size());
        assertTrue("First team should have at least 1 player", teams.get(0).size() >= 1);
        assertTrue("Second team should have at least 1 player", teams.get(1).size() >= 1);
    }

    @Test
    public void testBalancingGeneticWithUnevenTeams() {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual("Johnny", 8));
        individuals.add(new Individual("Robbie", 5));
        individuals.add(new Individual("Juliet", 3));
        individuals.add(new Individual("Scarlet", 5));
        individuals.add(new Individual("Jude", 9));
        individuals.add(new Individual("Deborah", 6));

        List<List<Individual>> teams = Main.balancingGenetic(individuals, 4, 1000);

        assertNotNull("Teams should not be null", teams);
        assertEquals("Expected 4 teams", 4, teams.size());

        int minTeamSize = teams.stream().mapToInt(List::size).min().orElse(0);
        int maxTeamSize = teams.stream().mapToInt(List::size).max().orElse(0);

        assertTrue("Teams should have similar sizes", maxTeamSize - minTeamSize <= 1);
    }

    @Test
    public void testGenerateRandomTeams() {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual("Johnny",8));
        individuals.add(new Individual("Robbie",5));
        individuals.add(new Individual("Juliet",3));
        individuals.add(new Individual("Scarlet",5));
        individuals.add(new Individual("Jude",9));
        individuals.add(new Individual("Deborah",6));

        int teamsNum = 3;
        List<List<Individual>> teams = Main.generateRandomTeams(individuals, teamsNum);

        assertNotNull(teams);
        assertEquals(teamsNum, teams.size());

        int totalIndividuals = individuals.size();
        int totalAssigned = teams.stream().mapToInt(List::size).sum();
        assertEquals(totalIndividuals, totalAssigned);
    }

    @Test
    public void testDeviation() {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual("Johnny",8));
        individuals.add(new Individual("Robbie",5));
        individuals.add(new Individual("Juliet",3));
        individuals.add(new Individual("Scarlet",5));
        individuals.add(new Individual("Jude",9));
        individuals.add(new Individual("Deborah",6));

        int teamsNum = 3;
        List<List<Individual>> teams = Main.balancingGenetic(individuals, teamsNum, 1000);

        double deviation = Main.deviation(teams, individuals);

        assertTrue(deviation >= 0);
    }

    @Test
    public void testDeviationWithOneTeam() {
        List<List<Individual>> teams = new ArrayList<>();
        List<Individual> team = new ArrayList<>();
        team.add(new Individual("Johnny", 8));
        team.add(new Individual("Robbie", 5));
        teams.add(team);

        double deviation = Main.deviation(teams, team);

        assertEquals("Expected deviation for one team is 0.0", 0.0, deviation, 0.001);
    }

    @Test
    public void testDeviationWithTwoTeams() {
        List<List<Individual>> teams = new ArrayList<>();
        List<Individual> team1 = new ArrayList<>();
        team1.add(new Individual("Johnny", 8));
        team1.add(new Individual("Robbie", 5));

        List<Individual> team2 = new ArrayList<>();
        team2.add(new Individual("Juliet", 3));
        team2.add(new Individual("Scarlet", 5));

        teams.add(team1);
        teams.add(team2);

        double deviation = Main.deviation(teams, team1);

        assertEquals("Expected deviation is 1.76", 1.76, deviation, 0.01);
    }
}
